<template>
  <div class="stock-table-container">
    <el-table
      :data="tableData"
      :border="false"
      class="custom-table"
      @row-click="handleRowClick"
    >
      <!-- ... existing table columns ... -->
    </el-table>
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next"
      />
    </div>
  </div>
</template>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.stock-table-container {
  background: white;
  border-radius: $border-radius-md;
  padding: $spacing-md;
  box-shadow: $box-shadow-light;
  
  .custom-table {
    // 表格样式
    :deep(.el-table__header) {
      background-color: $background-light;
      th {
        background-color: transparent;
        color: $text-primary;
        font-weight: 600;
        padding: $spacing-md;
      }
    }

    :deep(.el-table__row) {
      transition: all 0.3s ease;
      
      &:hover {
        background-color: rgba($secondary-color, 0.05);
        transform: translateY(-2px);
      }

      td {
        padding: $spacing-md;
      }
    }

    // 股票代码列样式
    .stock-code {
      color: $secondary-color;
      font-weight: 500;
      cursor: pointer;
      
      &:hover {
        color: darken($secondary-color, 10%);
      }
    }

    // 涨跌幅样式
    .price-up {
      color: $success-color;
      font-weight: 500;
    }
    
    .price-down {
      color: $danger-color;
      font-weight: 500;
    }
  }

  .pagination-container {
    margin-top: $spacing-lg;
    display: flex;
    justify-content: flex-end;
  }
}
</style> 