<template>
  <div>
    <!-- 解释沪深300的概念 -->
    <el-card class="concept-card">
      <h2>中证100简介</h2>
      <p>
        中证100指数是从沪深300指数样本股中挑选规模最大的100只股票组成样本股，以综合反映沪深证券市场中最具市场影响力的一批大市值公司的整体状况。
      </p>
    </el-card>

    <!-- 表格部分 -->
    <el-card class="table-card">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="ts_code" label="股票代码" width="80"></el-table-column>
        <el-table-column prop="stock_name" label="股票名称"></el-table-column>
        <el-table-column prop="tradeDateMonth" label="交易日期"></el-table-column>
        <el-table-column prop="score" label="股票得分"></el-table-column>
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
import { fetchZh100Data } from "@/api/tableData";

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
        const response = await fetchZh100Data(
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
</style>
