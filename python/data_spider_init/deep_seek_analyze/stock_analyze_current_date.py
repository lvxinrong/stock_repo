import json

from elasticsearch import Elasticsearch
from get_mysql_conn import get_mysql_conn
from get_deep_seek_client import get_deepseek_client
import deepseek


def get_es_client():
    es = Elasticsearch(hosts=['http://192.168.3.74:9200/'])
    return es


def get_stock_deepseek_analyze_info(stock_name, cal_date):
    client = get_deepseek_client()
    system_content = f"""
    今天是 {cal_date}，分析一下{stock_name}近20个交易日的走势，并对未来5个交易日的涨跌进行预测,并按照你的理解给出买入点和止盈止损位。
    """

    print(system_content)
    response = client.chat.completions.create(
        model="deep_seek_analyze-reasoner",
        messages=[
            {
                "role": "user",
                "content": system_content
            }
        ],
        paramteres={

        },

        stream=False
    )
    return response.choices[0].message.content, response.choices[0].message.reasoning_content


def get_macd_20_from_es(limitTop, tradeDate):
    es_client = get_es_client()
    currDate = tradeDate
    # 构建查询条件
    query_body = {
        "query": {
            "bool": {
                "filter": [  # 精确匹配buySignal=true的文档
                    {"term": {"buySignal": True}}
                ]
            }
        },
        "sort": [  # 按趋势强度降序排序
            {"trendStrength": {"order": "desc"}}
        ],
        "size": limitTop  # 取前十条
    }
    index_name = "macd20_" + currDate

    try:
        # 执行搜索
        response = es_client.search(
            index=index_name,  # 指定索引名称
            body=query_body
        )

        # 处理查询结果
        results = []
        for hit in response['hits']['hits']:
            source_data = hit['_source']
            results.append({
                "id": source_data["id"],
                "tsCode": source_data["tsCode"],
                "trendStrength": source_data["trendStrength"],
                "buySignal": source_data["buySignal"],
                "lastTradeDate": source_data["lastTradeDate"]
            })
        return results
    except Exception as e:
        print(f"查询异常: {str(e)}")


def save_result_to_mysql(tradeDate, name, deepSeekContent, reasoningContent, stockPool):
    try:
        conn = get_mysql_conn()
        cursor = conn.cursor()
        # 转换数据结构（假设stockPool是列表/字典）
        stock_json = json.dumps(stockPool, ensure_ascii=False)

        # 参数化SQL语句防止注入[4,8](@ref)
        sql = """INSERT INTO birthday_good_stock_day 
                (trade_date, name, deep_seek_content, reasoning_content, stock_pool)
                VALUES (%s, %s, %s, %s, %s)"""

        # 执行插入操作[1,6](@ref)
        cursor.execute(sql, (
            tradeDate,  # 转换为日期对象
            name,
            deepSeekContent,
            reasoningContent,
            stock_json
        ))
        conn.commit()
        cursor.close()
        conn.close()
    except Exception as e:
        print(f"数据库操作失败: {str(e)}")


if __name__ == '__main__':
    print(get_stock_deepseek_analyze_info("奥普光电", "2025年4月1日"))
