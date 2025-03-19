import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import './style/common.scss'; // 引入公共样式
import locale from 'element-plus/es/locale/lang/zh-cn'
import * as Vue from "echarts";



const app = createApp(App);
app.use(router);
app.use(ElementPlus, { locale });
app.mount('#app');