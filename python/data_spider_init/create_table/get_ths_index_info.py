import pandas as pd
import tushare as ts
from get_mysql_conn import get_mysql_conn
from get_tusahre_api_pro import get_tushare_api_pro

# 连接tushare
pro = get_tushare_api_pro()
data = pro.ths_index()

# 转换为DataFrameA
df = pd.DataFrame(data)
df_combined_cleaned = df.astype(object).where(pd.notnull(df), None)


# 将DataFrame写入数据库
cnx = get_mysql_conn()
cursor = cnx.cursor()
table_name = 'ths_index'

# 创建表格
create_table_query = """
CREATE TABLE IF NOT EXISTS {} (
  {}
);
""".format(table_name, ',\n  '.join(['{} VARCHAR(100)'.format(col) for col in df_combined_cleaned.columns]))
cursor.execute(create_table_query)

# 批量插入数据
insert_query = "INSERT INTO {} ({}) VALUES ({})".format(
    table_name, ', '.join(df_combined_cleaned.columns), ', '.join(['%s'] * len(df_combined_cleaned.columns))
)
data_tuples = [tuple(row) for row in df_combined_cleaned.values]

cursor.executemany(insert_query, data_tuples)

# 提交事务
cnx.commit()

# 关闭游标和数据库连接
cursor.close()
cnx.close()