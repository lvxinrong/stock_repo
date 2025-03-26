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
            <el-icon>
              <Search/>
            </el-icon>
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
          class="indicator-table"
          :row-class-name="tableRowClassName"
          :header-cell-style="{
            backgroundColor: '#2B5797',
            color: 'white',
            fontSize: '14px',
            fontWeight: 600,
            position: 'sticky',
            top: 0
          }"
          height="calc(100vh - 220px)">
        <!-- 表格列定义保持不变 -->
        <!-- 基础数据列 -->
        <el-table-column prop="tsCode" label="代码" width="120" fixed/>
        <el-table-column prop="tradeDate" label="日期" width="120" fixed/>
        <el-table-column prop="close" label="收盘价" width="100"/>
        <el-table-column prop="open" label="开盘价" width="100"/>
        <el-table-column prop="high" label="最高价" width="100"/>
        <el-table-column prop="low" label="最低价" width="100"/>

        <!-- 技术指标列 -->
        <el-table-column label="MACD" width="180">
          <el-table-column prop="macdDif" label="DIF" width="90">
              <template #default="{ row }">
                <span :class="getValueClass(row.macdDif)">{{ row.macdDif }}</span>
              </template>
          </el-table-column>
          <el-table-column prop="macdDea" label="DEA" width="90">
            <template #default="{ row }">
              <span :class="getValueClass(row.macdDea)">{{ row.macdDea }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="macd" label="MACD" width="90">
            <template #default="{ row }">
              <span :class="getValueClass(row.macd)">{{ row.macd }}</span>
            </template>
          </el-table-column>
        </el-table-column>

        <el-table-column label="KDJ" width="150">
          <el-table-column prop="kdjK" label="K值" width="75">
            <template #default="{ row }">
              <span :class="getValueClass(row.kdjK)">{{ row.kdjK }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="kdjD" label="D值" width="75">
            <template #default="{ row }">
              <span :class="getValueClass(row.kdjD)">{{ row.kdjD }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="kdjJ" label="J值" width="75">
            <template #default="{ row }">
              <span :class="getValueClass(row.kdjJ)">{{ row.kdjJ }}</span>
            </template>
          </el-table-column>
        </el-table-column>

        <el-table-column label="RSI" width="150">
          <el-table-column prop="rsi6" label="RSI6" width="75">
            <template #default="{ row }">
              <span :class="getValueClass(row.rsi6)">{{ row.rsi6 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="rsi12" label="RSI12" width="75">
            <template #default="{ row }">
              <span :class="getValueClass(row.rsi12)">{{ row.rsi12 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="rsi24" label="RSI24" width="75">
            <template #default="{ row }">
              <span :class="getValueClass(row.rsi24)">{{ row.rsi24 }}</span>
            </template>
          </el-table-column>
        </el-table-column>

        <el-table-column label="BOLL" width="150">
          <el-table-column prop="bollUpper" label="上轨" width="75">
            <template #default="{ row }">
              <span :class="getValueClass(row.bollUpper)">{{ row.bollUpper }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="bollMid" label="中轨" width="75">
            <template #default="{ row }">
              <span :class="getValueClass(row.bollMid)">{{ row.bollMid }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="bollLower" label="下轨" width="75">
            <template #default="{ row }">
              <span :class="getValueClass(row.bollLower)">{{ row.bollLower }}</span>
            </template>
          </el-table-column>
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
import {ref, reactive, onMounted} from 'vue'
import {ElMessage} from "element-plus";
import {fetchStockSktPageResult} from '@/api/tableData.js'
import {Search} from "@element-plus/icons-vue";

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
    const response = await fetchStockSktPageResult({
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
const tableRowClassName = ({rowIndex}) => {
  return rowIndex % 2 === 1 ? 'striped-row' : ''
}
// 初始化加载
onMounted(() => {
  fetchData()
})

// 添加数值状态判断方法
const getValueClass = (value) => {
  if (value > 5) return 'strong-positive'
  if (value > 1) return 'positive'
  if (value < -5) return 'strong-negative'
  if (value < -1) return 'negative'
  return 'neutral'
}
</script>



<style scoped lang="scss">

$finance-primary: #2B5797; // 专业金融蓝
$indicator-block-bg: rgba(43, 87, 151, 0.03); // 指标区块背景
$zebra-stripe: #F8FAFF; // 斑马纹优化

// 专业级颜色阶梯定义
$color-range: (
    strong-negative: #059669,    // 深绿
    negative:       #10b981,    // 基础绿
    neutral:        #64748b,    // 中性灰
    positive:       #ef4444,    // 基础红positive
    strong-positive: #dc2626    // 深红

);

// 动态颜色应用
@each $name, $color in $color-range {
  .#{$name} {
    color: $color;
    font-weight: 600;
    position: relative;

    &::after {
      content: '';
      position: absolute;
      right: -8px;
      top: 50%;
      transform: translateY(-50%);
      width: 6px;
      height: 6px;
      border-radius: 50%;
      background: $color;
      opacity: 0.3;
    }
  }
}

// 增强数值可读性
.positive, .negative {
  font-family: 'DIN Alternate', sans-serif;
  letter-spacing: -0.05em;

  &::before {
    content: attr(data-symbol);
    font-size: 0.8em;
    margin-right: 2px;
  }
}

.stock-indicator-container {
  // 主背景升级为动态渐变
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);

  .el-card {
    border-radius: 12px;
    box-shadow: 0 4px 24px rgba(43, 87, 151, 0.08); // 更柔和的投影

    &__header {
      background: linear-gradient(95deg, $finance-primary 0%, lighten($finance-primary, 8%) 100%);
      color: white;
      border-radius: 12px 12px 0 0;
    }
  }
}

// 指标区块视觉分层
.el-table {
  [label="MACD"] { background: $indicator-block-bg; }
  [label="KDJ"] { background: darken($indicator-block-bg, 2%); }
  [label="RSI"] { background: darken($indicator-block-bg, 4%); }

  // 数值状态标识升级
  .cell {
    .negative {
      color: #EF4444;
      &::before { content: "↓ "; font-weight: 800; }
    }
    .positive {
      color: #10B981;
      &::before { content: "↑ "; font-weight: 800; }
    }
  }
}

:root {
  --font-title: 'Inter', sans-serif;
  --font-data: 'JetBrains Mono', monospace;
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

// 骨架屏动画
@keyframes skeleton-wave {
  50% { opacity: 0.6; }
}

.el-loading-mask {
  background: rgba(255,255,255,0.9);

  .el-loading-spinner {
    .circular {
      path {
        stroke: $finance-primary;
        stroke-width: 3px;
      }
    }
  }
}

//// 应用到MACD等指标列
//[prop^="macd"] .cell {
//  color: get-color(cellData);
//  font-weight: 500;
//}

@media (max-width: 768px) {
  // 隐藏次要指标列
  [label="RSI"], [label="BOLL"] {
    display: none;
  }

  // 输入框紧凑布局
  .search-form {
    .el-form-item {
      margin-bottom: 12px;

      .el-input__wrapper {
        padding: 0 12px;
      }
    }
  }

  // 分页组件优化
  .el-pagination {
    :deep(.btn-prev), :deep(.btn-next) {
      display: none;
    }
  }
}
</style>