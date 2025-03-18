<template>
  <div class="industry-page">
    <!-- 头部 -->
    <div class="header">
      <div class="title-container">
        <h2 class="page-title">板块资金流向详情</h2>
        <div class="trade-date">
          <i class="icon-calendar"></i>
          <span>{{ latestTradeDate }}</span>
        </div>
      </div>
    </div>

    <!-- 新增的 5 个浮动窗口 -->
    <div class="stats-container">
      <!-- 涨幅最大的 5 个板块 -->
      <div class="stat-card">
        <div class="stat-header">
          <i class="icon-trending-up"></i>
          <h3>涨幅最大板块 TOP5</h3>
        </div>
        <div v-if="topRiseIndustries.length > 0" class="stat-content">
          <div v-for="(item, index) in topRiseIndustries" :key="index" class="industry-item">
            <span class="rank">{{ index + 1 }}</span>
            <span class="name">{{ item.name }}</span>
            <span class="value positive">{{ item.pctChange }}%</span>
          </div>
        </div>
        <div v-else class="no-data">无数据</div>
      </div>

      <!-- 涨幅最大的 5 个概念 -->
      <div class="stat-card">
        <div class="stat-header">
          <i class="icon-trending-up"></i>
          <h3>涨幅最大概念 TOP5</h3>
        </div>
        <div v-if="topRiseThemes.length > 0" class="stat-content">
          <div v-for="(item, index) in topRiseThemes" :key="index" class="industry-item">
            <span class="rank">{{ index + 1 }}</span>
            <span class="name">{{ item.name }}</span>
            <span class="value positive">{{ item.pctChange }}%</span>
          </div>
        </div>
        <div v-else class="no-data">无数据</div>
      </div>

      <!-- 涨幅最大的 5 个行业 -->
      <div class="stat-card">
        <div class="stat-header">
          <i class="icon-trending-up"></i>
          <h3>涨幅最大行业 TOP5</h3>
        </div>
        <div v-if="topRiseIndustry.length > 0" class="stat-content">
          <div v-for="(item, index) in topRiseIndustry" :key="index" class="industry-item">
            <span class="rank">{{ index + 1 }}</span>
            <span class="name">{{ item.name }}</span>
            <span class="value positive">{{ item.pctChange }}%</span>
          </div>
        </div>
        <div v-else class="no-data">无数据</div>
      </div>

      <!-- 主力净额最大的 5 个板块 -->
      <div class="stat-card">
        <div class="stat-header">
          <i class="icon-cash-in"></i>
          <h3>主力净额最大板块 TOP5</h3>
        </div>
        <div v-if="topNetAmountIndustries.length > 0" class="stat-content">
          <div v-for="(item, index) in topNetAmountIndustries" :key="index" class="industry-item">
            <span class="rank">{{ index + 1 }}</span>
            <span class="name">{{ item.name }}</span>
            <span class="value positive">{{ formatToHundredMillion(item.netAmount) }} 亿</span>
          </div>
        </div>
        <div v-else class="no-data">无数据</div>
      </div>

      <!-- 超大单占比最大的 5 个板块 -->
      <div class="stat-card">
        <div class="stat-header">
          <i class="icon-cash-in"></i>
          <h3>超大单占比最大板块 TOP5</h3>
        </div>
        <div v-if="topElgAmountRateIndustries.length > 0" class="stat-content">
          <div v-for="(item, index) in topElgAmountRateIndustries" :key="index" class="industry-item">
            <span class="rank">{{ index + 1 }}</span>
            <span class="name">{{ item.name }}</span>
            <span class="value positive">{{ item.buyElgAmountRate }}%</span>
          </div>
        </div>
        <div v-else class="no-data">无数据</div>
      </div>

      <!-- 大单占比最大的 5 个板块 -->
      <div class="stat-card">
        <div class="stat-header">
          <i class="icon-cash-in"></i>
          <h3>大单占比最大板块 TOP5</h3>
        </div>
        <div v-if="topLgAmountRateIndustries.length > 0" class="stat-content">
          <div v-for="(item, index) in topLgAmountRateIndustries" :key="index" class="industry-item">
            <span class="rank">{{ index + 1 }}</span>
            <span class="name">{{ item.name }}</span>
            <span class="value positive">{{ item.buyLgAmountRate }}%</span>
          </div>
        </div>
        <div v-else class="no-data">无数据</div>
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

    <!-- 数据表格 -->
    <div class="data-card" v-if="!loading && !error">
      <div v-if="filteredList.length > 0" class="table-responsive">
        <table class="modern-table">
          <thead>
          <tr>
            <th>交易日期</th>
            <th>数据类型</th>
            <th>板块代码</th>
            <th>板块名称</th>
            <th>涨跌幅</th>
            <th>最新指数</th>
            <th>主力净额</th>
            <th>主力占比</th>
            <th>超大单净额</th>
            <th>超大单占比</th>
            <th>大单净额</th>
            <th>大单占比</th>
            <th>中单净额</th>
            <th>中单占比</th>
            <th>小单净额</th>
            <th>小单占比</th>
            <th>主力最大股</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(item, index) in filteredList" :key="index">
            <td>{{ item.tradeDate }}</td>
            <td>{{ item.contentType }}</td>
            <td>{{ item.tsCode }}</td>
            <td>{{ item.name }}</td>
            <td :class="getChangeClass(item.pctChange)">
              {{ item.pctChange }}%
            </td>
            <td>{{ formatNumber(item.close) }}</td>
            <td :class="netAmountClass(item.netAmount)">
              {{ formatToHundredMillion(item.netAmount) }}
            </td>
            <td>{{ item.netAmountRate }}%</td>
            <td>{{ formatToHundredMillion(item.buyElgAmount) }}</td>
            <td>{{ item.buyElgAmountRate }}%</td>
            <td>{{ formatToHundredMillion(item.buyLgAmount) }}</td>
            <td>{{ item.buyLgAmountRate }}%</td>
            <td>{{ formatToHundredMillion(item.buyMdAmount) }}</td>
            <td>{{ item.buyMdAmountRate }}%</td>
            <td>{{ formatToHundredMillion(item.buySmAmount) }}</td>
            <td>{{ item.buySmAmountRate }}%</td>
            <td>{{ item.buySmAmountStock }}</td>
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
import { fetchIndMoneyFlowDcDayDate } from '@/api/tableData.js';
import { getCurrentDateFormatted } from '@/api/dateUtils';

