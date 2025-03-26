from datetime import datetime

from get_tusahre_api_pro import get_tushare_api_pro
from get_mysql_conn import get_mysql_conn
import pandas as pd


def update_stock_data(tsCode, startDate, endDate, tusharePro, mysqlConn):
    # 连接tushare
    pro = tusharePro

    # 获取股票列表
    data = pro.stk_factor(ts_code=tsCode, start_date=startDate, end_date=endDate)
    # 转换为DataFrame
    ini_df = pd.DataFrame(data)
    if ini_df.empty:
        print(f"No data for {tsCode}. Skipping...")
        return

    df = ini_df.astype(object).where(pd.notnull(ini_df), None)

    cnx = mysqlConn
    # 将DataFrame写入数据库
    cursor = cnx.cursor()
    table_name = 'stock_stk_factor_data'

    df = df.rename(columns={'change': 'change_value'})
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

    # 关闭游标和数据库连接
    cursor.close()
    print(f'finish data tsCode: {tsCode}, startDate: {startDate}, endDate: {endDate}')


def get_all_stock_list():
    tsCodeList = []
    connection = get_mysql_conn()
    cursor = connection.cursor()
    sql = "select ts_code from stock_basic"
    cursor.execute(sql)
    results = cursor.fetchall()
    for row in results:
        # 暂时不考虑北交所
        if row[0].endswith(".BJ"):
            continue
        tsCodeList.append(row[0])
    cursor.close()
    connection.close()
    return tsCodeList


def update_stock_curr_data(tsCode, tradeDate, tusharePro, mysqlConn):
    # 连接tushare
    pro = tusharePro

    # 获取股票列表
    data = pro.stk_factor(ts_code=tsCode, trade_date=tradeDate)
    # 转换为DataFrame
    ini_df = pd.DataFrame(data)
    if ini_df.empty:
        print(f"No data for {tsCode}. Skipping...")
        return

    df = ini_df.astype(object).where(pd.notnull(ini_df), None)

    cnx = mysqlConn
    # 将DataFrame写入数据库
    cursor = cnx.cursor()
    table_name = 'stock_stk_factor_data'

    df = df.rename(columns={'change': 'change_value'})
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

    # 关闭游标和数据库连接
    cursor.close()
    print(f'finish data tsCode: {tsCode}, tradeDate: {tradeDate}')


def get_all_stock_stk_factor_data(start, end):
    stockList = get_all_stock_list()
    tushare_pro = get_tushare_api_pro()
    connection = get_mysql_conn()
    for stock in stockList:
        try:
            update_stock_data(stock, start, end, tushare_pro, connection)
        except Exception as e:
            print(f"Error occurred: get_all_stock_stk_factor_data. tsCode: {stock}, Exception: {e}")

    connection.close()


def get_curr_data_stock_stk_factor_data(currDate):
    stockList = get_all_stock_list()
    tushare_pro = get_tushare_api_pro()
    connection = get_mysql_conn()
    for stock in stockList:
        try:
            update_stock_curr_data(stock, currDate, tushare_pro, connection)
        except Exception as e:
            print(f"Error occurred: get_all_stock_stk_factor_data. tsCode: {stock}, Exception: {e}")
    connection.close()


def update_stock_data_day(tradeDate, tusharePro, mysqlConn):
    # 连接tushare
    pro = tusharePro

    # 获取股票列表
    data = pro.stk_factor(trade_date=tradeDate)
    # 转换为DataFrame
    ini_df = pd.DataFrame(data)
    if ini_df.empty:
        print(f"No data for {tradeDate}. Skipping...")
        return

    df = ini_df.astype(object).where(pd.notnull(ini_df), None)

    cnx = mysqlConn
    # 将DataFrame写入数据库
    cursor = cnx.cursor()
    table_name = 'stock_stk_factor_data'

    df = df.rename(columns={'change': 'change_value'})
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

    # 关闭游标和数据库连接
    cursor.close()
    print(f'finish data tradeDate: {tradeDate}')


def get_curr_data_stock_stk_factor_data_fast(currDate):
    tushare_pro = get_tushare_api_pro()
    connection = get_mysql_conn()
    update_stock_data_day(currDate, tushare_pro, connection)


if __name__ == '__main__':
    # startValue = "20250101"
    # endValue = "20250331"
    # get_all_stock_stk_factor_data(startValue, endValue)
    get_curr_data_stock_stk_factor_data_fast("20250324")
