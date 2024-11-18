import pandas as pd
from get_mysql_conn import get_mysql_conn
from get_tusahre_api_pro import get_tushare_api_pro


hs_300_index_code = '399006.SZ'
pro = get_tushare_api_pro()
data = pro.index_weight(index_code=hs_300_index_code)
# 转换为DataFrame
ini_df = pd.DataFrame(data)
df = ini_df.astype(object).where(pd.notnull(ini_df), None)

cnx = get_mysql_conn()
# 将DataFrame写入数据库
cursor = cnx.cursor()
table_name = 'cyz_100_stock'

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
print(df)
