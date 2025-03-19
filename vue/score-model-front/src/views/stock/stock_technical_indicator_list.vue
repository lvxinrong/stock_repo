<template>
  <div class="stock-indicator-container">
    <!-- 查询条件模块 -->
    <el-card shadow="never" class="query-card">
      <el-form :inline="true" class="search-form" @submit.prevent>
        <el-form-item label="股票代码" class="form-item">
          <el-input
              v-model="queryParams.tsCode"
              placeholder="输入股票代码/名称"
              clearable
              suffix-icon="Search"
              class="custom-input"
          />
        </el-form-item>
        <el-form-item label="交易日期" class="form-item">
          <el-date-picker
              v-model="queryParams.tradeDate"
              type="date"
              value-format="YYYYMMDD"
              placeholder="选择日期"
              class="custom-date"
              :popper-class="'date-popper'"
          />
        </el-form-item>
        <el-form-item>
          <el-button
              type="primary"
              @click="handleSearch"
              class="query-button"
          >
            <el-icon><Search /></el-icon>
            <span>查询</span>
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据展示模块 -->
    <el-card shadow="never" class="table-card">
      <el-table
          v-loading="loading"
          :data="tableData"
          stripe
          style="width: 100%"
          height="100%"
          class="indicator-table"
          :header-cell-style="headerStyle"
          :row-class-name="tableRowClassName"
      >
        <!-- 表格列定义保持不变 -->
        <!-- 基础数据列 -->
        <el-table-column prop="tsCode" label="代码" width="120" fixed />
        <el-table-column prop="tradeDate" label="日期" width="120" fixed />
        <el-table-column prop="close" label="收盘价" width="100" />
        <el-table-column prop="open" label="开盘价" width="100" />
        <el-table-column prop="high" label="最高价" width="100" />
        <el-table-column prop="low" label="最低价" width="100" />

        <!-- 技术指标列 -->
        <el-table-column label="MACD" width="180">
          <el-table-column prop="macdDif" label="DIF" width="90" />
          <el-table-column prop="macdDea" label="DEA" width="90" />
          <el-table-column prop="macd" label="MACD" width="90" />
        </el-table-column>

        <el-table-column label="KDJ" width="150">
          <el-table-column prop="kdjK" label="K值" width="75" />
          <el-table-column prop="kdjD" label="D值" width="75" />
          <el-table-column prop="kdjJ" label="J值" width="75" />
        </el-table-column>

        <el-table-column label="RSI" width="150">
          <el-table-column prop="rsi6" label="RSI6" width="75" />
          <el-table-column prop="rsi12" label="RSI12" width="75" />
          <el-table-column prop="rsi24" label="RSI24" width="75" />
        </el-table-column>

        <el-table-column label="BOLL" width="150">
          <el-table-column prop="bollUpper" label="上轨" width="75" />
          <el-table-column prop="bollMid" label="中轨" width="75" />
          <el-table-column prop="bollLower" label="下轨" width="75" />
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <el-pagination
          background
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          class="pagination"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import {ElMessage} from "element-plus";
import { fetchStockSktPageResult } from '@/api/tableData.js'

const loading = ref(false)
const tableData = ref([])

// 查询参数
const queryParams = reactive({
  tsCode: '',
  tradeDate: ''
})

// 分页参数
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 获取数据
const fetchData = async () => {
  try {
    loading.value = true
    const response  = await fetchStockSktPageResult( {
        ...queryParams,
        page: pagination.current,
        size: pagination.size
    })

    tableData.value = response.records
    pagination.total = response.total
  } catch (error) {
    console.error('数据获取失败:', error)
    ElMessage.error('数据加载失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 分页事件处理
const handleSizeChange = (newSize) => {
  pagination.size = newSize
  fetchData()
}

const handleCurrentChange = (newPage) => {
  pagination.current = newPage
  fetchData()
}

// 查询条件处理
const handleSearch = () => {
  pagination.current = 1
  fetchData()
}

// 添加表格行样式
const tableRowClassName = ({ rowIndex }) => {
  return rowIndex % 2 === 1 ? 'striped-row' : ''
}

// 表头样式配置
const headerStyle = {
  backgroundColor: '#f8fafc',
  color: '#1e293b',
  fontSize: '14px',
  fontWeight: '600'
}

// 初始化加载
onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">

$primary-gradient: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
$positive-color: #10b981;
$negative-color: #ef4444;


.indicator-table {
  // 斑马纹增强
  :deep(.striped-row) {
    background: rgba(241, 245, 249, 0.5) !important;
  }

  // 指标列高亮
  [label="MACD"] { background: rgba(99, 102, 241, 0.05) }
  [label="KDJ"] { background: rgba(139, 92, 246, 0.05) }

  // 数值状态标识
  .cell {
    .negative { color: $negative-color; &::before { content: "▼ " } }
    .positive { color: $positive-color; &::before { content: "▲ " } }
  }
}

:root {
  --font-title: 'Inter', sans-serif;
  --font-data: 'JetBrains Mono', monospace;
}

.el-table {
  th { font-family: var(--font-title) }
  td { font-family: var(--font-data) }
}

.stock-indicator-container {
  padding: 24px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);

  .el-card {
    border-radius: 16px;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
    border: none;

    &__header {
      padding: 18px 24px;
      background: #ffffff;
      border-bottom: 1px solid #e2e8f0;
    }
  }
}

// 表格行悬浮动画
:deep(.el-table__row) {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 6px -1px rgba(99, 102, 241, 0.1);
  }
}

// 输入框动效
.custom-input {
  transition: all 0.3s;
  &:focus-within {
    box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.2);
  }
}


@media (max-width: 768px) {
  .search-form {
    flex-direction: column;

    .el-form-item {
      width: 100%;
      margin-right: 0;

      .custom-input { width: 100% }
    }
  }

  .el-table-column { min-width: 120px }
}
</style>