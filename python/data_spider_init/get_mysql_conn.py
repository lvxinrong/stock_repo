import mysql.connector
from mysql.connector import Error


def get_mysql_conn():
    try:
        conn = mysql.connector.connect(host='192.168.3.8',
                                       database='good_stock',
                                       user='root',
                                       password='123456')
        if conn.is_connected():
            print('Connected to MySQL database')
            return conn
    except Error as e:
        print(e)


def test_get_mysql_conn():
    conn = get_mysql_conn()
    assert conn.is_connected()
    conn.close()
