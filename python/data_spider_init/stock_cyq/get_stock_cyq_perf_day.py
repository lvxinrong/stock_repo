import pandas as pd
from get_mysql_conn import get_mysql_conn
from get_tusahre_api_pro import get_tushare_api_pro
from date_format_utils import generate_dates_for_month


def update_stock_money_flow_ths_by_month(year, month):
    dates = generate_dates_for_month(year, month)
    # 连接Tushare和MySQL
    pro = get_tushare_api_pro()
    cnx = get_mysql_conn()
    cursor = cnx.cursor()

    table_name = 'stock_cyq_perf_day'
    BATCH_SIZE = 1000  # 设置每次批量插入的数据量

    try:
        for date in dates:
            print(f"Fetching data for {date}")

            # 从Tushare获取数据
            df = pro.cyq_perf(trade_date=date)

            if df.empty:
                print(f"No data for {date}. Skipping...")
                continue

            # 转换为DataFrame，并进行处理
            ini_df = pd.DataFrame(df)
            df = ini_df.astype(object).where(pd.notnull(ini_df), None)
            df = df.rename(columns={'change': 'change_value'})

            # 过滤掉不需要插入的字段
            filtered_columns = [col for col in df.columns if col != 'email']
            df_filtered = df[filtered_columns]

            # 获取DataFrame的列名
            columns = df_filtered.columns.tolist()

            # 创建批量插入的SQL查询
            insert_query = f"INSERT INTO {table_name} ({', '.join(columns)}) VALUES ({', '.join(['%s'] * len(columns))})"
            data_tuples = [tuple(row) for row in df_filtered[columns].values]

            # 分批插入数据
            for i in range(0, len(data_tuples), BATCH_SIZE):
                batch_data = data_tuples[i:i + BATCH_SIZE]
                cursor.executemany(insert_query, batch_data)

            # 每批次提交事务
            cnx.commit()

        print("Data insertion completed.")

    except Exception as e:
        print(f"Error occurred: {e}")
        cnx.rollback()  # 出错时回滚事务

    finally:
        # 关闭游标和数据库连接
        cursor.close()
        cnx.close()


def update_stock_cyq_perf_day(tradeDate):
    pro = get_tushare_api_pro()
    cnx = get_mysql_conn()
    cursor = cnx.cursor()
    table_name = 'stock_cyq_perf_day'
    # 从Tushare获取数据
    df = pro.cyq_perf(trade_date=tradeDate)
    if df.empty:
        print(f"No data for {tradeDate}. Skipping...")
        return
    # 转换为DataFrame，并进行处理
    ini_df = pd.DataFrame(df)
    df = ini_df.astype(object).where(pd.notnull(ini_df), None)
    df = df.rename(columns={'change': 'change_value'})
    # 过滤掉不需要插入的字段
    filtered_columns = [col for col in df.columns if col != 'email']
    df_filtered = df[filtered_columns]
    # 获取DataFrame的列名
    columns = df_filtered.columns.tolist()
    # 创建批量插入的SQL查询
    insert_query = f"INSERT INTO {table_name} ({', '.join(columns)}) VALUES ({', '.join(['%s'] * len(columns))})"
    data_tuples = [tuple(row) for row in df_filtered[columns].values]
    cursor.executemany(insert_query, data_tuples)
    # 每批次提交事务
    cnx.commit()


if __name__ == '__main__':
    update_stock_cyq_perf_day("20250301")



