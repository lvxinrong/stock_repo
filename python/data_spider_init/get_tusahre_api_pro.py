import tushare as ts


# 连接tushare
def get_tushare_api_pro():
    try:
        # 2000 积分账号 5个月使用期 买入时间 2025年02月19 购入
        ts.set_token(get_token())
        pro = ts.pro_api()
        return pro
    except Exception as e:
        print(e)


def get_token():
    # 打开文件并读取整个内容
    with open('H:\\stock\\tushare_token.txt', 'r') as file:
        content = file.read()
        return content
