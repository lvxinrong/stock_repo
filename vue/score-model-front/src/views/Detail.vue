<template>
  <div class="detail-page">
    <el-card>
      <h2>股票月度交易详情</h2>
      <p>股票 代码: {{ ts_code }}</p>
    </el-card>

    <el-card class="list-card">
      <h3>相关数据列表</h3>
      <el-table
          :data="listData"
          style="width: 100%"
          stripe
          highlight-current-row
          :row-class-name="tableRowClassName"
      >
        <el-table-column prop="tradeDate" label="交易日期"></el-table-column>
        <el-table-column prop="open" label="开盘价"></el-table-column>
        <el-table-column prop="high" label="最高价"></el-table-column>
        <el-table-column prop="close" label="收盘价"></el-table-column>
        <el-table-column prop="preClose" label="昨收价【除权价，前复权】"></el-table-column>
        <el-table-column prop="changeValue" label="涨跌额"></el-table-column>
        <el-table-column prop="pctChg" label="涨跌幅"></el-table-column>
        <el-table-column prop="vol" label="成交量(手)"></el-table-column>
        <el-table-column prop="amount" label="成交额(千元)"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { fetchDetailData } from "@/api/tableData";

export default {
  data() {
    return {
      ts_code: null, // 路由参数ID
      listData: [], // 列表数据
    };
  },
  async mounted() {
    // 获取路由参数
    this.ts_code = this.$route.params.ts_code;

    // 调用后端接口获取数据
    await this.loadData();
  },
  methods: {
    async loadData() {
      try {
        const response = await fetchDetailData(this.ts_code);
        if (response) {
          this.listData = response; // 假设后端返回的数据结构为 { data: [...] }
        }
      } catch (error) {
        console.error("获取详情数据失败:", error);
      }
    },
    tableRowClassName({ rowIndex }) {
      return rowIndex % 2 === 0 ? "table-row-light" : "table-row-dark";
    },
  },
};
</script>

<style scoped>
.detail-page {
  padding: 20px;
}

.list-card {
  margin-top: 20px;
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
