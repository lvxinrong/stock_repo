import pandas as pd
from date_format_utils import generate_dates_for_month
from get_mysql_conn import get_mysql_conn
from get_tusahre_api_pro import get_tushare_api_pro

# 使用示例
year = 2024
month = 11
dates = generate_dates_for_month(year, month)
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
