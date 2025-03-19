<template>
  <div class="container">
    <!-- 查询条件表单 -->
    <el-card class="search-form" shadow="hover">
      <el-form :model="queryParams" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="股票代码">
              <el-input
                  v-model="queryParams.tsCode"
                  placeholder="000001.SZ"
                  clearable
                  suffix-icon="Search"
              />
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="趋势方向">
              <el-select
                  v-model="queryParams.trendDirection"
                  clearable
                  placeholder="请选择"
                  class="full-width-select"
              >
                <el-option label="上涨" value="UP" />
                <el-option label="下跌" value="DOWN" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="均线排列">
              <el-select
                  v-model="queryParams.maAlignment"
                  clearable
                  placeholder="请选择"
                  class="full-width-select"
              >
                <el-option label="多头排列" value="BULLISH" />
                <el-option label="空头排列" value="BEARISH" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="趋势强度">
              <div class="flex-range-inputs">
                <el-input-number
                    v-model="queryParams.trendStrengthMin"
                    :min="0"
                    :max="100"
                    placeholder="最小值"
                    controls-position="right"
                />
                <span class="range-separator">至</span>
                <el-input-number
                    v-model="queryParams.trendStrengthMax"
                    :min="0"
                    :max="100"
                    placeholder="最大值"
                    controls-position="right"
                />
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="买入信号">
              <el-switch
                  v-model="queryParams.buySignal"
                  inline-prompt
                  :active-icon="Check"
                  :inactive-icon="Close"
                  style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" class="form-actions">
            <el-button
                type="primary"
                @click="handleSearch"
                :loading="loading"
                class="action-btn"
            >
              <template #icon><Search /></template>
              查询
            </el-button>
            <el-button
                @click="resetSearch"
                class="action-btn reset-btn"
            >
              重置
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="data-table" shadow="never">
      <el-table
          :data="tableData"
          v-loading="loading"
          stripe
          style="width: 100%"
          header-row-class-name="table-header"
      >
        <el-table-column prop="tsCode" label="股票代码" width="120" />
        <el-table-column prop="lastTradeDate" label="最后交易日" width="120" />
        <el-table-column label="趋势方向" width="100">
          <template #default="{ row }">
            <el-tag :type="row.trendDirection === 'UP' ? 'success' : 'danger'">
              {{ row.trendDirection === 'UP' ? '上涨' : '下跌' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="trendStrength" label="趋势强度" width="120">
          <template #default="{ row }">
            {{ (row.trendStrength * 100).toFixed(2) }}%
          </template>
        </el-table-column>
        <el-table-column prop="maAlignment" label="均线排列" width="120">
          <template #default="{ row }">
            <el-tag :type="row.maAlignment === 'BULLISH' ? 'success' : 'danger'">
              {{ row.maAlignment === 'BULLISH' ? '多头' : '空头' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="买入信号" width="100">
          <template #default="{ row }">
            <el-icon :color="row.buySignal ? '#67C23A' : '#F56C6C'">
              <CircleCheckFilled v-if="row.buySignal" />
              <CircleCloseFilled v-else />
            </el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="buyFormatString" label="操作建议" />
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
            background
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :total="pagination.total"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSearch"
            @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import {Search, CircleCheckFilled, CircleCloseFilled, Close, Check} from '@element-plus/icons-vue'
import {ElMessage} from "element-plus";
import { macd20DaysCalResult } from '@/api/tableData.js'

// 初始化查询参数
const queryParams = reactive({
  tsCode: '',
  trendDirection: '',
  maAlignment: '',
  trendStrengthMin: null,
  trendStrengthMax: null,
  buySignal: null
})

// 分页参数
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 表格数据
const tableData = ref([])
const loading = ref(false)

// 处理搜索
const handleSearch = async () => {
  try {
    loading.value = true
    const params = {
      ...queryParams,
      page: pagination.current - 1, // 后端页码从0开始
      size: pagination.size
    }

    // 清理空值参数
    const cleanedParams = Object.fromEntries(
        Object.entries(params).filter(([_, v]) => v !== null && v !== '')
    )

    const response = await macd20DaysCalResult(cleanedParams)

    tableData.value = response.items
    pagination.total = response.total
  } catch (error) {
    console.error('搜索失败:', error)
    ElMessage.error('数据加载失败')
  } finally {
    loading.value = false
  }
}

// 分页变化处理
const handlePageChange = (newPage) => {
  pagination.current = newPage
  handleSearch()
}

// 重置搜索
const resetSearch = () => {
  Object.assign(queryParams, {
    tsCode: '',
    trendDirection: '',
    maAlignment: '',
    trendStrengthMin: null,
    trendStrengthMax: null,
    buySignal: null
  })
  pagination.current = 1
  handleSearch()
}

// 初始化加载数据
handleSearch()
</script>

<style scoped lang="scss">
.container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.search-form {
  margin-bottom: 20px;
  border-radius: 8px;
  transition: all 0.3s;

  :deep(.el-card__body) {
    padding: 24px 28px;
  }
}

.data-table {
  border-radius: 8px;
  border: 1px solid #ebeef5;

  :deep(.el-card__body) {
    padding: 0;
  }
}

.flex-range-inputs {
  display: flex;
  align-items: center;
  gap: 8px;

  .range-separator {
    color: #909399;
    flex-shrink: 0;
  }

  :deep(.el-input-number) {
    flex: 1;

    .el-input__wrapper {
      padding-left: 8px;
      padding-right: 8px;
    }
  }
}

.form-actions {
  padding-top: 12px;
  border-top: 1px solid #f0f2f5;
  margin-top: 16px;

  .action-btn {
    padding: 10px 24px;
    border-radius: 6px;
    transition: all 0.2s;

    &.reset-btn {
      border-color: #dcdfe6;
      &:hover {
        color: var(--el-color-primary);
        border-color: var(--el-color-primary-light-5);
        background-color: var(--el-color-primary-light-9);
      }
    }
  }
}

.pagination {
  padding: 20px;
}

:deep(.table-header) {
  th {
    background-color: #f8f9fc !important;
    color: #606266;
    font-weight: 600;
    height: 52px;
  }
}

.full-width-select {
  width: 100%;
}

.el-tag {
  font-weight: 500;
  padding: 0 10px;
  border-width: 0;

  &--success {
    background-color: var(--el-color-success-light-9);
    color: var(--el-color-success);
  }

  &--danger {
    background-color: var(--el-color-danger-light-9);
    color: var(--el-color-danger);
  }
}

.el-icon {
  font-size: 18px;
}
</style>