from get_mysql_conn import get_mysql_conn
from mysql.connector import Error


def execute_sql_file(connection, file_path):
    global cursor
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            sql_script = file.read()

        cursor = connection.cursor()
        for statement in sql_script.strip().split(';'):
            if statement.strip():  # 防止空语句执行
                cursor.execute(statement)
        print(f"SQL 文件 '{file_path}' 执行成功。")
    except Error as e:
        print(f"执行 SQL 文件时出错: {e}")
    except FileNotFoundError:
        print(f"未找到文件: {file_path}")
    finally:
        cursor.close()


def start_init_mysql_table():
    connection = get_mysql_conn()
    if connection:
        execute_sql_file(connection, 'create_mysql_table.sql')  #
        connection.close()


if __name__ == "__main__":
    start_init_mysql_table()
