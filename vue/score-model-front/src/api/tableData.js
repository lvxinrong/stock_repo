import apiClient from './index';

// 获取沪深300数据
export const fetchHuShen300Data = (page, pageSize) => {
    return apiClient.get('/hs300Stock/score', {
        params: { page, pageSize },
    });
};

// 获取创业板数据
export const fetchChuangYeBanData = (page, pageSize) => {
    return apiClient.get('/chuangyeban', {
        params: { page, pageSize },
    });
};

// 获取双创50数据
export const fetchShuangChuang50Data = (page, pageSize) => {
    return apiClient.get('/shuangchuang50', {
        params: { page, pageSize },
    });
};
