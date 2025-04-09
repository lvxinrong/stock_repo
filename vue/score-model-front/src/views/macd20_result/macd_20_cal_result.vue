<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="title">MACD技术指标分析</h1>
      <p class="subtitle">基于MACD 20日线的技术分析结果</p>
    </div>

    <div class="card-container">
      <div class="filter-form form-container">
        <el-form :inline="true">
          <el-form-item label="日期选择">
            <el-date-picker
              v-model="selectedDate"
              type="date"
              placeholder="选择日期"
              :disabled-date="disabledDate"
              value-format="YYYYMMDD"
              format="YYYY-MM-DD"
              @change="handleDateChange"
            />
          </el-form-item>
          <el-form-item label="买入信号">
            <el-select 
              v-model="queryParams.buySignal" 
              placeholder="请选择"
              @change="handleSearch"
            >
              <el-option label="全部" value="" />
              <el-option label="是" :value="true" />
              <el-option label="否" :value="false" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button 
              type="primary" 
              class="custom-button"
              @click="handleSearch"
              :loading="loading"
            >
              查询
            </el-button>
            <el-button 
              class="custom-button"
              @click="resetSearch"
            >
              重置
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 添加已选条件展示 -->
        <div class="selected-conditions" v-if="hasSelectedConditions">
          <span class="condition-label">已选条件：</span>
          <el-tag 
            v-if="queryParams.buySignal !== ''" 
            class="condition-tag"
            closable
            @close="clearBuySignal"
          >
            买入信号：{{ getBuySignalText(queryParams.buySignal) }}
          </el-tag>
        </div>
      </div>

      <div class="custom-table">
        <el-table
          :data="tableData"
          :border="false"
          v-loading="loading"
          size="small"
          class="compact-table"
          @sort-change="handleSortChange"
        >
          <el-table-column prop="tsCode" label="股票代码" width="120">
            <template #default="{ row }">
              <el-popover
                placement="right"
                :width="300"
                trigger="hover"
                :loading="row.loadingDetail"
                :disabled="!row.stockDetail"
                @show="handlePopoverShow(row)"
              >
                <template #default>
                  <div class="stock-detail-popover" v-if="row.stockDetail">
                    <div class="detail-header">
                      <span class="stock-name">{{ row.stockDetail.stockName }}</span>
                      <span class="stock-code">{{ row.stockDetail.tsCode }}</span>
                    </div>
                    <div class="detail-content">
                      <div class="detail-item">
                        <span class="label">最新价</span>
                        <span class="value" :class="getValueClass(row.stockDetail.changePercent)">
                          {{ row.stockDetail.closePrice }}
                        </span>
                      </div>
                      <div class="detail-item">
                        <span class="label">涨跌幅</span>
                        <span class="value" :class="getValueClass(row.stockDetail.changePercent)">
                          {{ row.stockDetail.changePercent }}%
                        </span>
                      </div>
                      <div class="detail-item">
                        <span class="label">成交量</span>
                        <span class="value">{{ formatVolume(row.stockDetail.volume) }}</span>
                      </div>
                      <div class="detail-item">
                        <span class="label">市值</span>
                        <span class="value">{{ formatMarketCap(row.stockDetail.marketCap) }}</span>
                      </div>
                      <div class="detail-item">
                        <span class="label">行业</span>
                        <span class="value">{{ row.stockDetail.industry || '--' }}</span>
                      </div>
                    </div>
                  </div>
                  <div v-else-if="row.loadingDetail" class="loading-content">
                    <el-icon class="is-loading"><Loading /></el-icon>
                    <span>加载中...</span>
                  </div>
                </template>
                <template #reference>
                  <router-link 
                    :to="`/stock/${row.tsCode}`"
                    class="stock-code"
                  >
                    {{ row.tsCode }}
                  </router-link>
                </template>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column prop="stockName" label="股票名称" width="120" />
          <el-table-column prop="trendDirection" label="趋势方向" width="100">
            <template #default="{ row }">
              <span :class="row.trendDirection === 'UP' ? 'price-up' : 'price-down'">
                {{ row.trendDirection === 'UP' ? '上升' : '下降' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="trendStrength" label="趋势强度" width="120">
            <template #default="{ row }">
              <span :class="row.trendStrength >= 0 ? 'price-up' : 'price-down'">
                {{ row.trendStrength?.toFixed(2) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="maAlignment" label="均线排列" width="120">
            <template #default="{ row }">
              <span :class="row.maAlignment === 'BULLISH' ? 'price-up' : 'price-down'">
                {{ row.maAlignment === 'BULLISH' ? '多头' : '空头' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="positiveMacdArea" label="MACD面积" width="120">
            <template #default="{ row }">
              <span :class="row.positiveMacdArea >= 0 ? 'price-up' : 'price-down'">
                {{ row.positiveMacdArea?.toFixed(2) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="buySignal" label="买入信号" width="120">
            <template #default="{ row }">
              <span :class="row.buySignal ? 'price-up' : 'price-down'">
                {{ row.buySignal ? '是' : '否' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column 
            prop="cumulativeIncrease" 
            label="累计涨幅" 
            width="120"
            sortable="custom"
            :sort-orders="['ascending', 'descending']"
          >
            <template #default="{ row }">
              <span :class="row.cumulativeIncrease >= 0 ? 'price-up' : 'price-down'">
                {{ row.cumulativeIncrease?.toFixed(2) }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column 
            prop="buySignalTrueCount" 
            label="推荐次数" 
            width="100"
            sortable="custom"
            :sort-orders="['ascending', 'descending']"
          >
            <template #default="{ row }">
              <span>{{ row.buySignalTrueCount }}</span>
            </template>
          </el-table-column>
          <el-table-column 
            prop="totalVm" 
            label="最新市值(亿元)" 
            width="120"
            sortable="custom"
            :sort-orders="['ascending', 'descending']"
          >
            <template #default="{ row }">
              <span>{{ row.totalVm?.toFixed(1) }}亿元</span>
            </template>
          </el-table-column>
          <el-table-column prop="buyFormatString" label="决策建议" min-width="120" />
        </el-table>

        <div class="pagination-container">
          <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :total="pagination.total"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, reactive, ref} from 'vue'
import {Loading} from '@element-plus/icons-vue'
import {ElMessage} from "element-plus"
import {fetchStockBasicInfo, macd20DaysCalResult} from '@/api/tableData.js'
import dayjs from 'dayjs'

// 新增对话框相关状态
const dialogVisible = ref(false)
const detailData = reactive({})
const loadingDetail = ref(false)
// 获取当前日期（YYYYMMDD格式）
const getCurrentDate = () => dayjs().format('YYYYMMDD')
// 新增的dialogStyle声明
const dialogStyle = ref({
  left: '0px',
  top: '0px',
  visibility: 'hidden' // 初始隐藏
})
// 格式化工具函数
const formatMarketCap = (value) => {
  return value !== null && value !== undefined ? `${(value / 10000).toFixed(2)}亿` : '--';
}

const formatVolume = (value) => {
  return value ? `${(value / 1e6).toFixed(2)}万` : '--'
}

const formatChangePercent = (value) => {
  return value ? `${value}%` : '--'
}

// 声明方法
// 修正后的 handleCodeClick 方法
const handleCodeClick = async (code) => {
  try {
    // 先显示loading状态
    loadingDetail.value = true


    // 调用接口获取数据
    const response = await fetchStockBasicInfo(code)
    // 更新详情数据
    Object.assign(detailData, response)

  } catch (error) {
    console.error('获取股票详情失败:', error)
    ElMessage.error('数据加载失败，请稍后重试')
  } finally {
    // 隐藏loading状态
    loadingDetail.value = false
  }
}

// 状态定义
const selectedDate = ref(dayjs().format('YYYYMMDD'))
const tableData = ref([])
const loading = ref(false)

// 分页参数
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 查询参数
const queryParams = reactive({
  tradeDate: dayjs().format('YYYYMMDD'),
  buySignal: ''
})

// 判断是否有已选条件
const hasSelectedConditions = computed(() => {
  return queryParams.buySignal !== ''
})

// 获取买入信号显示文本
const getBuySignalText = (value) => {
  if (value === '') return '全部'
  return value ? '是' : '否'
}

// 清除买入信号条件
const clearBuySignal = () => {
  queryParams.buySignal = ''
  handleSearch()
}

// 日期限制：不能选择未来日期
const disabledDate = (date) => {
  return date > new Date()
}

// 处理日期变化
const handleDateChange = (val) => {
  selectedDate.value = val
  pagination.current = 1 // 重置页码
  handleSearch()
}

// 查询方法
const handleSearch = async () => {
  if (!selectedDate.value) {
    ElMessage.warning('请选择日期')
    return
  }

  try {
    loading.value = true
    const params = {
      tradeDate: selectedDate.value,
      page: pagination.current - 1,
      size: pagination.size,
      buySignal: queryParams.buySignal === '' ? null : queryParams.buySignal
    }

    const response = await macd20DaysCalResult(params)
    
    if (response && response.items) {
      tableData.value = response.items
      pagination.total = response.total
    }
  } catch (error) {
    console.error('查询失败:', error)
    ElMessage.error('数据加载失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 重置搜索
const resetSearch = () => {
  selectedDate.value = dayjs().format('YYYYMMDD')
  queryParams.buySignal = ''
  pagination.current = 1
  handleSearch()
}

// 分页处理
const handleSizeChange = (size) => {
  pagination.size = size
  handleSearch()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  handleSearch()
}

// 处理气泡显示
const handlePopoverShow = async (row) => {
  if (row.stockDetail || row.loadingDetail) return
  
  try {
    row.loadingDetail = true
    row.stockDetail = await fetchStockBasicInfo(row.tsCode)
  } catch (error) {
    console.error('获取股票详情失败:', error)
    ElMessage.error('获取股票详情失败')
  } finally {
    row.loadingDetail = false
  }
}

// 获取数值的样式类
const getValueClass = (value) => {
  if (!value) return ''
  return value >= 0 ? 'price-up' : 'price-down'
}

// 页面加载时执行初始查询
onMounted(() => {
  handleSearch()
})

// 添加表格的排序事件处理
const handleSortChange = ({ prop, order }) => {
  if (!order) {
    // 取消排序时恢复原始顺序
    tableData.value = [...tableData.value]
    return
  }

  tableData.value.sort((a, b) => {
    let aValue = a[prop]
    let bValue = b[prop]
    
    // 处理空值
    if (aValue === null || aValue === undefined) return 1
    if (bValue === null || bValue === undefined) return -1
    
    // 确保是数字类型
    aValue = Number(aValue)
    bValue = Number(bValue)
    
    // 根据排序方向返回比较结果
    return order === 'ascending' 
      ? aValue - bValue 
      : bValue - aValue
  })
}
</script>

<style lang="scss" scoped>
@import '@/styles/common.scss';

.filter-form {
  margin-bottom: $spacing-lg;
}

.stock-code {
  color: $secondary-color;
  text-decoration: none;
  font-weight: 500;
  
  &:hover {
    color: darken($secondary-color, 10%);
  }
}

// 添加选择器样式
:deep(.el-select) {
  width: 120px;
}

.selected-conditions {
  margin-top: $spacing-sm;
  display: flex;
  align-items: center;
  
  .condition-label {
    color: $text-secondary;
    margin-right: $spacing-sm;
  }
  
  .condition-tag {
    margin-right: $spacing-sm;
    background-color: rgba($secondary-color, 0.1);
    border-color: rgba($secondary-color, 0.2);
    color: $secondary-color;
    
    &:hover {
      background-color: rgba($secondary-color, 0.15);
    }
  }
}

.compact-table {
  :deep(.el-table__header) {
    th {
      padding: $spacing-xs $spacing-sm !important;
      height: 40px !important;
      line-height: 20px !important;
      background-color: $background-light;
      font-size: 13px;
    }
  }

  :deep(.el-table__body) {
    td {
      padding: $spacing-xs $spacing-sm !important;
      height: 36px !important;
      line-height: 20px !important;
      font-size: 13px;
    }
  }

  :deep(.el-tag) {
    height: 22px;
    line-height: 20px;
    padding: 0 8px;
    font-size: 12px;
  }

  .stock-code {
    font-size: 13px;
    padding: 2px 0;
  }

  :deep(.cell) {
    display: flex;
    align-items: center;
    
    &.is-right {
      justify-content: flex-end;
    }
  }

  :deep(.el-table__row) {
    &:hover {
      td {
        transform: translateY(-1px);
      }
    }
  }
}

.pagination-container {
  margin-top: $spacing-md;
  
  :deep(.el-pagination) {
    padding: $spacing-xs 0;
    font-size: 13px;
    
    .el-pagination__sizes {
      margin-right: $spacing-sm;
    }
    
    .el-select .el-input {
      margin: 0 $spacing-xs;
    }
  }
}

.stock-detail-popover {
  .detail-header {
    padding-bottom: $spacing-sm;
    margin-bottom: $spacing-sm;
    border-bottom: 1px solid #eee;
    
    .stock-name {
      font-size: 16px;
      font-weight: 600;
      margin-right: $spacing-sm;
    }
    
    .stock-code {
      color: $text-secondary;
      font-size: 14px;
    }
  }
  
  .detail-content {
    .detail-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: $spacing-xs;
      
      .label {
        color: $text-secondary;
        font-size: 13px;
      }
      
      .value {
        font-size: 13px;
        font-weight: 500;
      }
    }
  }
}

.loading-content {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: $spacing-md;
  color: $text-secondary;
  
  .el-icon {
    margin-right: $spacing-xs;
    font-size: 16px;
  }
}

:deep(.el-popover.el-popper) {
  padding: $spacing-md;
  border-radius: $border-radius-md;
  box-shadow: $box-shadow-medium;
}
</style>