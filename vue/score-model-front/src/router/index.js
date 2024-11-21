import { createRouter, createWebHistory } from 'vue-router';
import HuShen300 from '../views/HuShen300.vue';
import Zh100 from '../views/ZhongZheng100.vue';
import Zh500 from '../views/ZhongZheng500.vue';
import Zh800 from '../views/ZhongZheng800.vue';
import Zh1000 from '../views/ZhongZheng1000.vue';
import Detail from "@/views/Detail.vue";

const routes = [
    { path: '/', redirect: '/hushen300' },
    { path: '/hushen300', name: 'HuShen300', component: HuShen300 },
    { path: '/zh100', name: 'Zh100', component: Zh100 },
    { path: '/zh500', name: 'Zh500', component: Zh500 },
    { path: '/zh800', name: 'Zh800', component: Zh800 },
    { path: '/zh1000', name: 'Zh1000', component: Zh1000 },
    { path: '/detail/:ts_code', name: 'Detail', component: Detail },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
