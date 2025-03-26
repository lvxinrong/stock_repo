<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="title">沪深300指数成分股</h1>
      <p class="subtitle">展示沪深300指数的所有成分股数据</p>
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
              format="YYYY-MM-DD"
            />
          </el-form-item>
          <el-form-item>
            <el-button 
              type="primary" 
              class="custom-button"
              @click="handleSearch"
            >
              查询
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="data-summary">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="data-card">
              <div class="card-title">指数点位</div>
              <div class="card-value">{{ indexValue }}</div>
              <div class="card-footer">当前沪深300指数点位</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="data-card">
              <div class="card-title">涨跌幅</div>
              <div class="card-value" :class="indexChange >= 0 ? 'price-up' : 'price-down'">
                {{ indexChange }}%
              </div>
              <div class="card-footer">较上一交易日变化</div>
            </div>
          </el-col>
          <!-- 添加更多数据卡片... -->
        </el-row>
      </div>

      <div class="custom-table">
        <el-table
          :data="tableData"
          :border="false"
          v-loading="loading"
        >
          <el-table-column prop="stock_code" label="股票代码" width="120">
            <template #default="{ row }">
              <router-link 
                :to="`/stock/${row.stock_code}`"
                class="stock-code"
              >
                {{ row.stock_code }}
              </router-link>
            </template>
          </el-table-column>
          <el-table-column prop="stock_name" label="股票名称" width="120" />
          <el-table-column prop="close_price" label="收盘价" width="100">
            <template #default="{ row }">
              <span :class="row.change_percent >= 0 ? 'price-up' : 'price-down'">
                {{ row.close_price }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="change_percent" label="涨跌幅" width="100">
            <template #default="{ row }">
              <span :class="row.change_percent >= 0 ? 'price-up' : 'price-down'">
                {{ row.change_percent }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="volume" label="成交量" width="120">
            <template #default="{ row }">
              {{ formatVolume(row.volume) }}
            </template>
          </el-table-column>
          <el-table-column prop="turnover" label="成交额" width="120">
            <template #default="{ row }">
              {{ formatTurnover(row.turnover) }}
            </template>
          </el-table-column>
        </el-table>
      </div>

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
</template>

<script setup>
import { ref, reactive } from 'vue'
import { getHuShen300Data } from '@/api/tableData'
import dayjs from 'dayjs'

// 数据状态
const tableData = ref([])
const loading = ref(false)
const indexValue = ref('--')
const indexChange = ref('--')

// 查询参数
const queryParams = reactive({
  tradeDate: dayjs().format('YYYY-MM-DD')
})

// 分页参数
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 格式化函数
const formatVolume = (value) => {
  return value ? `${(value / 1e6).toFixed(2)}万手` : '--'
}

const formatTurnover = (value) => {
  return value ? `${(value / 1e8).toFixed(2)}亿` : '--'
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
      tradeDate: dayjs(queryParams.tradeDate).format('YYYYMMDD'),
      page: pagination.current - 1,
      size: pagination.size
    }
    
    const response = await getHuShen300Data(params)
    tableData.value = response.items
    pagination.total = response.total
    
    // 更新指数数据
    if (response.indexData) {
      indexValue.value = response.indexData.close.toFixed(2)
      indexChange.value = response.indexData.change_percent.toFixed(2)
    }
  } catch (error) {
    console.error('数据加载失败:', error)
  } finally {
    loading.value = false
  }
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

// 初始加载
handleSearch()
</script>

<style lang="scss" scoped>
@import '@/styles/common.scss';

.filter-form {
  margin-bottom: $spacing-lg;
}

.data-summary {
  margin-bottom: $spacing-xl;
}

.stock-code {
  color: $secondary-color;
  text-decoration: none;
  font-weight: 500;
  
  &:hover {
    color: darken($secondary-color, 10%);
  }
}
</style> 