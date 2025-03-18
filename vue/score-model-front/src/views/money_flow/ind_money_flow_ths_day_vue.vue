<template>
  <div class="industry-page">
    <!-- 头部 -->
    <div class="header">
      <div class="title-container">
        <h2 class="page-title">同花顺行业资金流向</h2>
        <div class="trade-date">
          <i class="icon-calendar"></i>
          <span>{{ latestTradeDate }}</span>
        </div>
      </div>
    </div>

    <!-- 筛选区域 -->
    <div class="filter-card">
      <div class="filter-content">
        <label class="filter-label">
          <i class="icon-filter"></i>
          交易日期筛选：
        </label>
        <div class="date-picker-wrapper">
          <input
              type="date"
              v-model="searchDate"
              @change="filterTable"
              class="modern-date-picker"
              :max="getCurrentDateFormatted()"
          />
          <i class="icon-chevron-down"></i>
        </div>
      </div>
    </div>

    <!-- 新增的三个展示窗口 -->
    <div class="stats-container">
      <!-- 涨幅最大板块 -->
      <div class="stat-card">
        <div class="stat-header">
          <i class="icon-trending-up"></i>
          <h3>涨幅TOP3板块</h3>
        </div>
        <div v-if="topRiseIndustry.length > 0" class="stat-content">
          <div v-for="(item, index) in topRiseIndustry" :key="index" class="industry-item">
            <span class="rank">{{ index + 1 }}</span>
            <span class="name">{{ item.industry }}</span>
            <span class="value positive">当日涨幅: {{formatCurrency(item.pctChange) }}%</span>
          </div>
        </div>
        <div v-else class="no-data">无数据</div>
      </div>

      <!-- 资金流入最大板块 -->
      <div class="stat-card">
        <div class="stat-header">
          <i class="icon-cash-in"></i>
          <h3>资金流入TOP3</h3>
        </div>
        <div v-if="topInflowIndustries.length > 0" class="stat-content">
          <div v-for="(item, index) in topInflowIndustries" :key="index" class="industry-item">
            <span class="rank">{{ index + 1 }}</span>
            <span class="name">{{ item.industry }}</span>
            <span class="value positive">资金净流入: {{formatCurrency(item.netAmount) }}(亿元)</span>
          </div>
        </div>
        <div v-else class="no-data">无数据</div>
      </div>

      <!-- 资金流出最大板块 -->
      <div class="stat-card">
        <div class="stat-header">
          <i class="icon-cash-out"></i>
          <h3>资金流出TOP3</h3>
        </div>
        <div v-if="topOutflowIndustries.length > 0" class="stat-content">
          <div v-for="(item, index) in topOutflowIndustries" :key="index" class="industry-item">
            <span class="rank">{{ index + 1 }}</span>
            <span class="name">{{ item.industry }}</span>
            <span class="value negative">资金净流出: {{ formatCurrency(item.netAmount) }}(亿元)</span>
          </div>
        </div>
        <div v-else class="no-data">无数据</div>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="data-card" v-if="!loading && !error">
      <div v-if="filteredList.length > 0" class="table-responsive">
        <table class="modern-table">
          <thead>
          <tr>
            <th v-for="(col, index) in columns" :key="index" :class="col.class">
              {{ col.title }}
            </th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(item, index) in filteredList" :key="index">
            <td>{{ item.tradeDate }}</td>
            <td class="code-cell">{{ item.tsCode }}</td>
            <td class="industry-cell">{{ item.industry }}</td>
            <td class="stock-cell">
              <span class="stock-tag">{{ item.leadStock }}</span>
            </td>
            <td>{{ item.close }}</td>
            <td :class="netAmountClass(item.pctChange)">
              {{ item.pctChange }}%
            </td>
            <td>{{ item.companyNum }}</td>
            <td>{{ item.pctChangeStock }}</td>
            <td>{{ item.closePrice }}</td>
            <td class="positive">{{ item.netBuyAmount }}</td>
            <td class="negative">{{ item.netSellAmount }}</td>
            <td :class="netAmountClass(item.netAmount)">
              {{ item.netAmount }}
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <div v-else class="empty-state">
        <i class="icon-data-empty"></i>
        <p>当日无板块数据</p>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-overlay">
      <div class="spinner"></div>
    </div>

    <!-- 错误状态 -->
    <div v-if="error" class="error-alert">
      <i class="icon-error"></i>
      <p>数据加载失败，请稍后重试</p>
    </div>
  </div>
