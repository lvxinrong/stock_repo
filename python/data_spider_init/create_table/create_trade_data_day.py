import pandas as pd
import tushare as ts
from get_mysql_conn import get_mysql_conn

cnx = get_mysql_conn()

# 连接tushare
ts.set_token('dfb6e9f4f9a3db86c59a3a0f680a9bdc46ed1b5adbf1e354c7faa761')
pro = ts.pro_api()

# 获取股票列表
data = pro.query('stock_basic', exchange='', list_status='L',
                 fields='ts_code,symbol,name,area,industry,fullname,enname,cnspell,market,exchange,curr_type,'
                        'list_status,list_date,delist_date,is_hs')
# 转换为DataFrame
ini_df = pd.DataFrame(data)

df = ini_df.astype(object).where(pd.notnull(ini_df), None)

cnx = get_mysql_conn()
# 将DataFrame写入数据库
cursor = cnx.cursor()
table_name = 'trade_data_day'

# 过滤掉不需要插入的字段
filtered_columns = [col for col in df.columns if col != 'email']
df_filtered = df[filtered_columns]

# 获取DataFrame的列名
columns = df_filtered.columns.tolist()

# 创建表格
create_table_query = """
CREATE TABLE IF NOT EXISTS {} (
  {}
);
""".format(table_name, ',\n  '.join(['{} VARCHAR(100)'.format(col) for col in columns]))
cursor.execute(create_table_query)

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
