import tushare as ts


# 连接tushare
def get_tushare_api_pro():
    try:
        # 2000 积分账号 5个月使用期 买入时间 2023年6月14日
        ts.set_token('dfb6e9f4f9a3db86c59a3a0f680a9bdc46ed1b5adbf1e354c7faa761')
        pro = ts.pro_api()
        return pro
    except Exception as e:
        print(e)

print(get_tushare_api_pro())