export default {
  data() {
    return {
      list: [], // 原始数据
      filteredList: [], // 过滤后的数据
      searchDate: '', // 搜索日期
      loading: true,
      error: false,
      latestTradeDate: '', // 最新交易日
      topRiseIndustries: [], // 涨幅最大的 5 个板块
      topRiseThemes: [], // 涨幅最大的 5 个题材
      topNetAmountIndustries: [], // 主力净额最大的 5 个板块
      topElgAmountRateIndustries: [], // 超大单占比最大的 5 个板块
      topLgAmountRateIndustries: [], // 大单占比最大的 5 个板块
      topRiseIndustry: [],
    };
  },
  computed: {
    // 计算涨幅最大的 5 个板块
    topRiseIndustries() {
      if (this.filteredList.length === 0) return [];
      return this.filteredList
          .slice()
          .sort((a, b) => parseFloat(b.pctChange) - parseFloat(a.pctChange))
          .slice(0, 5);
    },
    // 计算涨幅最大的 5 个题材
    topRiseThemes() {
      if (this.filteredList.length === 0) return [];
      return this.filteredList
          .filter((item) => item.contentType === '概念')
          .sort((a, b) => parseFloat(b.pctChange) - parseFloat(a.pctChange))
          .slice(0, 5);
    },
    // 计算涨幅最大的 5 个题材
    topRiseIndustry() {
      if (this.filteredList.length === 0) return [];
      return this.filteredList
          .filter((item) => item.contentType === '行业')
          .sort((a, b) => parseFloat(b.pctChange) - parseFloat(a.pctChange))
          .slice(0, 5);
    },
    // 计算主力净额最大的 5 个板块
    topNetAmountIndustries() {
      if (this.filteredList.length === 0) return [];
      return this.filteredList
          .slice()
          .sort((a, b) => parseFloat(b.netAmount) - parseFloat(a.netAmount))
          .slice(0, 5);
    },
    // 计算超大单占比最大的 5 个板块
    topElgAmountRateIndustries() {
      if (this.filteredList.length === 0) return [];
      return this.filteredList
          .slice()
          .sort((a, b) => parseFloat(b.buyElgAmountRate) - parseFloat(a.buyElgAmountRate))
          .slice(0, 5);
    },
    // 计算大单占比最大的 5 个板块
    topLgAmountRateIndustries() {
      if (this.filteredList.length === 0) return [];
      return this.filteredList
          .slice()
          .sort((a, b) => parseFloat(b.buyLgAmountRate) - parseFloat(a.buyLgAmountRate))
          .slice(0, 5);
    },
  },
  created() {
    this.fetchData().then(() => {
      this.filteredList = this.list; // 初始化时显示全部数据
    });
  },
  methods: {
    getCurrentDateFormatted,
    async fetchData() {
      try {
        // 默认加载最新交易日的数据
        this.list = await fetchIndMoneyFlowDcDayDate(getCurrentDateFormatted());
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
          this.filteredList = await fetchIndMoneyFlowDcDayDate(tradeDate); // 调用接口获取指定日期的数据
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
    // 根据净额设置样式
    netAmountClass(netAmount) {
      return Number(netAmount) >= 0 ? 'positive' : 'negative';
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
    // 格式化数字
    formatNumber(value) {
      return Number(value).toLocaleString();
    },
    // 格式化货币
    formatCurrency(value) {
      return `¥${Number(value).toLocaleString()}`;
    },
    // 将元转换为亿元，保留两位小数
    formatToHundredMillion(value) {
      const billion = Number(value) / 100000000; // 1 亿元 = 100000000 元
      return billion.toFixed(2); // 保留两位小数
    },
  },
};
</script>

<style scoped>

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
  font-size: 32px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
}

.trade-date {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
  font-size: 14px;
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
  padding: 12px;
  border-bottom: 2px solid #e1e3e6;
  white-space: nowrap;
}

.modern-table td {
  padding: 12px;
  border-bottom: 1px solid #f0f2f5;
  text-align: center;
}

.modern-table tbody tr:hover td {
  background: #fafbff;
}

/* 特殊样式 */
.rise {
  color: #16a34a;
  font-weight: 500;
}

.fall {
  color: #dc2626;
  font-weight: 500;
}

.positive {
  color: #f20427;
  font-weight: 500;
}

.negative {
  color: #28f704;
  font-weight: 500;
}

/* 空状态 */
.empty-state {
  padding: 48px 24px;
  text-align: center;
  color: #999;
}

/* 加载动画 */
.loading-overlay {
  /* 添加加载动画样式 */
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

  .modern-table {
    font-size: 12px;
  }

  .modern-table th,
  .modern-table td {
    padding: 8px;
  }
}
</style>