import axios from 'axios';

// 创建 Axios 实例
const apiClient = axios.create({
    baseURL: 'http://localhost:8080', // 后端基础地址
    timeout: 10000, // 请求超时时间
});

// 请求拦截器
apiClient.interceptors.request.use(
    (config) => {
        // 在发送请求前可以统一添加请求头等
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 响应拦截器
apiClient.interceptors.response.use(
    (response) => {
        return response.data; // 简化返回的数据
    },
    (error) => {
        console.error('API Error:', error);
        return Promise.reject(error);
    }
);

export default apiClient;
