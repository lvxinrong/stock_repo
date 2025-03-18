import pandas as pd
from get_mysql_conn import get_mysql_conn
from get_tusahre_api_pro import get_tushare_api_pro


# 定义获取成分股数据的函数
def get_member_data(ts_code, pro):
    print('Getting member data for {}'.format(ts_code))
    cnx = get_mysql_conn()
    # 使用传入的 pro 参数获取成分股数据
    member_data = pro.ths_member(ts_code=ts_code)
    member_df = pd.DataFrame(member_data)
    member_df_cleaned = member_df.astype(object).where(pd.notnull(member_df), None)
    # 将成分股数据写入数据库
    insert_member_data(member_df_cleaned, cnx)
    cnx.close()
    print('Finished getting member data for {}'.format(ts_code))


# 定义插入成分股数据的函数
def insert_member_data(data_tuples, cnx):
    # 插入成分股数据的函数
    member_table_name = 'ths_member'
    member_cursor = cnx.cursor()

    # 创建表格
    create_member_table_query = """
    CREATE TABLE IF NOT EXISTS {} (
      {}
    );
    """.format(member_table_name, ',\n  '.join(['{} VARCHAR(100)'.format(col) for col in data_tuples[0]._data.keys()]))
    member_cursor.execute(create_member_table_query)

    # 批量插入THS成分股数据
    member_insert_query = "INSERT INTO {} ({}) VALUES ({})".format(
        member_table_name, ', '.join(data_tuples[0]._data.keys()), ', '.join(['%s'] * len(data_tuples[0]._data.keys()))
    )
    member_data_tuples = [tuple(row._data.values()) for row in data_tuples]
    member_cursor.executemany(member_insert_query, member_data_tuples)

    # 提交事务
    member_cursor.commit()

    member_cursor.close()


def process_member_data(ts_code):
    # 获取并插入成分股数据的函数
    print('Processing member data for {}'.format(ts_code))
    try:
        pro = get_tushare_api_pro()
        get_member_data(ts_code, pro)
    except Exception as e:
        print(f"Error processing {ts_code}: {e}")
