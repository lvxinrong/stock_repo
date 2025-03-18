<template>
  <div class="limit-up-page">
    <!-- 头部 -->
    <div class="header">
      <h2 class="page-title">📈 DeepSeek分析结果</h2>
      <div class="trade-date">交易日：{{ latestTradeDate }}</div>
    </div>

    <!-- 数据表格 -->
    <div class="data-container" v-if="!loading && !error">
      <div class="scroll-wrapper">
        <table class="stock-table">
          <thead>
          <tr>
            <th class="col-code">代码</th>
            <th class="col-date">交易日期</th>
            <th class="col-name">DeepSeek 分析结果</th>
            <th
                class="col-price sortable"
                @click="handleSort"
                :class="sortDirection"
            >
              DeepSeek 评分
              <span class="sort-arrow" v-if="sortBy === 'score'">
                {{ sortDirection === 'asc' ? '↑' : '↓' }}
              </span>
            </th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(item, index) in sortedList" :key="index">
            <td class="col-code">{{ item.tsCode }}</td>
            <td class="col-date">{{ item.tradeDate }}</td>
            <td class="col-name">{{ item.analyzeResult }}</td>
            <td class="col-price">{{ item.score }}</td>
          </tr>
          </tbody>
        </table>
        <div v-if="list.length === 0" class="no-data">当日无涨停数据</div>
      </div>
    </div>
  </div>
</template>

<script>
import { fetchLimitUpDeepSeekAnalyzeDate } from '@/api/tableData.js'

export default {
  data() {
    return {
      list: [],
      loading: true,
      error: false,
      latestTradeDate: '',
      sortBy: 'score',        // 当前排序字段
      sortDirection: 'desc',   // 排序方向：asc/desc
    }
  },
  created() {
    this.fetchData()
  },
  computed: {
    // 排序后的列表
    sortedList() {
      return [...this.list].sort((a, b) => {
        const valueA = parseFloat(a[this.sortBy]) || 0
        const valueB = parseFloat(b[this.sortBy]) || 0
        return this.sortDirection === 'asc'
            ? valueA - valueB
            : valueB - valueA
      })
    }
  },
  methods: {
    async fetchData() {
      try {
        this.list = await fetchLimitUpDeepSeekAnalyzeDate()
        if (this.list.length > 0) {
          this.latestTradeDate = this.list[0].tradeDate
        }
      } catch (error) {
        console.error('数据获取失败:', error)
        this.error = true
      } finally {
        this.loading = false
      }
    },
    // 处理排序
    handleSort() {
      if (this.sortBy === 'score') {
        // 如果已经是当前排序字段，切换方向
        this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc'
      } else {
        // 如果是新排序字段，默认降序
        this.sortBy = 'score'
        this.sortDirection = 'desc'
      }
    }
  }
}
</script>

<style scoped>
/* 新增排序相关样式 */
.sortable {
  cursor: pointer;
  user-select: none;
  transition: background 0.2s;
}

.sortable:hover {
  background-color: #f0f0f0;
}

.sort-arrow {
  margin-left: 8px;
  font-size: 12px;
  color: #666;
}

/* 基础布局 */
.limit-up-page {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
  background-color: #f9fafb;
  min-height: 100vh;
}

/* 头部样式 */
.header {
  margin-bottom: 24px;
  padding: 16px;
  background: linear-gradient(135deg, #6a11cb, #2575fc);
  border-radius: 12px;
  color: white;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  margin: 0;
}

.trade-date {
  font-size: 14px;
  margin-top: 8px;
  opacity: 0.9;
}

/* 数据容器 */
.data-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.scroll-wrapper {
  overflow-x: auto;
  padding: 16px;
}

/* 表格样式 */
.stock-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.stock-table th {
  background-color: #f8f9fa;
  color: #555;
  font-weight: 600;
  padding: 12px 16px;
  border-bottom: 2px solid #e8e8e8;
  text-align: left;
}

.stock-table td {
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
  color: #333;
}

.stock-table tbody tr:hover {
  background-color: #f5f7fa;
}

/* 列宽优化 */
.col-code { width: 100px; }
.col-date { width: 120px; }
.col-name { min-width: 300px; }
.col-price { width: 100px; }

/* 无数据提示 */
.no-data {
  padding: 24px;
  text-align: center;
  color: #999;
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header {
    padding: 12px;
  }

  .page-title {
    font-size: 20px;
  }

  .trade-date {
    font-size: 12px;
  }

  .stock-table th,
  .stock-table td {
    padding: 10px 12px;
    font-size: 12px;
  }

  .col-name {
    min-width: 200px;
  }
}
</style>