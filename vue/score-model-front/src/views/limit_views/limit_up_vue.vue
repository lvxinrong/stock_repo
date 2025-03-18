<template>
  <div class="limit-up-page">
    <!-- 头部 -->
    <div class="header">
      <h2 class="page-title">📈 涨停股票列表</h2>
      <div class="trade-date">交易日：{{ latestTradeDate }}</div>
    </div>

    <!-- 统计区域 -->
    <div class="stats-container">
      <div class="stat-card">
        <div class="stat-title">涨停总数</div>
        <div class="stat-value">{{ totalCount }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-title">沪深主板</div>
        <div class="stat-value">{{ marketCount.HS || 0 }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-title">创业板</div>
        <div class="stat-value">{{ marketCount.GEM || 0 }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-title">科创板</div>
        <div class="stat-value">{{ marketCount.STAR || 0 }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-title">热门涨停原因</div>
        <div class="stat-value">
          <div v-for="(keyword, index) in topKeywords" :key="index" class="keyword-tag">
            {{ keyword.text }} ({{ keyword.count }})
          </div>
        </div>
      </div>
    </div>

    <!-- 折线图 -->
    <div class="chart-container">
      <div ref="lineChart" class="line-chart"></div>
    </div>

    <!-- 数据表格 -->
    <div v-if="loading" class="status-info loading">加载中...</div>
    <div v-if="error" class="status-info error">数据加载失败，请稍后重试</div>

    <div class="data-container" v-if="!loading && !error">
      <!-- 在统计区域下方添加筛选控件 -->
      <div class="filter-container">
        <label for="market-filter">市场类型：</label>
        <select id="market-filter" v-model="selectedMarket" @change="filterTable">
          <option value="">全部</option>
          <option value="HS">沪深主板</option>
          <option value="GEM">创业板</option>
          <option value="STAR">科创板</option>
        </select>
      </div>
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
          <tr v-for="(item, index) in filteredList" :key="index">
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
            <td class="col-market"><div v-html="formatMarket(item.marketType)"></div></td>
          </tr>
          </tbody>
        </table>
        <div v-if="list.length === 0" class="no-data">当日无涨停数据</div>
      </div>
    </div>
  </div>
</template>

<script>
import { fetchLimitUpCurrentDate, fetchLimitUpCountDate } from '@/api/tableData.js'
import * as echarts from 'echarts'
import { markRaw } from 'vue';

const MARKET_TYPE_MAP = {
  HS: {
    name: '沪深主板',
    color: '#1890ff'
  },
  GEM: {
    name: '创业板',
    color: '#52c41a'
  },
  STAR: {
    name: '科创板',
    color: '#f5222d'
  }
}

export default {
  data() {
    return {
      list: [],
      filteredList: [], // 过滤后的数据
      selectedMarket: '', // 选中的市场类型
      loading: true,
      error: false,
      latestTradeDate: '',
      lineChart: null, // ECharts 实例
      chartLoading: true, // 图表加载状态
      chartError: false, // 图表加载错误
      dateList: [], // 日期列表
      countList: [] // 涨停数量列表
    }
  },
  created() {
    this.fetchData().then(() => {
      this.filteredList = this.list; // 初始化时显示全部数据
    });
    this.fetchChartData()
  },
  computed: {
    // 统计计算属性
    totalCount() {
      return this.list.length
    },
    marketCount() {
      return this.list.reduce((acc, item) => {
        acc[item.marketType] = (acc[item.marketType] || 0) + 1
        return acc
      }, {})
    },
    // 新增关键词统计
    topKeywords() {
      if (!this.list.length) return []

      // 1. 拆分所有关键词
      const allKeywords = this.list
          .flatMap(item => item.luDesc.split('+'))
          .filter(Boolean) // 过滤空值

      // 2. 统计词频
      const keywordMap = allKeywords.reduce((map, keyword) => {
        map[keyword] = (map[keyword] || 0) + 1
        return map
      }, {})

      // 3. 排序并取前三
      return Object.entries(keywordMap)
          .sort((a, b) => b[1] - a[1]) // 按次数降序
          .slice(0, 3) // 取前三
          .map(([text, count]) => ({ text, count }))
    }
  },
  methods: {
    async fetchData() {
      try {
        this.list = await fetchLimitUpCurrentDate()
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
    async fetchChartData() {
      try {
        this.chartLoading = true
        this.chartError = false

        // 调用接口获取折线图数据
        const response = await fetchLimitUpCountDate()
        this.dateList = response.dateList // 日期列表
        this.countList = response.countList // 涨停数量列表

        // 初始化折线图
        this.initLineChart()
      } catch (error) {
        console.error('图表数据获取失败:', error)
        this.chartError = true
      } finally {
        this.chartLoading = false
      }
    },
    // 过滤表格数据
    filterTable() {
      if (this.selectedMarket) {
        this.filteredList = this.list.filter(item => item.marketType === this.selectedMarket);
      } else {
        this.filteredList = this.list; // 如果未选择，则显示全部
      }
    },
    getChangeClass(pctChg) {
      // 根据字符串值判断涨跌
      const value = parseFloat(pctChg)
      return {
        'rise': value > 0,
        'fall': value < 0
      }
    },
    formatMarket(type) {
      const config = MARKET_TYPE_MAP[type] || { name: type, color: '#666' }
      return `<span style="color: ${config.color}">${config.name}</span>`
    },
    // 初始化折线图
    initLineChart() {
      // 初始化 ECharts 实例
      // this.lineChart = echarts.init(this.$refs.lineChart)
      this.lineChart = markRaw(echarts.init(this.$refs.lineChart));
      // 配置折线图
      // 配置折线图
      const option = {
        title: {
          text: '近60天涨停数量趋势',
          left: 'center',
          textStyle: {
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        tooltip: {
          trigger: 'axis', // 触发类型为坐标轴
          formatter: function (params) {
            console.log(1)
            // params 是一个数组，包含当前点的信息
            const date = params[0].axisValue; // 横轴的值（日期）
            const value = params[0].data; // 纵轴的值（涨停数量）
            return `日期: ${date}<br>涨停数量: ${value}`;
          }
        },
        xAxis: {
          type: 'category',
          data: this.dateList,
          axisLabel: {
            rotate: 45 // 旋转 45 度
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '涨停数量',
            type: 'line',
            data: this.countList,
            smooth: false, // 平滑曲线
            lineStyle: {
              color: '#1890ff', // 线条颜色
              width: 2
            },
            itemStyle: {
              color: '#1890ff' // 数据点颜色
            }
          }
        ]
      };

      // 渲染图表
      this.lineChart.setOption(option)
    }
  },
  beforeDestroy() {
    // 销毁 ECharts 实例
    if (this.lineChart) {
      this.lineChart.dispose()
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
  font-size: 50px;
  margin: 0;
  color: #333;
}

.trade-date {
  color: #666;
  font-size: 30px;
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
.number {
  font-family: 'Roboto Mono', monospace;
  text-align: right;
}

.rise {
  color: #f56c6c;
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
}

.status-badge.continuous {
  background: #f0f9eb;
  color: #67c23a;
}

.status-badge.straight {
  background: #fef0f0;
  color: #f56c6c;
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

/* 统计卡片样式 */
.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin: 20px 0;
  padding: 0 10px;
}
.stat-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 120px;
  transition: all 0.3s ease;
}

.stat-card {
  transition: transform 0.2s;
}
.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.stat-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
  text-align: center;
}

.stat-value {
  font-size: 32px;
  font-weight: 600;
  color: #333;
  text-align: center;
  line-height: 1.2;
}

/* 调整表格容器上边距 */
.data-container {
  margin-top: 30px;
}
/* 关键词标签样式 */
.keyword-tag {
  display: inline-block;
  padding: 6px 12px;
  margin: 4px;
  border-radius: 16px;
  background: #e6f7ff;
  color: #1890ff;
  font-size: 13px;
  text-align: center;
  transition: all 0.2s;
}

.keyword-tag:hover {
  background: #1890ff;
  color: white;
  transform: scale(1.05);
}

/* 新增折线图容器样式 */
.chart-container {
  margin: 20px 0;
  padding: 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.line-chart {
  width: 100%;
  height: 400px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .stat-card {
    min-height: 100px;
    padding: 16px;
  }

  .stat-value {
    font-size: 28px;
  }
}

.filter-container {
  margin: 20px 0;
  padding: 10px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
}

.filter-container label {
  font-size: 14px;
  color: #666;
  margin-right: 10px;
}

.filter-container select {
  padding: 8px 12px;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  font-size: 14px;
  color: #333;
  background: #f8f9fa;
  cursor: pointer;
  transition: all 0.2s;
}

.filter-container select:hover {
  border-color: #1890ff;
  background: #fff;
}
</style>