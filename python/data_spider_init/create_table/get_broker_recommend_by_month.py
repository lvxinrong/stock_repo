import pandas as pd
import tushare as ts
from get_mysql_conn import get_mysql_conn
from get_tusahre_api_pro import get_tushare_api_pro

start_year = 2022
end_year = 2023

# 连接tushare
pro = get_tushare_api_pro()

# 初始化一个空列表来保存生成的日期字符串
date_strings = []

# 循环生成日期字符串
for year in range(start_year, end_year + 1):
    for month in range(1, 13):
        # 使用字符串格式化来生成两位数的月份
        month_string = '{:02d}'.format(month)
        date_string = f"{year}{month_string}"
        date_strings.append(date_string)

# 初始化一个空的DataFrame
df_combined = pd.DataFrame()

# 打印生成的日期字符串
for date_string in date_strings:
    print(date_string)
    # 获取指定月份的券商研报信息数据
    data = pro.broker_recommend(month=date_string)
    # 将数据转换为DataFrame并合并到df_combined中
    df = pd.DataFrame(data)
    df_combined = pd.concat([df_combined, df], ignore_index=True)

# 清理数据并转换为DataFrame
df_combined_cleaned = df_combined.astype(object).where(pd.notnull(df_combined), None)

cnx = get_mysql_conn()
# 将DataFrame写入数据库
cursor = cnx.cursor()
table_name = 'broker_recommend'

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
data_tuples = [tuple(row) for row in df_combined_cleaned.to_numpy()]

cursor.executemany(insert_query, data_tuples)

# 提交事务
cnx.commit()

# 关闭游标和数据库连接
cursor.close()
cnx.close()