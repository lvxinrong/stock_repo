import pandas as pd
from date_format_utils import generate_dates_for_month
from get_mysql_conn import get_mysql_conn
from get_tusahre_api_pro import get_tushare_api_pro


# # 使用示例
# year = 2024
# month = 11
# dates = generate_dates_for_month(year, month)
# # 连接tushare
# pro = get_tushare_api_pro()
# cnx = get_mysql_conn()
# # 将DataFrame写入数据库
# cursor = cnx.cursor()
# for date in dates:
#     print(date)
#     df = pro.daily(trade_date=date)
#     # 转换为DataFrame
#     ini_df = pd.DataFrame(df)
#     df = ini_df.astype(object).where(pd.notnull(ini_df), None)
#     df = df.rename(columns={'change': 'change_value'})
#     table_name = 'trade_daily'
#     # 过滤掉不需要插入的字段
#     filtered_columns = [col for col in df.columns if col != 'email']
#     df_filtered = df[filtered_columns]
#     # 获取DataFrame的列名
#     columns = df_filtered.columns.tolist()
#
#     # 批量插入数据
#     insert_query = "INSERT INTO {} ({}) VALUES ({})".format(
#         table_name, ', '.join(columns), ', '.join(['%s'] * len(columns))
#     )
#     data_tuples = [tuple(row) for row in df_filtered[columns].values]
#     cursor.executemany(insert_query, data_tuples)
#     # 提交事务
#     cnx.commit()
# # 每次查询一个月的数据
# # 关闭游标和数据库连接
# cursor.close()
# cnx.close()

def insert_trade_data(year, month):
    dates = generate_dates_for_month(year, month)

    # 连接Tushare和MySQL
    pro = get_tushare_api_pro()
    cnx = get_mysql_conn()
    cursor = cnx.cursor()

    table_name = 'trade_daily'
    BATCH_SIZE = 1000  # 设置每次批量插入的数据量

    try:
        for date in dates:
            print(f"Fetching data for {date}")

            # 从Tushare获取数据
            df = pro.daily(trade_date=date)

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


def insert_trade_daily_year(year):
    pro = get_tushare_api_pro()
    cnx = get_mysql_conn()
    cursor = cnx.cursor()

    table_name = 'trade_daily'
    BATCH_SIZE = 1000  # 设置每次批量插入的数据量
    start_date = year + '0101'
    end_date = year + '0131'
    try:
        # 从Tushare获取数据
        df = pro.daily(start_date=start_date, end_date=end_date)
        if df.empty:
            print(f"No data for {year}. Skipping...")
            return

        print(df)

        # 转换为DataFrame，并进行处理
        # ini_df = pd.DataFrame(df)
        # df = ini_df.astype(object).where(pd.notnull(ini_df), None)
        # df = df.rename(columns={'change': 'change_value'})
        #
        # # 过滤掉不需要插入的字段
        # filtered_columns = [col for col in df.columns if col != 'email']
        # df_filtered = df[filtered_columns]
        #
        # # 获取DataFrame的列名
        # columns = df_filtered.columns.tolist()
        #
        # # 创建批量插入的SQL查询
        # insert_query = f"INSERT INTO {table_name} ({', '.join(columns)}) VALUES ({', '.join(['%s'] * len(columns))})"
        # data_tuples = [tuple(row) for row in df_filtered[columns].values]
        #
        # # 分批插入数据
        # for i in range(0, len(data_tuples), BATCH_SIZE):
        #     batch_data = data_tuples[i:i + BATCH_SIZE]
        #     cursor.executemany(insert_query, batch_data)
        #
        # # 每批次提交事务
        # cnx.commit()

        print("Data insertion completed.")

    except Exception as e:
        print(f"Error occurred: {e}")
        cnx.rollback()  # 出错时回滚事务

    finally:
        # 关闭游标和数据库连接
        cursor.close()
        cnx.close()


# 使用示例
# insert_trade_data(2024, 9)
if __name__ == '__main__':
    for i in range(1, 13):
        print(i)
        insert_trade_data(2023, i)

    # insert_trade_daily_year('2023')
