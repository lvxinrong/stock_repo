import json
from datetime import datetime

import chardet
import pandas as pd
from openai import OpenAI
from get_deep_seek_client import get_deepseek_client
from get_mysql_conn import get_mysql_conn
from mysql.connector import Error


def get_stock_analyze_info(stock_list: list):
    client = get_deepseek_client()
    system_content = """
    你是一个专业的A股投资专家, 用户会提供给你一批当天涨停的股票代码，你需要对这批股票进行分析, 分析结果保持在100字左右，尽量详细，包括你的思考过程。对股票进行打分，分数范围0-100，分数越高越值得投资
    输出需要遵守以下的格式:
    [{
        "tsCode": <股票代码>,
        "analyze": <你的分析结果>,
        "score": <投资打分>
    }]
    """

    response = client.chat.completions.create(
        model="deepseek-chat",
        messages=[
            {"role": "system", "content": system_content},
            {"role": "user", "content": json.dumps(stock_list)},
        ],
        stream=False
    )
    return response.choices[0].message.content


def get_limit_up_stock(tradeDate: str) -> list:
    connection = get_mysql_conn()
    cursor = connection.cursor(dictionary=True)  # 返回字典格式的结果
    query = """
    SELECT ts_code
    FROM limit_up_ths_daily
    WHERE trade_date = %s
    """
    cursor.execute(query, (tradeDate,))
    results = cursor.fetchall()  # 获取所有结果

    return results


def save_result_to_mysql(save_result):
    # 解析 JSON 数据
    data = json.loads(save_result)

    # 连接数据库
    connection = get_mysql_conn()
    cursor = connection.cursor()

    # 插入数据的 SQL 语句
    query = """
    INSERT INTO limit_up_deepseek_analyze_result (ts_code, trade_date, analyze_result, score)
    VALUES (%s, %s, %s, %s)
    """

    # 遍历 JSON 数据并插入到数据库
    for item in data:
        values = (
            item["tsCode"],  # 股票代码
            trade_date,
            item["analyze"],  # 分析内容
            item["score"]  # 投资打分
        )
        cursor.execute(query, values)

    # 提交事务
    connection.commit()
    print("数据保存成功！")


if __name__ == '__main__':
    # trade_date = datetime.today().strftime('%Y%m%d')
    trade_date = "20250321"
    up_stock_result = get_limit_up_stock(trade_date)
    query_list = []
    for s in up_stock_result:
        query_list.append('股票代码: {}, 交易日期: {}'.format(s['ts_code'], trade_date))

    for i in range(0, len(query_list), 20):
        group = query_list[i: i + 20]
        json_result = get_stock_analyze_info(group)
        json_result = json_result.replace("```json", "")
        json_result = json_result.replace("```", "")
        print(json_result)
        save_result_to_mysql(json_result)

