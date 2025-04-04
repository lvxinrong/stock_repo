import { createRouter, createWebHistory } from 'vue-router';
import HuShen300 from '../views/HuShen300.vue';
import Zh100 from '../views/ZhongZheng100.vue';
import Zh500 from '../views/ZhongZheng500.vue';
import Zh800 from '../views/ZhongZheng800.vue';
import Zh1000 from '../views/ZhongZheng1000.vue';
import Detail from "@/views/Detail.vue";
import LimitUp from "@/views/limit_views/limit_up_vue.vue"
import LimitDown from "@/views/limit_views/limit_down_view.vue"
import deep_seek_result from "@/views/deep_seek_views/deep_seek_result.vue";
import deep_seek_day_result from "@/views/deep_seek_views/limit_up_deep_day_result.vue";
import ind_money_flow_ths_day_vue from "@/views/money_flow/ind_money_flow_ths_day_vue.vue";
import ind_money_flow_dc_day_vue from "@/views/money_flow/ind_money_flow_dc_day.vue";
import mkt_money_flow_latest_90_days from "@/views/money_flow/mkt_money_flow_latest_90_days.vue";
import stock_cyq_perf_day from "@/views/stock/stock_cyq_perf_day.vue";
import stock_stk_factor_result from "@/views/stock/stock_technical_indicator_list.vue";
import macd_20_day_cal_result from "@/views/macd20_result/macd_20_cal_result.vue"


const routes = [
    { path: '/', redirect: '/hushen300' },
    { path: '/hushen300', name: 'HuShen300', component: HuShen300 },
    { path: '/zh100', name: 'Zh100', component: Zh100 },
    { path: '/zh500', name: 'Zh500', component: Zh500 },
    { path: '/zh800', name: 'Zh800', component: Zh800 },
    { path: '/zh1000', name: 'Zh1000', component: Zh1000 },
    { path: '/limit-up-day', name: 'LimitUpDay', component: LimitUp },
    { path: '/limit-down-day', name: 'limitDown', component: LimitDown },
    { path: '/detail/:ts_code', name: 'Detail', component: Detail },
    {path: '/deepseek-analysis', name: 'DeepSeekResult',  component: deep_seek_result},
    {path: '/limit-up-day/deepseek-result', name: 'DeepSeekAnalyzeResult',  component: deep_seek_day_result},
    {path: '/money-flow/ind_money_flow_ths_day_vue', name: 'indMoneyFlowThsDayVue', component: ind_money_flow_ths_day_vue},
    {path: '/money-flow/ind_money_flow_dc_day_vue', name: 'indMoneyFlowDcDayVue', component: ind_money_flow_dc_day_vue},
    {path: '/money-flow/mkt-money-flow-latest', name: 'mktMoneyFlowLatest90Days', component: mkt_money_flow_latest_90_days},
    {path: '/stock_cyq/stock_cyq_perf_day', name: 'stockCyqPerfDay', component: stock_cyq_perf_day},
    {path: '/technical-indicator/daily', name: 'stockStkFactorResult', component: stock_stk_factor_result },
    {path: '/strategy/macd-20', name: 'macd20DaysCalResult', component: macd_20_day_cal_result},
    {path: '/stock_cyq/stock_cyq_converge', name: 'stockCyqConverge', component: () => import('@/views/stock_cyq/stock_cyq_converge.vue')}
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
