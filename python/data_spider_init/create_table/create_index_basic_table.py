import pandas as pd
import tushare as ts
from get_mysql_conn import get_mysql_conn
from get_tusahre_api_pro import get_tushare_api_pro
import pandas as pa


def update_index_basic_info_all():
    # 连接tushare
    pro = get_tushare_api_pro()
    # 获取指数基本信息
    data = pro.index_basic()
    # 转换为DataFrame
    ini_df = pd.DataFrame(data)
    df = ini_df.astype(object).where(pd.notnull(ini_df), None)
    cnx = get_mysql_conn()
    # 将DataFrame写入数据库
    cursor = cnx.cursor()
    table_name = 'index_basic'
    # 过滤掉不需要插入的字段
    filtered_columns = [col for col in df.columns if col != 'email']
    df_filtered = df[filtered_columns]
    # 获取DataFrame的列名
    columns = df_filtered.columns.tolist()
    # 清空历史数据
    truncate_table_sql = "truncate table {}".format(table_name)
    cursor.execute(truncate_table_sql)
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
    cnx.close()


if __name__ == '__main__':
    update_index_basic_info_all()
