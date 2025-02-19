import pandas as pd
from get_mysql_conn import get_mysql_conn
from get_tusahre_api_pro import get_tushare_api_pro
from date_format_utils import generate_date_range_1


# ts_code_index = ['000001.SH', '399001.SZ', '399006.SZ', '399300.SZ']
def query_index_daily(index_code, startYear, startMonth, endYear, endMonth):
    dates = generate_date_range_1(startYear, startMonth, endYear, endMonth)

    # 连接tushare
    pro = get_tushare_api_pro()
    cnx = get_mysql_conn()

    # 创建游标
    cursor = cnx.cursor()

    # 批量插入
    table_name = 'index_basic_daily'

    for date in dates:
        print(f"Fetching data for {date} - {index_code}")

        try:
            # 从Tushare获取数据
            df = pro.index_daily(ts_code=index_code, start_date=date[0], end_date=date[1])

            if df.empty:
                print(f"No data for {date}. Skipping...")
                continue

            # 转换为DataFrame
            ini_df = pd.DataFrame(df)
            df = ini_df.astype(object).where(pd.notnull(ini_df), None)
            df = df.rename(columns={'change': 'change_value'})

            # 获取DataFrame的列名
            columns = df.columns.tolist()

            # 批量插入数据的SQL语句
            insert_query = f"INSERT INTO {table_name} ({', '.join(columns)}) VALUES ({', '.join(['%s'] * len(columns))})"
            data_tuples = [tuple(row) for row in df[columns].values]

            # 执行批量插入
            cursor.executemany(insert_query, data_tuples)

            # 提交事务（一次提交）
            cnx.commit()

        except Exception as e:
            print(f"Error fetching data for {date}: {e}")
            continue

    # 关闭游标和数据库连接
    cursor.close()
    cnx.close()
    print("Data insertion completed.")

startYear=2024
endYear=2025
startMonth=9
endMonth=2


query_index_daily("000905.SH", startYear, startMonth, endYear, endMonth)
query_index_daily("000906.SH", startYear, startMonth, endYear, endMonth)
query_index_daily("000852.SH", startYear, startMonth, endYear, endMonth)
query_index_daily("399300.SZ", startYear, startMonth, endYear, endMonth)
query_index_daily("000903.SH", startYear, startMonth, endYear, endMonth)
