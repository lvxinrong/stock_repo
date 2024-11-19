<template>
  <div>
    <h1>双创50</h1>
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="value" label="数值"></el-table-column>
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
import axios from 'axios';

export default {
  name: 'HuShen300',
  setup() {
    const tableData = ref([]);
    const currentPage = ref(1);
    const pageSize = ref(10);
    const total = ref(0);

    const fetchData = async (page = 1) => {
      try {
        const response = await axios.get(`/api/hushen300?page=${page}&pageSize=${pageSize.value}`);
        tableData.value = response.data.items;
        total.value = response.data.total;
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
