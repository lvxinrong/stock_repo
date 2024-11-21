<template>
  <div>
    <!-- 解释沪深300的概念 -->
    <el-card class="concept-card">
      <h2>沪深300简介</h2>
      <p>
        沪深300指数是由上海和深圳证券市场中选取300只规模大、流动性好的股票组成的成份股指数，
        反映了中国A股市场的整体走势。它是国内市场最具代表性的指数之一，常用于投资业绩的基准。
      </p>
    </el-card>

    <!-- 表格部分 -->
    <el-card class="table-card">
      <el-table
          :data="tableData"
          style="width: 100%"
          stripe
          highlight-current-row
          :row-class-name="tableRowClassName"
      >
        <el-table-column label="股票代码">
          <template #default="{ row }">
            <span
                class="highlight-link"
                @click="navigateToDetail(row.ts_code)"
            >
              {{ row.ts_code }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="stock_name" label="股票名称"></el-table-column>
        <el-table-column prop="tradeDateMonth" label="交易日期"></el-table-column>
        <el-table-column prop="score" label="股票得分"></el-table-column>
        <el-table-column prop="tradeDateYieldRate" label="计算月涨幅"></el-table-column>
        <el-table-column prop="yieldRate" label="本月收益率(本月1号到最新数据的收益率)"></el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
      <el-pagination
          background
          layout="sizes, prev, pager, next"
          :total="total"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20, 50]"
          @current-change="fetchData"
          @size-change="handleSizeChange"
      />
      </div>
    </el-card>
  </div>
</template>

<script>
import { fetchHuShen300Data } from "@/api/tableData";
import { goToDetail } from "@/common_js/navigation.js"; // 引入公共方法

export default {
  data() {
    return {
      tableData: [], // 表格数据
      total: 0, // 总条数
      pageSize: 10, // 每页条数（默认值）
      currentPage: 1, // 当前页码
    };
  },
  mounted() {
    this.fetchData(this.currentPage);
  },
  methods: {
    async fetchData(page = 1) { // 默认值为 1，防止未传参时报错
      try {
        console.log("fetchData 当前页:", page);
        this.currentPage = page;
        const response = await fetchHuShen300Data(
          this.currentPage,
          this.pageSize
        );
        if (response) {
          this.tableData = response.items;
          this.total = response.total;
        }
      } catch (error) {
        console.error("数据加载失败:", error);
      }
    },
    handleSizeChange(newSize) {
      console.log("每页条数更新为:", newSize);
      this.pageSize = newSize;
      this.fetchData(1); // 重置到第一页
    },
    navigateToDetail(ts_code) {
      goToDetail(this.$router, ts_code)
    },
    tableRowClassName({ rowIndex }) {
      return rowIndex % 2 === 0 ? "table-row-light" : "table-row-dark";
    },
  },
};
</script>

<style scoped>
.concept-card {
  margin-bottom: 20px;
}

.table-card {
  padding: 20px;
}

/* 表格样式增强 */
.el-table th {
  background-color: #f5f7fa; /* 表头背景色 */
  color: #606266; /* 表头文字颜色 */
  font-weight: bold; /* 表头加粗 */
  text-align: center; /* 表头文字居中 */
}
.el-table td {
  text-align: left; /* 单元格默认左对齐 */
}

/* 分页栏间距调整 */
.pagination-container {
  margin-top: 20px; /* 调整与表格的距离 */
  text-align: center; /* 居中分页栏 */
}

/* 交替行颜色：增强对比度 */
:deep(.table-row-light) {
  background-color: #ffffff !important; /* 浅白色 */
}
:deep(.table-row-dark) {
  background-color: #f2f2f2 !important; /* 更深的灰色 */
}
/* 当前行高亮效果 */
:deep(.el-table__row:hover) {
  background-color: #d0ebff !important; /* 悬停行高亮，浅蓝色 */
}
</style>
