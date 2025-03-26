<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="title">筹码集中度分析</h1>
      <p class="subtitle">展示股票筹码集中度及相关指标分析</p>
    </div>

    <div class="card-container">
      <div class="filter-form form-container">
        <el-form :inline="true">
          <el-form-item label="交易日期">
            <el-date-picker
              v-model="queryParams.tradeDate"
              type="date"
              placeholder="选择日期"
              :disabled-date="disabledDate"
              value-format="YYYYMMDD"
              format="YYYY-MM-DD"
              @change="handleSearch"
            />
          </el-form-item>
          <el-form-item label="综合评价">
            <el-select 
              v-model="queryParams.isGood" 
              placeholder="请选择"
              clearable
              @change="handleSearch"
            >
              <el-option label="全部" value="" />
              <el-option label="推荐" :value="true" />
              <el-option label="不推荐" :value="false" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button 
              type="primary" 
              class="custom-button"
              @click="handleSearch"
              :loading="loading"
            >
              <el-icon><Search /></el-icon>
              查询
            </el-button>
            <el-button 
              class="custom-button"
              @click="resetSearch"
            >
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 添加已选条件展示 -->
        <div class="selected-conditions" v-if="hasSelectedConditions">
          <span class="condition-label">已选条件：</span>
          <el-tag 
            v-if="queryParams.isGood !== ''" 
            class="condition-tag"
            closable
            @close="clearIsGood"
          >
            综合评价：{{ getIsGoodText(queryParams.isGood) }}
          </el-tag>
        </div>
      </div>

      <!-- 数据概览卡片 -->
      <div class="data-summary">
        <el-row :gutter="24">
          <el-col :span="6">
            <div class="data-card">
              <div class="card-header">
                <div class="card-title">总股票数</div>
                <el-icon class="card-icon"><DataLine /></el-icon>
              </div>
              <div class="card-value">{{ totalCount }}</div>
              <div class="card-footer">当日分析的股票总数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="data-card">
              <div class="card-header">
                <div class="card-title">集中度有效</div>
                <el-icon class="card-icon"><TrendCharts /></el-icon>
              </div>
              <div class="card-value price-up">{{ convergeValidCount }}</div>
              <div class="card-footer">筹码集中度有效的股票数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="data-card">
              <div class="card-header">
                <div class="card-title">控盘有效</div>
                <el-icon class="card-icon"><PieChart /></el-icon>
              </div>
              <div class="card-value price-up">{{ controlValidCount }}</div>
              <div class="card-footer">控盘指标有效的股票数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="data-card">
              <div class="card-header">
                <div class="card-title">综合有效</div>
                <el-icon class="card-icon"><Medal /></el-icon>
              </div>
              <div class="card-value price-up">{{ goodCount }}</div>
              <div class="card-footer">所有指标均有效的股票数</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 数据表格 -->
      <div class="custom-table">
        <el-table
          :data="tableData"
          :border="false"
          v-loading="loading"
          size="small"
          class="compact-table"
        >
          <el-table-column prop="tsCode" label="股票代码" width="120">
            <template #default="{ row }">
              <router-link 
                :to="`/stock/${row.tsCode}`"
                class="stock-code"
              >
                {{ row.tsCode }}
              </router-link>
            </template>
          </el-table-column>
          <el-table-column prop="stockName" label="股票名称" width="100">
            <template #default="{ row }">
              <span>{{ row.stockName }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="currentPrice" label="当前价格" width="100">
            <template #default="{ row }">
              <span>{{ row.currentPrice?.toFixed(2) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="lastConvergeRatio" label="集中度" width="100">
            <template #default="{ row }">
              <span>{{ (row.lastConvergeRatio * 100)?.toFixed(2) }}%</span>
            </template>
          </el-table-column>
          <el-table-column prop="convergeValid" label="集中度有效" width="100">
            <template #default="{ row }">
              <el-tag :type="row.convergeValid ? 'success' : 'info'" size="small">
                {{ row.convergeValid ? '是' : '否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="controlValid" label="控盘有效" width="100">
            <template #default="{ row }">
              <el-tag :type="row.controlValid ? 'success' : 'info'" size="small">
                {{ row.controlValid ? '是' : '否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="priceValid" label="价格有效" width="100">
            <template #default="{ row }">
              <el-tag :type="row.priceValid ? 'success' : 'info'" size="small">
                {{ row.priceValid ? '是' : '否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="isGood" label="综合评价" width="100">
            <template #default="{ row }">
              <el-tag :type="row.isGood ? 'success' : 'info'" size="small">
                {{ row.isGood ? '推荐' : '不推荐' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="集中度趋势" min-width="300">
            <template #default="{ row }">
              <div class="trend-chart" ref="trendChartRef">
                <v-chart :option="getTrendChartOption(row.convergeRatio)" autoresize />
              </div>
            </template>
          </el-table-column>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { Search, Refresh, DataLine, TrendCharts, PieChart, Medal } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import { GridComponent, TooltipComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import {fetchStockCyqConverge} from "@/api/tableData.js";

// 注册必要的 ECharts 组件
use([
  CanvasRenderer,
  LineChart,
  GridComponent,
  TooltipComponent
])

// 状态定义
const loading = ref(false)
const tableData = ref([])

// 查询参数
const queryParams = reactive({
  tradeDate: dayjs().format('YYYYMMDD'),
  isGood: ''  // 添加 isGood 参数
})

// 分页参数
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 计算属性
const totalCount = computed(() => pagination.total)
const convergeValidCount = computed(() => 
  tableData.value.filter(item => item.convergeValid).length
)
const controlValidCount = computed(() => 
  tableData.value.filter(item => item.controlValid).length
)
const goodCount = computed(() => 
  tableData.value.filter(item => item.isGood).length
)

// 计算是否有选中的查询条件
const hasSelectedConditions = computed(() => {
  return queryParams.isGood !== ''
})

// 获取综合评价文本
const getIsGoodText = (value) => {
  if (value === true) return '推荐'
  if (value === false) return '不推荐'
  return '全部'
}

// 清除综合评价条件
const clearIsGood = () => {
  queryParams.isGood = ''
  handleSearch()
}

// 获取趋势图配置
const getTrendChartOption = (data) => {
  return {
    grid: {
      top: 5,
      right: 5,
      bottom: 5,
      left: 5,
      containLabel: false
    },
    xAxis: {
      type: 'category',
      show: false
    },
    yAxis: {
      type: 'value',
      show: false
    },
    tooltip: {
      trigger: 'axis',
      position: function (point, params, dom, rect, size) {
        // 计算tooltip的位置，确保它显示在鼠标上方
        return [point[0], Math.max(point[1] - size.contentSize[1] - 10, 0)];
      },
      formatter: (params) => {
        const value = params[0].value
        return `集中度: ${(value * 100).toFixed(2)}%`
      },
      extraCssText: 'z-index: 999;'  // 确保tooltip显示在最上层
    },
    series: [{
      data: data,
      type: 'line',
      smooth: true,
      symbol: 'none',
      lineStyle: {
        width: 2,
        color: '#3498db'
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0,
            color: 'rgba(52, 152, 219, 0.2)'
          }, {
            offset: 1,
            color: 'rgba(52, 152, 219, 0)'
          }]
        }
      }
    }]
  }
}

// 日期限制
const disabledDate = (date) => {
  return date > new Date()
}

// 查询方法
const handleSearch = async () => {
  try {
    loading.value = true
    const params = {
      tradeDate: queryParams.tradeDate,
      page: pagination.current - 1,
      size: pagination.size,
      isGood: queryParams.isGood === '' ? null : queryParams.isGood  // 处理全部的情况
    }
    
    const response = await fetchStockCyqConverge(params)
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
  queryParams.tradeDate = dayjs().format('YYYYMMDD')
  queryParams.isGood = ''  // 重置 isGood
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

// 页面加载时执行初始查询
onMounted(() => {
  handleSearch()
})
</script>

<style lang="scss" scoped>
@import '@/styles/common.scss';

.trend-chart {
  height: 50px;
  width: 100%;
  position: relative;  // 添加相对定位
}

:deep(.el-table__row) {
  .trend-chart {
    .echarts {
      height: 100%;
      width: 100%;
      
      // 确保tooltip不会被表格遮挡
      :deep(.echarts-tooltip) {
        z-index: 9999;
      }
    }
  }
}

.compact-table {
  // ... 其他样式保持不变 ...
}

// 添加选择器样式
:deep(.el-select) {
  width: 120px;
}
</style> 