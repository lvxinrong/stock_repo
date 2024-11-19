import { createRouter, createWebHistory } from 'vue-router';
import HuShen300 from '../views/HuShen300.vue';
import ChuangYeBan from '../views/ChuangYeBan.vue';
import ShuangChuang50 from '../views/ShuangChuang50.vue';

const routes = [
    { path: '/', redirect: '/hushen300' },
    { path: '/hushen300', name: 'HuShen300', component: HuShen300 },
    { path: '/chuangyeban', name: 'ChuangYeBan', component: ChuangYeBan },
    { path: '/shuangchuang50', name: 'ShuangChuang50', component: ShuangChuang50 },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