</template>

<script>
import { fetchIndMoneyFlowThsDayDate } from '@/api/tableData.js';
import { getCurrentDateFormatted } from'@/api/dateUtils.js'
export default {
  data() {
    return {
      columns: [
        { title: '日期', class: 'col-date' },
        { title: '板块代码', class: 'col-code' },
        { title: '板块名称', class: 'col-name' },
        { title: '领涨股', class: 'col-lead' },
        { title: '收盘指数', class: 'col-index' },
        { title: '指数涨跌', class: 'col-pct' },
        { title: '公司数', class: 'col-company' },
        { title: '领涨股涨跌', class: 'col-stock-pct' },
        { title: '最新价', class: 'col-price' },
        { title: '流入(亿)', class: 'col-in' },
        { title: '流出(亿)', class: 'col-out' },
        { title: '净额(亿)', class: 'col-net' }
      ],
      list: [], // 原始数据
      filteredList: [], // 过滤后的数据
      searchDate: '', // 搜索日期
      loading: true,
      error: false,
      latestTradeDate: '', // 最新交易日
      topRiseIndustry: [], // 涨幅最大板块
      topInflowIndustries: [], // 资金流入TOP3
      topOutflowIndustries: [] // 资金流出TOP3
    };
  },
  created() {
    this.fetchData().then(() => {
      this.filteredList = this.list; // 初始化时显示全部数据
    });
  },
  computed: {
    // 统计计算属性
    totalCount() {
      return this.list.length;
    },
    // 计算涨幅最大板块
    topRiseIndustry() {
      if (this.list.length === 0) return [];
      return this.list
          .slice()
          .sort((a, b) => parseFloat(b.pctChange) - parseFloat(a.pctChange))
          .slice(0, 3);
    },
    // 计算资金流入TOP3
    topInflowIndustries() {
      if (this.list.length === 0) return [];
      return this.list
          .slice()
          .sort((a, b) => parseFloat(b.netBuyAmount) - parseFloat(a.netBuyAmount))
          .slice(0, 3);
    },
    // 计算资金流出TOP3
    topOutflowIndustries() {
      if (this.list.length === 0) return [];
      return this.list
          .slice()
          .sort((a, b) => parseFloat(b.netSellAmount) - parseFloat(a.netSellAmount))
          .slice(0, 3);
    }
  },
  methods: {
    getCurrentDateFormatted,
    async fetchData() {
      try {
        // 默认加载最新交易日的数据
        this.list = await fetchIndMoneyFlowThsDayDate(getCurrentDateFormatted());
        if (this.list.length > 0) {
          this.latestTradeDate = this.list[0].tradeDate;
          this.searchDate = this.formatDateForInput(this.latestTradeDate); // 设置默认搜索日期
        }
      } catch (error) {
        console.error('数据获取失败:', error);
        this.error = true;
      } finally {
        this.loading = false;
      }
    },
    // 过滤表格数据
    async filterTable() {
      if (this.searchDate) {
        const tradeDate = this.formatDateForApi(this.searchDate); // 将日期格式化为 yyyyMMdd
        try {
          this.loading = true;
          // this.filteredList = await fetchIndMoneyFlowThsDayDate(tradeDate); // 调用接口获取指定日期的数据
          this.list = await fetchIndMoneyFlowThsDayDate(tradeDate);
          this.filteredList = this.list
        } catch (error) {
          console.error('数据获取失败:', error);
          this.error = true;
        } finally {
          this.loading = false;
        }
      } else {
        this.filteredList = this.list; // 如果未选择日期，则显示全部
      }
    },
    // 根据涨跌幅设置样式
    getChangeClass(pctChange) {
      const value = parseFloat(pctChange);
      return {
        rise: value > 0,
        fall: value < 0,
      };
    },
    // 将 yyyy-MM-dd 格式化为 yyyyMMdd
    formatDateForApi(date) {
      return date.replace(/-/g, '');
    },
    // 将 yyyyMMdd 格式化为 yyyy-MM-dd（用于 input[type="date"]）
    formatDateForInput(date) {
      if (!date) return '';
      const year = date.slice(0, 4);
      const month = date.slice(4, 6);
      const day = date.slice(6, 8);
      return `${year}-${month}-${day}`;
    },
    // 新增格式化方法
    formatDate(dateStr) {
      // 实现日期格式化逻辑
    },
    formatNumber(value) {
      // 实现数字格式化
    },
    formatCurrency(value) {
      // 实现货币格式化
      return value;
    },
    netAmountClass(value) {
      return Number(value) >= 0 ? 'positive' : 'negative';
    },
    getChangeIcon(pctChange) {
      const value = parseFloat(pctChange);
      return value > 0 ? 'icon-arrow-up' : value < 0 ? 'icon-arrow-down' : '';
    }
  },
};
</script>

