# 更新最新的股票交易数据
from get_mysql_conn import get_mysql_conn
from get_tusahre_api_pro import get_tushare_api_pro
from datetime import datetime, timedelta
import pandas as pd
from date_format_utils import get_date_range


def get_latest_trade_date():
    # 创建数据库连接
    cnx = get_mysql_conn()
    print(cnx)
    try:
        cursor = cnx.cursor()
        # 查询 trade_date 最新值
        query = "SELECT MAX(trade_date) FROM trade_daily;"
        cursor.execute(query)
        result = cursor.fetchone()
        # 返回结果
        cursor.close()
        return result[0]
    finally:
        # 关闭连接
        cnx.close()


def get_next_date_string(date_string):
    current_date = datetime.strptime(date_string, "%Y%m%d")
    next_day = current_date + timedelta(days=1)
    return next_day.strftime("%Y%m%d")


# 从 date_string 开始更新交易数据
def update_trade_daily_start_with_date(date_string):
    # 使用示例
    dates = get_date_range(date_string)
    print(dates)
    # 连接tushare
    pro = get_tushare_api_pro()
    cnx = get_mysql_conn()
    # 将DataFrame写入数据库
    cursor = cnx.cursor()
    for date in dates:
        print(date)
        df = pro.daily(trade_date=date)
        # 转换为DataFrame
        ini_df = pd.DataFrame(df)
        df = ini_df.astype(object).where(pd.notnull(ini_df), None)
        df = df.rename(columns={'change': 'change_value'})
        table_name = 'trade_daily'
        # 过滤掉不需要插入的字段
        filtered_columns = [col for col in df.columns if col != 'email']
        df_filtered = df[filtered_columns]
        # 获取DataFrame的列名
        columns = df_filtered.columns.tolist()

        # 批量插入数据
        insert_query = "INSERT INTO {} ({}) VALUES ({})".format(
            table_name, ', '.join(columns), ', '.join(['%s'] * len(columns))
        )
        data_tuples = [tuple(row) for row in df_filtered[columns].values]
        cursor.executemany(insert_query, data_tuples)
        # 提交事务
        cnx.commit()
    # 每次查询一个月的数据
    # 关闭游标和数据库连接
    cursor.close()
    cnx.close()


def update_trade_daily_last_data():
    update_trade_daily_start_with_date(get_next_date_string(get_latest_trade_date()))


if __name__ == '__main__':
    update_trade_daily_last_data()
