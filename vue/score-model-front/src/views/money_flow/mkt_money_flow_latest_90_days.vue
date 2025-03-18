<template>
  <div class="limit-up-page">
    <!-- 头部 -->
    <div class="header">
      <h2 class="page-title">近90日大盘资金流向</h2>
    </div>

    <!-- 折线图 -->
    <div class="chart-container" v-if="!loading && !error && list.length > 0">
      <div ref="chart" class="chart"></div>
    </div>

    <!-- 数据表格 -->
    <div class="data-container" v-if="!loading && !error">
      <div class="scroll-wrapper">
        <table class="stock-table">
          <thead>
          <tr>
            <th class="col-code">交易日期</th>
            <th class="col-date">上证收盘价(点)</th>
            <th class="col-date">上证涨跌幅(%)</th>
            <th class="col-date">深证收盘价(点)</th>
            <th class="col-date">深证涨跌幅(%)</th>
            <th class="col-date">主力净流入净额(亿元)</th>
            <th class="col-date">主力净流入净占比(%)</th>
            <th class="col-date">超大单净流入净额(亿元)</th>
            <th class="col-date">超大单净流入净占比(%)</th>
            <th class="col-date">大单净流入净额(亿元)</th>
            <th class="col-date">大单净流入净占比(%)</th>
            <th class="col-date">中单净流入净额(亿元)</th>
            <th class="col-date">中单净流入净占比(%)</th>
            <th class="col-date">小单净流入净额(亿元)</th>
            <th class="col-date">小单净流入净占比(%)</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(item, index) in list" :key="index">
            <td class="col-code">{{ item.tradeDate }}</td>
            <td class="col-code">{{ item.closeSh }}</td>
            <td class="col-code">{{ item.pctChangeSh }}</td>
            <td class="col-code">{{ item.closeSz }}</td>
            <td class="col-code">{{ item.pctChangeSz }}</td>
            <td class="col-code">{{ (item.netAmount / 100000000).toFixed(2) }}</td>
            <td class="col-code">{{ item.netAmountRate }}</td>
            <td class="col-code">{{ (item.buyElgAmount / 100000000).toFixed(2) }}</td>
            <td class="col-code">{{ item.buyElgAmountRate }}</td>
            <td class="col-code">{{ (item.buyLgAmount / 100000000).toFixed(2) }}</td>
            <td class="col-code">{{ item.buyLgAmountRate }}</td>
            <td class="col-code">{{ (item.buyMdAmount / 100000000).toFixed(2) }}</td>
            <td class="col-code">{{ item.buyMdAmountRate }}</td>
            <td class="col-code">{{ (item.buySmAmount / 100000000).toFixed(2) }}</td>
            <td class="col-code">{{ item.buySmAmountRate }}</td>
          </tr>
          </tbody>
        </table>
        <div v-if="list.length === 0" class="no-data">无数据</div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import { fetchMktMoneyFlowLatest90Day } from '@/api/tableData.js';

export default {
  data() {
    return {
      list: [],
      loading: false,
      error: false,
      sortBy: 'score',        // 当前排序字段
      sortDirection: 'desc',   // 排序方向：asc/desc
    };
  },
  created() {
    this.fetchData();
  },
  mounted() {
    // 确保 DOM 挂载完成后再初始化图表
    this.$nextTick(() => {
      if (this.list.length > 0) {
        this.renderChart();
      }
    });
  },
  methods: {
    async fetchData() {
      try {
        this.list = await fetchMktMoneyFlowLatest90Day();
        // 数据加载完成后，确保 DOM 更新完成再渲染图表
        this.$nextTick(() => {
          this.renderChart();
        });
      } catch (error) {
        console.error('数据获取失败:', error);
        this.error = true;
      } finally {
        this.loading = false;
      }
    },
    renderChart() {
      // 确保 DOM 元素存在
      if (!this.$refs.chart) {
        console.error('图表容器未找到');
        return;
      }

      const chartDom = this.$refs.chart;
      const myChart = echarts.init(chartDom);

      const dates = this.list.map(item => item.tradeDate);
      const totalAmounts = this.list.map(item =>
          parseFloat(((item.netAmount + item.buyElgAmount + item.buyLgAmount + item.buyMdAmount + item.buySmAmount) / 100000000).toFixed(2))
      );

      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: '{b}: {c} 亿元'
        },
        xAxis: {
          type: 'category',
          data: dates,
          axisLabel: {
            rotate: 45 // 横轴标签旋转 45 度
          }
        },
        yAxis: {
          type: 'value',
          name: '亿元'
        },
        series: [{
          data: totalAmounts,
          type: 'bar', // 将类型改为柱状图
          barWidth: '60%', // 柱状图宽度
          itemStyle: {
            // 根据值的正负动态设置颜色
            color: (params) => {
              return params.value < 0 ? '#52c41a' : '#f5222d'; // 负值为绿色，正值为红色
            }
          }
        }]
      };

      myChart.setOption(option);
    }
  }
};
</script>
<style scoped>
/* 基础布局 */
.limit-up-page {
  padding: 24px;
  max-width: 90%;
  margin: 0 auto;
  background-color: #f9fafb;
  min-height: 100vh;
}

/* 头部样式 */
.header {
  margin-bottom: 24px;
  padding: 16px;
  background: linear-gradient(135deg, #6a11cb, #2575fc);
  border-radius: 12px;
  color: white;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  margin: 0;
}

/* 图表容器 */
.chart-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 24px;
  padding: 16px;
}

.chart {
  width: 100%;
  height: 400px;
}

/* 数据容器 */
.data-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.scroll-wrapper {
  overflow-x: auto;
  padding: 16px;
}

/* 表格样式 */
.stock-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.stock-table th {
  background-color: #f8f9fa;
  color: #555;
  font-weight: 600;
  padding: 12px 16px;
  border-bottom: 2px solid #e8e8e8;
  text-align: left;
}

.stock-table td {
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
  color: #333;
}

.stock-table tbody tr:hover {
  background-color: #f5f7fa;
}

/* 列宽优化 */
.col-code { width: 100px; }
.col-date { width: 120px; }

/* 无数据提示 */
.no-data {
  padding: 24px;
  text-align: center;
  color: #999;
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header {
    padding: 12px;
  }
  .page-title {
    font-size: 20px;
  }
  .stock-table th,
  .stock-table td {
    padding: 10px 12px;
    font-size: 12px;
  }
}
</style>