<style scoped>
/* 基础布局 */
.industry-page {
  padding: 24px;
  max-width: 95%;
  margin: 0 auto;
  font-family: 'Segoe UI', system-ui, sans-serif;
}

/* 头部样式 */
.header {
  margin-bottom: 32px;
}

.title-container {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.page-title {
  font-size: 50px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
}

.trade-date {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
  font-size: 30px;
}

/* 筛选卡片 */
.filter-card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  margin-bottom: 24px;
  padding: 16px;
}

.filter-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.date-picker-wrapper {
  position: relative;
  flex: 1;
  max-width: 240px;
}

.modern-date-picker {
  width: 100%;
  padding: 10px 16px;
  border: 1px solid #e1e3e6;
  border-radius: 8px;
  font-size: 14px;
  color: #1a1a1a;
  background: #f8f9fa;
  transition: all 0.2s;
}

.modern-date-picker:hover {
  border-color: #409eff;
  background: #ffffff;
}

/* 数据卡片 */
.data-card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

/* 现代表格样式 */
.modern-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.modern-table th {
  background: #f8f9fa;
  color: #666;
  font-weight: 500;
  padding: 16px;
  border-bottom: 2px solid #e1e3e6;
}

.modern-table td {
  padding: 14px 16px;
  border-bottom: 1px solid #f0f2f5;
}

.modern-table tbody tr:hover td {
  background: #fafbff;
}

/* 特殊样式 */
.positive {
  color: #f70743;
  font-weight: 500;
}

.negative {
  color: #1ce202;
  font-weight: 500;
}

/* 空状态 */
.empty-state {
  padding: 48px 24px;
  text-align: center;
  color: #999;
}

/* 新增样式 */
.stats-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin-bottom: 24px;
}

.stat-card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 20px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.stat-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
}

.stat-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
}

.stat-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.industry-name {
  font-size: 16px;
  font-weight: 500;
  color: #1a1a1a;
}

.industry-value {
  font-size: 24px;
  font-weight: 600;
}

.industry-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 0;
}

.industry-item .rank {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
  border-radius: 50%;
  font-size: 12px;
  color: #666;
}

.industry-item .name {
  flex: 1;
  font-size: 14px;
  color: #1a1a1a;
}

.industry-item .value {
  font-size: 14px;
  font-weight: 500;
}

.no-data {
  text-align: center;
  color: #999;
  padding: 16px 0;
}

/* 表格头部固定 */
.modern-table thead th {
  position: sticky;
  top: 0;
  background: #f8f9fa;
  z-index: 1;
}

/* 新增样式 */
.stats-container {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 16px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.stat-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.stat-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
}

.stat-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.industry-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 0;
}

.industry-item .rank {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
  border-radius: 50%;
  font-size: 12px;
  color: #666;
}

.industry-item .name {
  flex: 1;
  font-size: 14px;
  color: #1a1a1a;
}

.industry-item .value {
  font-size: 14px;
  font-weight: 500;
}

.no-data {
  text-align: center;
  color: #999;
  padding: 16px 0;
}

/* 表格头部固定 */
.modern-table thead th {
  position: sticky;
  top: 0;
  background: #f8f9fa;
  z-index: 1;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .stats-container {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-container {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .stats-container {
    grid-template-columns: 1fr;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .stats-container {
    grid-template-columns: 1fr;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .industry-page {
    padding: 16px;
  }

  .filter-content {
    flex-direction: column;
    align-items: stretch;
  }

  .date-picker-wrapper {
    max-width: 100%;
  }
}
</style>