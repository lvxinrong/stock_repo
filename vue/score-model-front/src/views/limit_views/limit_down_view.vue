<template>
  <div class="limit-up-page">
    <div class="header">
      <h2 class="page-title">📈 涨停跌停列表</h2>
      <div class="trade-date">交易日：{{ latestTradeDate }}</div>
    </div>

    <div v-if="loading" class="status-info loading">加载中...</div>
    <div v-if="error" class="status-info error">数据加载失败，请稍后重试</div>

    <div class="data-container" v-if="!loading && !error">
      <router-link
          to="/deepseek-analysis"
          class="model-link"
      >
        📊 模型分析结果 →
      </router-link>
      <div class="scroll-wrapper">
        <table class="stock-table">
          <thead>
          <tr>
            <th class="col-date">日期</th>
            <th class="col-code">代码</th>
            <th class="col-name">名称</th>
            <th class="col-price">收盘价</th>
            <th class="col-pct">涨跌幅</th>
            <th class="col-open">打开次数</th>
            <th class="col-reason">涨停原因</th>
            <th class="col-type">板单类别</th>
            <th class="col-tag">标签</th>
            <th class="col-status">涨停状态</th>
            <th class="col-order">封单量</th>
            <th class="col-amount">封单额</th>
            <th class="col-turnover">换手率</th>
            <th class="col-float">流通市值</th>
            <th class="col-limit">最大封单</th>
            <th class="col-rate">封板率</th>
            <th class="col-volume">成交额</th>
            <th class="col-market">市场类型</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(item, index) in list" :key="index">
            <td class="col-date">{{ item.tradeDate }}</td>
            <td class="col-code">{{ item.tsCode }}</td>
            <td class="col-name">{{ item.name }}</td>
            <td class="col-price">{{ item.price }}</td>
            <td class="col-pct" :class="getChangeClass(item.pctChg)">
              {{ item.pctChg }}
            </td>
            <td class="col-open">{{ item.openNum }}</td>
            <td class="col-reason">{{ item.luDesc || '-' }}</td>
            <td class="col-type">{{ item.limitType }}</td>
            <td class="col-tag">
              <span class="tag">{{ item.tag || '-' }}</span>
            </td>
            <td class="col-status">
              <span class="status-badge">{{ item.status }}</span>
            </td>
            <td class="col-order">{{ item.limitOrder }}</td>
            <td class="col-amount">{{ item.limitAmount }}</td>
            <td class="col-turnover">{{ item.turnoverRate }}</td>
            <td class="col-float">{{ item.freeFloat }}</td>
            <td class="col-limit">{{ item.luLimitOrder }}</td>
            <td class="col-rate">{{ item.limitUpSucRate }}</td>
            <td class="col-volume">{{ item.turnover }}</td>
            <td class="col-market">{{ item.marketType }}</td>
          </tr>
          </tbody>
        </table>
        <div v-if="list.length === 0" class="no-data">当日无跌停数据</div>
      </div>
    </div>
  </div>
</template>

<script>
import { fetchLimitDownCurrentDate } from '@/api/tableData.js'

export default {
  data() {
    return {
      list: [],
      loading: true,
      error: false,
      latestTradeDate: ''
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        this.list = await fetchLimitDownCurrentDate()
        if (this.list.length > 0) {
          this.latestTradeDate = this.list[0].tradeDate
        }
      } catch (error) {
        console.error('数据获取失败:', error)
        this.error = true
      } finally {
        this.loading = false
      }
    },
    getChangeClass(pctChg) {
      // 根据字符串值判断涨跌
      const value = parseFloat(pctChg)
      return {
        'rise': value > 0,
        'fall': value < 0
      }
    }
  }
}
</script>

<style scoped>
/* 基础布局 */
.limit-up-page {
  padding: 16px;
  max-width: 98%;
  margin: 0 auto;
}

.header {
  margin-bottom: 20px;
  padding: 12px 0;
  border-bottom: 1px solid #eee;
}

.page-title {
  font-size: 18px;
  margin: 0;
  color: #333;
}

.trade-date {
  color: #666;
  font-size: 12px;
  margin-top: 4px;
}

/* 数据容器 */
.data-container {
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.03);
}

.scroll-wrapper {
  overflow-x: auto;
  padding: 8px 0;
}

/* 表格样式 */
.stock-table {
  min-width: 1600px;
  border-collapse: separate;
  border-spacing: 0;
  font-size: 13px;
}

.stock-table th {
  background: #f8f9fa;
  color: #666;
  font-weight: 500;
  padding: 12px 8px;
  border-bottom: 2px solid #e8e8e8;
  white-space: nowrap;
}

.stock-table td {
  padding: 10px 8px;
  border-bottom: 1px solid #f0f0f0;
  transition: background 0.2s;
}

.stock-table tbody tr:hover td {
  background: #fafafa;
}

/* 列宽优化 */
.col-date { width: 90px; }
.col-code { width: 80px; }
.col-name { width: 80px; }
.col-price { width: 70px; }
.col-pct { width: 75px; }
.col-open { width: 70px; }
.col-reason { min-width: 150px; }
.col-type { width: 70px; }
.col-tag { width: 100px; }
.col-status { width: 90px; }
.col-order { width: 80px; }
.col-amount { width: 90px; }
.col-turnover { width: 75px; }
.col-float { width: 90px; }
.col-limit { width: 90px; }
.col-rate { width: 80px; }
.col-volume { width: 90px; }
.col-market { width: 80px; }

/* 特殊样式 */
.rise {
  color: #f56c6c;
  font-weight: bold;
}

.fall {
  color: #67c23a;
  font-weight: bold;
}

.tag {
  display: inline-block;
  padding: 2px 6px;
  background: #f0f2f5;
  border-radius: 3px;
  font-size: 12px;
}

.status-badge {
  display: inline-block;
  padding: 3px 8px;
  border-radius: 12px;
  font-size: 12px;
  background: #f0f9eb;
  color: #67c23a;
}

/* 状态提示 */
.status-info {
  padding: 40px 0;
  text-align: center;
  font-size: 14px;
}

.no-data {
  padding: 20px;
  color: #999;
  text-align: center;
}
</style>