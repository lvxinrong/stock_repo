<template>
  <aside class="sidebar">
    <ul class="menu-group">
      <!-- 指数行情 -->
      <li class="menu-category">
        <h3 class="category-title">📈 指数行情</h3>
        <ul class="sub-menu">
          <li><router-link to="/hushen300" class="menu-item">沪深300</router-link></li>
          <li><router-link to="/zh100" class="menu-item">中证100</router-link></li>
          <li><router-link to="/zh500" class="menu-item">中证500</router-link></li>
          <li><router-link to="/zh800" class="menu-item">中证800</router-link></li>
          <li><router-link to="/zh1000" class="menu-item">中证1000</router-link></li>
        </ul>
      </li>

      <!-- 涨跌停行情 -->
      <li class="menu-category">
        <h3 class="category-title">🚀 涨跌停行情</h3>
        <ul class="sub-menu">
          <li>
            <div class="menu-item" @click="toggleLimitUpSubMenu">
              当日涨停
              <span class="arrow" :class="{ 'rotate': isLimitUpSubMenuVisible }">▼</span>
            </div>
            <!-- 三级目录 -->
            <ul class="sub-sub-menu" v-if="isLimitUpSubMenuVisible">
              <li><router-link to="/limit-up-day" class="menu-item">当日涨停列表</router-link></li>
              <li><router-link to="/limit-up-day/deepseek-result" class="menu-item">Deep Seek分析结果</router-link></li>
            </ul>
          </li>
          <li><router-link to="/limit-down-day" class="menu-item">当日跌停列表</router-link></li>
          <li><router-link to="/limit-statistics" class="menu-item">涨跌停统计</router-link></li>
        </ul>
      </li>

      <!-- 新增的资金流向目录 -->
      <li class="menu-category">
        <h3 class="category-title">💰 资金流向</h3>
        <ul class="sub-menu">
          <li><router-link to="/money-flow/mkt-money-flow-latest" class="menu-item">近90日大盘资金流向</router-link></li>
          <li><router-link to="/money-flow/ind_money_flow_ths_day_vue" class="menu-item">同花顺板块资金流向</router-link></li>
          <li><router-link to="/money-flow/ind_money_flow_dc_day_vue" class="menu-item">东方财富板块资金流向</router-link></li>
        </ul>
      </li>

      <!-- 新增的筹码结构 -->
      <li class="menu-category">
        <h3 class="category-title">🧩 筹码结构</h3>
        <ul class="sub-menu">
          <li><router-link to="/stock_cyq/stock_cyq_perf_day" class="menu-item">每日筹码分布</router-link></li>
        </ul>
      </li>

      <li class="menu-category">
        <h3 class="category-title">📊 技术指标</h3>
        <ul class="sub-menu">
          <li><router-link to="/technical-indicator/daily" class="menu-item">每日技术指标</router-link></li>
        </ul>
      </li>

      <!-- 新增策略选股目录 -->
      <li class="menu-category">
        <h3 class="category-title">🎰 策略选股</h3>
        <ul class="sub-menu">
          <li><router-link to="/strategy/macd-20" class="menu-item">MACD20日技术指标</router-link></li>
        </ul>
      </li>

    </ul>
  </aside>
</template>

<script>
export default {
  data() {
    return {
      isLimitUpSubMenuVisible: false, // 控制三级目录的显示与隐藏
    };
  },
  methods: {
    // 切换三级目录的显示状态
    toggleLimitUpSubMenu() {
      this.isLimitUpSubMenuVisible = !this.isLimitUpSubMenuVisible;
    },
  },
};
</script>

<style>
.sidebar {
  background-color: #f8f9fa;
  padding: 20px;
  min-width: 240px;
}

.menu-category {
  margin-bottom: 25px;
  border-left: 3px solid #2c3e50;
  padding-left: 15px;
}

.category-title {
  color: #2c3e50;
  font-size: 1.1em;
  font-weight: 600;
  margin: 0 0 12px 0;
  padding: 5px 0;
}

.sub-menu {
  list-style: none;
  padding-left: 15px;
  margin: 0;
}

.menu-item {
  display: block;
  padding: 8px 12px;
  color: #34495e;
  text-decoration: none;
  border-radius: 4px;
  transition: all 0.2s;
  font-size: 0.95em;
}

.menu-item:hover {
  background-color: #e9ecef;
  transform: translateX(5px);
}

.menu-item.router-link-exact-active {
  background-color: #3498db;
  color: white;
  font-weight: 500;
}

/* 基础样式 */
.sidebar {
  background-color: #f8f9fa;
  padding: 20px;
  min-width: 240px;
}

.menu-group {
  list-style: none;
  padding: 0;
}

.menu-category {
  margin-bottom: 25px;
  border-left: 3px solid #2c3e50;
  padding-left: 15px;
}

.category-title {
  color: #2c3e50;
  font-size: 1.1em;
  font-weight: 600;
  margin: 0 0 12px 0;
  padding: 5px 0;
}

.sub-menu {
  list-style: none;
  padding-left: 15px;
  margin: 0;
}

.sub-sub-menu {
  list-style: none;
  padding-left: 20px;
  margin: 10px 0;
}

.menu-item {
  display: block;
  padding: 8px 12px;
  color: #34495e;
  text-decoration: none;
  border-radius: 4px;
  transition: all 0.2s;
  cursor: pointer;
}

.menu-item:hover {
  background-color: #e9ecef;
  transform: translateX(5px);
}

.arrow {
  display: inline-block;
  margin-left: 8px;
  transition: transform 0.2s;
}

.arrow.rotate {
  transform: rotate(180deg);
}

/* 路由激活样式 */
.router-link-active {
  background-color: #3498db;
  color: white;
  font-weight: 500;
}
</style>


<script setup>
</script>