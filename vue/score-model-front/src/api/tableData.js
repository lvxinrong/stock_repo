import apiClient from './index';

// 获取沪深300数据
export const fetchHuShen300Data = (page, pageSize) => {
    console.log(page, pageSize)
    return apiClient.get('/hs300Stock/score', {
        params: { page, pageSize },
    });
};

export const fetchZh100Data = (page, pageSize) => {
    console.log(page, pageSize)
    return apiClient.get('/zh100Stock/score', {
        params: { page, pageSize },
    });
};

export const fetchZh500Data = (page, pageSize) => {
    console.log(page, pageSize)
    return apiClient.get('/zh500Stock/score', {
        params: { page, pageSize },
    });
};

export const fetchZh800Data = (page, pageSize) => {
    console.log(page, pageSize)
    return apiClient.get('/zh800Stock/score', {
        params: { page, pageSize },
    });
};

export const fetchZh1000Data = (page, pageSize) => {
    console.log(page, pageSize)
    return apiClient.get('/zh1000Stock/score', {
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

export const fetchDetailData = (ts_code) => {
    return apiClient.get('/tradeDaily/getStockTradeList', {
        params: { ts_code },
    });
}
