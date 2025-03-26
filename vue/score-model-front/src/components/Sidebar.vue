<template>
  <el-menu
    class="custom-sidebar"
    :collapse="isCollapse"
    :default-active="activeIndex"
  >
    <div class="logo-container">
      <img src="@/assets/logo.png" alt="Logo" class="logo" />
      <span v-if="!isCollapse" class="title">股票数据</span>
    </div>
    
    <!-- 指数行情 -->
    <el-sub-menu index="1">
      <template #title>
        <div class="menu-title">
          <span class="icon">📈</span>
          <span class="text">指数行情</span>
        </div>
      </template>
      <el-menu-item index="1-1">
        <router-link to="/hushen300">沪深300</router-link>
      </el-menu-item>
      <el-menu-item index="1-2">
        <router-link to="/zh100">中证100</router-link>
      </el-menu-item>
      <el-menu-item index="1-3">
        <router-link to="/zh500">中证500</router-link>
      </el-menu-item>
      <el-menu-item index="1-4">
        <router-link to="/zh800">中证800</router-link>
      </el-menu-item>
      <el-menu-item index="1-5">
        <router-link to="/zh1000">中证1000</router-link>
      </el-menu-item>
    </el-sub-menu>

    <!-- 涨跌停行情 -->
    <el-sub-menu index="2">
      <template #title>
        <div class="menu-title">
          <span class="icon">🚀</span>
          <span class="text">涨跌停行情</span>
        </div>
      </template>
      <el-menu-item index="2-1">
        <router-link to="/limit-up-day">当日涨停</router-link>
      </el-menu-item>
      <el-menu-item index="2-1-1-2">
        <router-link to="/limit-up-day/deepseek-result">Deep Seek分析结果</router-link>
      </el-menu-item>
      <el-menu-item index="2-2">
        <router-link to="/limit-down-day">当日跌停列表</router-link>
      </el-menu-item>
      <el-menu-item index="2-3">
        <router-link to="/limit-statistics">涨跌停统计</router-link>
      </el-menu-item>
    </el-sub-menu>

    <!-- 新增的资金流向目录 -->
    <el-sub-menu index="3">
      <template #title>💰 资金流向</template>
      <el-menu-item index="3-1"><router-link to="/money-flow/mkt-money-flow-latest" class="menu-item">近90日大盘资金流向</router-link></el-menu-item>
      <el-menu-item index="3-2"><router-link to="/money-flow/ind_money_flow_ths_day_vue" class="menu-item">同花顺板块资金流向</router-link></el-menu-item>
      <el-menu-item index="3-3"><router-link to="/money-flow/ind_money_flow_dc_day_vue" class="menu-item">东方财富板块资金流向</router-link></el-menu-item>
    </el-sub-menu>

    <!-- 新增的筹码结构 -->
    <el-sub-menu index="4">
      <template #title>🧩 筹码结构</template>
      <el-menu-item index="4-1"><router-link to="/stock_cyq/stock_cyq_perf_day" class="menu-item">每日筹码分布</router-link></el-menu-item>
      <el-menu-item index="4-2"><router-link to="/stock_cyq/stock_cyq_converge" class="menu-item">筹码集中度分析</router-link></el-menu-item>

    </el-sub-menu>

    <el-sub-menu index="5">
      <template #title>📊 技术指标</template>
      <el-menu-item index="5-1"><router-link to="/technical-indicator/daily" class="menu-item">每日技术指标</router-link></el-menu-item>
    </el-sub-menu>

    <!-- 新增策略选股目录 -->
    <el-sub-menu index="6">
      <template #title>🎰 策略选股</template>
      <el-menu-item index="6-1"><router-link to="/strategy/macd-20" class="menu-item">MACD20日技术指标</router-link></el-menu-item>
    </el-sub-menu>
  </el-menu>
</template>

<script>
export default {
  data() {
    return {
      isCollapse: false,
      activeIndex: '1-1'
    };
  },
};
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.custom-sidebar {
  height: 100vh;
  border-right: none;
  box-shadow: $box-shadow-light;
  background: $background-dark;
  
  .logo-container {
    height: 64px;
    display: flex;
    align-items: center;
    padding: $spacing-md $spacing-lg;
    background: lighten($background-dark, 5%);
    margin-bottom: $spacing-md;
    
    .logo {
      width: 32px;
      height: 32px;
      margin-right: $spacing-md;
    }
    
    .title {
      font-size: 20px;
      font-weight: 600;
      color: white;
      letter-spacing: 1px;
    }
  }

  :deep(.el-menu) {
    border-right: none;
    background: transparent;
  }

  :deep(.el-sub-menu) {
    .el-sub-menu__title {
      color: #ffffff90;
      height: 50px;
      line-height: 50px;
      padding: 0 $spacing-lg;
      
      &:hover {
        background-color: lighten($background-dark, 10%);
      }
    }
  }

  :deep(.el-menu-item) {
    height: 40px;
    line-height: 40px;
    padding: 0 $spacing-lg 0 48px !important;
    color: #ffffff80;
    
    &:hover {
      background-color: lighten($background-dark, 10%);
    }
    
    &.is-active {
      background-color: $secondary-color;
      color: white;
      
      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 4px;
        height: 16px;
        background: white;
        border-radius: 0 2px 2px 0;
      }
    }

    a {
      color: inherit;
      text-decoration: none;
      display: block;
      width: 100%;
      height: 100%;
    }
  }

  .menu-title {
    display: flex;
    align-items: center;
    gap: $spacing-sm;

    .icon {
      font-size: 18px;
      width: 24px;
      display: inline-flex;
      justify-content: center;
    }

    .text {
      font-weight: 500;
    }
  }
}

// 折叠时的样式
:deep(.el-menu--collapse) {
  .menu-title {
    .text {
      display: none;
    }
  }
}

// 弹出的子菜单样式
:deep(.el-menu--popup) {
  background: $background-dark;
  padding: $spacing-xs 0;
  min-width: 180px;
  border-radius: $border-radius-sm;
  
  .el-menu-item {
    color: #ffffff80;
    height: 40px;
    line-height: 40px;
    
    &:hover {
      background-color: lighten($background-dark, 10%);
    }
    
    &.is-active {
      background-color: $secondary-color;
      color: white;
    }
  }
}
</style>

<script setup>
</script>