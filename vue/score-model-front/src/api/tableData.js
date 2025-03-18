import apiClient from './index';

// 获取沪深300数据
export const fetchHuShen300Data = (page, pageSize, stockMonth) => {
    console.log(page, pageSize)
    return apiClient.get('/hs300Stock/score', {
        params: { page, pageSize, stockMonth},
    });
};

export const fetchZh100Data = (page, pageSize, stockMonth) => {
    console.log(page, pageSize)
    return apiClient.get('/zh100Stock/score', {
        params: { page, pageSize, stockMonth},
    });
};

export const fetchZh500Data = (page, pageSize, stockMonth) => {
    console.log(page, pageSize)
    return apiClient.get('/zh500Stock/score', {
        params: { page, pageSize, stockMonth},
    });
};

export const fetchZh800Data = (page, pageSize, stockMonth) => {
    console.log(page, pageSize)
    return apiClient.get('/zh800Stock/score', {
        params: { page, pageSize, stockMonth},
    });
};

export const fetchZh1000Data = (page, pageSize, stockMonth) => {
    console.log(page, pageSize)
    return apiClient.get('/zh1000Stock/score', {
        params: { page, pageSize, stockMonth},
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

export const fetchLimitUpCurrentDate = () => {
    return apiClient.get('/limitUpThsDaily/getCurrentDate');
}

export const fetchLimitDownCurrentDate = () => {
    return apiClient.get('/limitDownThsDaily/getCurrentDate');
}

export const fetchLimitUpDeepSeekAnalyzeDate = () => {
    return apiClient.get('/limitUpDeepseekAnalyzeResult/getLatestDate');
}

export const fetchLimitUpCountDate = () => {
    return apiClient.get('/limitUpThsDaily/getLast60UpDate');
}

export const fetchIndMoneyFlowThsDayDate = (tradeDate) => {
    return apiClient.get('/indMoneyFlowThsDay/getListByTradeDate', {
        params: { tradeDate },
    });
}

export const fetchIndMoneyFlowDcDayDate = (tradeDate) => {
    return apiClient.get('/indMoneyFlowDcDay/getListByTradeDate', {
        params: { tradeDate },
    });
}

export const fetchMktMoneyFlowLatest90Day = () => {
    return apiClient.get('/mktMoneyFlowDcDay/getLast90DayDate');
}

export const fetchStockCyqPerfCurrentDate = () => {
    return apiClient.get('/stockCyqPerfDay/getLatestDateList');
}