import pandas as pd
from get_mysql_conn import get_mysql_conn
from get_tusahre_api_pro import get_tushare_api_pro
from date_format_utils import generate_date_range

def get_daily_basic_info(trade_date):
    pro = get_tushare_api_pro()
    data = pro.daily_basic(trade_date=trade_date)
    # 转换为DataFrame
    ini_df = pd.DataFrame(data)
    df = ini_df.astype(object).where(pd.notnull(ini_df), None)
    if df.empty:
        print(f"No data for {trade_date}. Skipping...")
        return
    cnx = get_mysql_conn()
    # 将DataFrame写入数据库
    cursor = cnx.cursor()
    table_name = 'daily_basic_info'

    # 清空历史数据
    truncate_table_sql = "truncate table {}".format(table_name)
    cursor.execute(truncate_table_sql)
    # 获取DataFrame的列名
    columns = df.columns.tolist()
    # 批量插入数据
    insert_query = "INSERT INTO {} ({}) VALUES ({})".format(
        table_name, ', '.join(columns), ', '.join(['%s'] * len(columns))
    )
    data_tuples = [tuple(row) for row in df[columns].values]

    cursor.executemany(insert_query, data_tuples)

    # 提交事务
    cnx.commit()

    # 关闭游标和数据库连接
    cursor.close()
    cnx.close()

def get_daily_basic_info_by_month(startYear, startMonth, endYear, endMonth):
    dates = generate_date_range(startYear, startMonth, endYear, endMonth);
    for tradeDate in dates:
        get_daily_basic_info(tradeDate)

get_daily_basic_info_by_month(2024,9,2025,2)