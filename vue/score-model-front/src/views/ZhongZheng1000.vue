<template>
  <div>
    <!-- 解释沪深300的概念 -->
    <el-card class="concept-card">
      <h2>中证1000简介</h2>
      <p>
        中证1000指数是由中证指数有限公司编制，其成份股是选择中证800指数样本股之外规模偏小且流动性好的1000只股票组成，与沪深300和中证500等指数形成互补。中证1000指数成份股的平均市值及市值中位数较中小板指、创业板指、中证500指数相比都更小，更能综合反映沪深证券市场内小市值公司的整体状况。
      </p>
    </el-card>

    <!-- 过滤器容器 -->
    <div class="filter-container">
      <span class="filter-label">选择日期：</span>
      <el-select v-model="selectedMonth" placeholder="请选择" @change="handleMonthChange" class="filter-select">
        <el-option
            v-for="item in monthOptions"
            :key="item"
            :label="item"
            :value="item"
        ></el-option>
      </el-select>
    </div>
    <!-- 表格部分 -->
    <el-card class="table-card">
      <el-table :data="tableData" style="width: 100%">
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
      <el-pagination
          background
          layout="sizes, prev, pager, next"
          :total="total"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20, 50]"
          @current-change="fetchData"
          @size-change="handleSizeChange"
      />
    </el-card>
  </div>
</template>

<script>
import { fetchZh1000Data } from "@/api/tableData";
import {goToDetail} from "@/common_js/navigation.js";
import {defaultMonthOptions} from "@/api/dateUtils.js";

export default {
  data() {
    return {
      monthOptions: defaultMonthOptions, // 下拉框选项
      selectedMonth: defaultMonthOptions[0], // 当前选择的年月
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
        const response = await fetchZh1000Data(
            this.currentPage,
            this.pageSize,
            this.selectedMonth
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
    handleMonthChange() {
      this.fetchData(1); // 重新加载数据
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

/* 过滤器样式 */
.filter-container {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.filter-label {
  font-size: 14px;
  color: #606266;
  margin-right: 10px; /* 文字与下拉框之间的间距 */
}

.filter-select {
  width: 200px; /* 限制下拉框宽度 */
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
