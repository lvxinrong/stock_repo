import pandas as pd
from get_mysql_conn import get_mysql_conn
from get_tusahre_api_pro import get_tushare_api_pro
from date_format_utils import generate_dates_for_month


# ts_code_index = ['000001.SH', '399001.SZ', '399006.SZ', '399300.SZ']
ts_code_index = ['399300.SZ']

table_name = 'index_basic_daily'

year = 2024
month = 11
dates = generate_dates_for_month(year, month)
# 连接tushare
pro = get_tushare_api_pro()
cnx = get_mysql_conn()
# 将DataFrame写入数据库
cursor = cnx.cursor()
for date in dates:
    df = pro.index_daily(ts_code=ts_code_index[0], trade_date=date)
    # 转换为DataFrame
    ini_df = pd.DataFrame(df)
    df = ini_df.astype(object).where(pd.notnull(ini_df), None)
    df = df.rename(columns={'change': 'change_value'})
    table_name = 'index_basic_daily'
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
# 每次查询一个月的数据
# 关闭游标和数据库连接
cursor.close()
cnx.close()
