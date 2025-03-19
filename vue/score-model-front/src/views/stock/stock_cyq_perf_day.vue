<template xmlns="http://www.w3.org/1999/html">
  <div class="stock-cost-page">
    <!-- 头部 -->
    <div class="header">
      <h2 class="page-title">股票成本分布数据</h2>
    </div>

    <div>
      <el-form-item label="股票代码">
        <el-input v-model="queryParams.tsCode" placeholder="请输入股票代码" clearable />
      </el-form-item>
      <el-form-item label="交易日期">
        <el-date-picker
            v-model="queryParams.tradeDate"
            type="date"
            value-format="YYYYMMDD"
            placeholder="选择日期"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
      </el-form-item>
    </div>

    <!-- 数据表格 -->
    <div class="data-container" v-if="!loading && !error">
      <div class="scroll-wrapper">
        <table class="stock-table">
          <thead>
          <tr>
            <th class="col-code">股票代码</th>
            <th class="col-date">交易日期</th>
            <th class="col-number">历史最低价</th>
            <th class="col-number">历史最高价</th>
            <th class="col-number">5分位成本</th>
            <th class="col-number">15分位成本</th>
            <th class="col-number">50分位成本</th>
            <th class="col-number">85分位成本</th>
            <th class="col-number">95分位成本</th>
            <th class="col-number">加权平均成本</th>
            <th class="col-number">胜率</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(item, index) in list" :key="index">
            <td class="col-code">{{ item.tsCode }}</td>
            <td class="col-date">{{ item.tradeDate }}</td>
            <td class="col-number">{{ item.hisLow }}</td>
            <td class="col-number">{{ item.hisHigh }}</td>
            <td class="col-number">{{ item.cost5pct }}</td>
            <td class="col-number">{{ item.cost15pct }}</td>
            <td class="col-number">{{ item.cost50pct }}</td>
            <td class="col-number">{{ item.cost85pct }}</td>
            <td class="col-number">{{ item.cost95pct }}</td>
            <td class="col-number">{{ item.weightAvg }}</td>
            <td class="col-number">{{ item.winnerRate }}</td>

          </tr>
          </tbody>
        </table>
        <!-- 在表格下方添加分页组件 -->
        <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pagination.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            class="pagination-container"
        />
        <div v-if="list.length === 0" class="no-data">无数据</div>
      </div>
    </div>
  </div>
</template>

<script>
import { fetchStockCyqPerfCurrentDate, fetchStockCyqPerfCurrentDatePage } from '@/api/tableData.js';
import {reactive} from "vue";

export default {
  data() {
    return {
      // 新增分页参数
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      list: [], // 原始数据
      // 移除 filteredList，改用分页接口过滤
      filterParams: {
        tsCode: ''
      }, // 过滤后的数据
      loading: true, // 加载状态
      error: false, // 错误状态
      filterCode: '', // 股票代码过滤条件
      sortDirection: 'asc', // 排序方向：asc/desc
      queryParams: {
        tsCode: '',
        tradeDate: ''
      },
    };
  },
  created() {
    this.fetchData();
  },
  computed: {
    // 计算胜率最高的 Top 10
    topWinners() {
      return this.list.slice(0, 10)
    },
  },
  methods: {
    async fetchData() {
      try {
        const params = {
          ...this.queryParams,
          page: this.pagination.current,
          size: this.pagination.size
        };

        const res = await fetchStockCyqPerfCurrentDatePage(params);
        this.list = res.records;
        this.pagination.total = res.total;

      } catch (error) {
        console.error('数据获取失败:', error);
        this.error = true;
      } finally {
        this.loading = false;
      }
    },

    // 分页事件处理
    handleSizeChange(newSize) {
      this.pagination.size = newSize;
      this.fetchData();
    },

    handleCurrentChange(newPage) {
      this.pagination.current = newPage;
      this.fetchData();
    },

    // 查询条件处理
    handleSearch() {
      this.pagination.current = 1
      this.fetchData()
    },
  }
};
</script>

<style scoped>
/* 基础布局 */
.stock-cost-page {
  padding: 24px;
  max-width: 90%;
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

/* 胜率最高的 Top 10 容器 */
.top-winner-container {
  margin-bottom: 24px;
  padding: 16px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.top-winner-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 12px 0;
  color: #333;
}

.top-winner-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.winner-item {
  padding: 8px 12px;
  background-color: #e8f0fe;
  border-radius: 4px;
  color: #1a73e8;
  cursor: pointer;
  transition: background-color 0.2s;
}

.winner-item:hover {
  background-color: #d2e3fc;
}

/* 过滤和排序容器 */
.filter-container {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
}

.filter-input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.sort-button {
  padding: 8px 16px;
  background-color: #6a11cb;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s;
}

.sort-button:hover {
  background-color: #2575fc;
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
  text-align: center; /* 所有内容居中对齐 */
}

.stock-table th {
  background-color: #6a11cb;
  color: white;
  font-weight: 600;
  padding: 12px 16px;
  border-bottom: 2px solid #e8e8e8;
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
.col-code { width: 120px; }
.col-date { width: 120px; }
.col-number { width: 100px; }

/* 无数据提示 */
.no-data {
  padding: 24px;
  text-align: center;
  color: #999;
  font-size: 14px;
}

.pagination-container {
  margin-top: 20px;
  padding: 16px;
  background: white;
  border-radius: 0 0 12px 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header {
    padding: 12px;
  }

  .page-title {
    font-size: 20px;
  }

  .filter-container {
    flex-direction: column;
  }

  .stock-table th,
  .stock-table td {
    padding: 10px 12px;
    font-size: 12px;
  }
}
</style>