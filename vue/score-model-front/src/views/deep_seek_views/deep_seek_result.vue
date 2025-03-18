<!-- deep_seek_result.vue -->
<template>
  <div class="analysis-container">
    <div class="header">
      <h2 class="title">DeepSeek 模型分析报告</h2>
      <div class="subtitle">基于深度学习的市场预测</div>
    </div>

    <div class="content">
      <!-- 加载状态 -->
      <div v-if="loading" class="status-box loading">
        <div class="loader"></div>
        模型分析中，预计需要10-15秒...
      </div>

      <!-- 错误状态 -->
      <div v-if="error" class="status-box error">
        ❌ 分析失败：{{ errorMessage }}
        <button @click="retry" class="retry-btn">重试</button>
      </div>

      <!-- 分析结果 -->
      <div v-if="result" class="result-container">
        <!-- 基础分析 -->
        <div class="section">
          <h3>📌 核心结论</h3>
          <div class="card">
            <div class="conclusion">{{ result.summary }}</div>
            <div class="confidence">
              模型置信度：<span :class="confidenceClass">{{ result.confidence }}%</span>
            </div>
          </div>
        </div>

        <!-- 详细分析 -->
        <div class="section">
          <h3>📊 量化指标</h3>
          <div class="grid">
            <div class="metric-card" v-for="(metric, index) in result.metrics" :key="index">
              <div class="metric-title">{{ metric.name }}</div>
              <div class="metric-value">{{ metric.value }}</div>
              <div class="metric-desc">{{ metric.description }}</div>
            </div>
          </div>
        </div>

        <!-- 原始数据 -->
        <div class="section">
          <h3>📁 分析元数据</h3>
          <pre class="raw-data">{{ formattedRawData }}</pre>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import OpenAI from "openai";


const openai = new OpenAI({
  baseURL: 'https://api.deepseek.com',
  apiKey: 'sk-0a0255fcdad84bfa8dc6b9326f5c25e7',
  dangerouslyAllowBrowser: true
});

export default {
  data() {
    return {
      loading: true,
      error: false,
      errorMessage: '',
      result: null,
      rawData: null
    };
  },
  computed: {
    confidenceClass() {
      const confidence = this.result?.confidence || 0;
      return {
        high: confidence >= 80,
        medium: confidence >= 60 && confidence < 80,
        low: confidence < 60
      };
    },
    formattedRawData() {
      return JSON.stringify(this.rawData, null, 2);
    }
  },
  async created() {
    await this.fetchAnalysis();
  },
  methods: {
    async fetchAnalysis() {
      try {
        this.loading = true;
        this.error = false;

        const response = await axios.post(
            openai.URL,
            this.getPayload()
        );

        this.result = response.data.analysis;
        this.rawData = response.data.raw;
      } catch (error) {
        this.handleError(error);
      } finally {
        this.loading = false;
      }
    },
    getPayload() {
      // 根据需求构造请求体
      return {
        analysis_type: 'stock_limit_up',
        date: new Date().toISOString().split('T')[0],
        // 可添加其他参数
      };
    },
    handleError(error) {
      this.error = true;
      this.errorMessage = error.response?.data?.message || error.message;
    },
    retry() {
      this.fetchAnalysis();
    }
  }
};
</script>

<style scoped>
.analysis-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.header {
  text-align: center;
  margin-bottom: 2rem;
}

.title {
  font-size: 2rem;
  color: #2c3e50;
}

.subtitle {
  color: #7f8c8d;
  margin-top: 0.5rem;
}

.status-box {
  padding: 2rem;
  text-align: center;
  border-radius: 8px;
  margin: 2rem 0;
}

.loading {
  background: #f8f9fa;
  color: #6c757d;
}

.error {
  background: #fff5f5;
  color: #dc3545;
}

.loader {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 3px solid #ddd;
  border-radius: 50%;
  border-top-color: #3498db;
  animation: spin 1s linear infinite;
  margin-right: 10px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.retry-btn {
  margin-top: 1rem;
  padding: 0.5rem 1rem;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.section {
  margin: 2rem 0;
}

.card {
  background: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.conclusion {
  font-size: 1.1rem;
  line-height: 1.6;
  color: #2c3e50;
}

.confidence {
  margin-top: 1rem;
  text-align: right;
  font-weight: bold;
}

.confidence .high { color: #27ae60; }
.confidence .medium { color: #f1c40f; }
.confidence .low { color: #e74c3c; }

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
}

.metric-card {
  background: white;
  padding: 1rem;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
}

.metric-title {
  font-weight: 600;
  color: #3498db;
  margin-bottom: 0.5rem;
}

.metric-value {
  font-size: 1.5rem;
  font-weight: bold;
  color: #2c3e50;
}

.metric-desc {
  color: #7f8c8d;
  font-size: 0.9rem;
  margin-top: 0.5rem;
}

.raw-data {
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 6px;
  overflow-x: auto;
  font-family: 'Courier New', monospace;
  font-size: 0.85rem;
}
</style>