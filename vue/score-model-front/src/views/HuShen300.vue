<template>
  <div>
    <h1>沪深300</h1>
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="ts_code" label="股票代码" width="80"></el-table-column>
      <el-table-column prop="stock_name" label="股票名称"></el-table-column>
      <el-table-column prop="tradeDateMonth" label="交易日期"></el-table-column>
      <el-table-column prop="score" label="股票得分"></el-table-column>

    </el-table>
    <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        @current-change="fetchData"
        layout="prev, pager, next, jumper"
    />
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { fetchHuShen300Data } from '@/api/tableData';

export default {
  name: 'HuShen300',
  setup() {
    const tableData = ref([]);
    const currentPage = ref(1);
    const pageSize = ref(10);
    const total = ref(0);

    const fetchData = async (page = 1) => {
      try {
        const response = await fetchHuShen300Data(page, pageSize.value);
        tableData.value = response.items;
        total.value = response.total;
      } catch (error) {
        console.error('Failed to fetch data:', error);
      }
    };

    onMounted(() => {
      fetchData();
    });

    return {
      tableData,
      currentPage,
      pageSize,
      total,
      fetchData,
    };
  },
};
</script>
