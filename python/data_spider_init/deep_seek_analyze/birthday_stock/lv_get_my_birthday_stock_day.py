from datetime import datetime

from deep_seek_analyze.get_deep_seek_client import get_deepseek_client
from get_mysql_conn import get_mysql_conn
import json
from elasticsearch import Elasticsearch


def get_es_client():
    es = Elasticsearch(hosts=['http://192.168.3.74:9200/'])
    return es


def get_my_scbz():
    # 打开文件并读取整个内容
    with open('H:\\stock\\lxr_scbz.txt', 'r', encoding='utf-8') as file:
        content = file.read()
        return content


def get_zhb_scbz():
    # 打开文件并读取整个内容
    with open('H:\\stock\\zhb_scbz.txt', 'r', encoding='utf-8') as file:
        content = file.read()
        return content


def get_lzd_scbz():
    # 打开文件并读取整个内容
    with open('H:\\stock\\lzd_scbz.txt', 'r', encoding='utf-8') as file:
        content = file.read()
        return content


def get_sy_scbz():
    # 打开文件并读取整个内容
    # 打开文件并读取整个内容
    with open('H:\\stock\\sy_scbz.txt', 'r', encoding='utf-8') as file:
        content = file.read()
        return content


def get_my_birthday_stock(stock_list: list, scbz_string, trade_date):
    client = get_deepseek_client()
    system_content = f"""
    您是一个知识面非常全面的投资专家，您可以通过生辰八字进行选股。 {scbz_string} 请结合这个八字 根据他在交易日: {trade_date} 自己选出的股票池，推荐三只可以买入的股票
    输出需要遵守以下的格式:
    [
        (推荐股票1: <股票代码>. 推荐理由: <推荐理由>),
        (推荐股票2: <股票代码>. 推荐理由: <推荐理由>)
    ]
    """

    print(system_content)
    response = client.chat.completions.create(
        model="deep_seek_analyze-reasoner",
        messages=[
            {"role": "system", "content": system_content},
            {"role": "user", "content": json.dumps(stock_list)},
        ],
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
    currDate = datetime.today().strftime('%Y%m%d')
    stock_info = get_macd_20_from_es(10, currDate)
    stock_code_list = []
    for value in stock_info:
        stock_code_list.append(value['tsCode'])

    # lxr的
    # lxr_content, lxr_reasoning_content = get_my_birthday_stock(stock_code_list, get_my_scbz(), currDate)
    # save_result_to_mysql(currDate, "lxr", lxr_content, lxr_reasoning_content, stock_code_list)
    # # lzd
    # lzd_content, lzd_reasoning_content = get_my_birthday_stock(stock_code_list, get_lzd_scbz(), currDate)
    # save_result_to_mysql(currDate, "lzd", lzd_content, lzd_reasoning_content, stock_code_list)
    # # zhb
    # zhb_content, zhb_reasoning_content = get_my_birthday_stock(stock_code_list, get_zhb_scbz(), currDate)
    # save_result_to_mysql(currDate, "zhb", zhb_content, zhb_reasoning_content, stock_code_list)
    # sy
    sy_content, sy_reasoning_content = get_my_birthday_stock(stock_code_list, get_sy_scbz(), currDate)
    save_result_to_mysql(currDate, "sy", sy_content, sy_reasoning_content, stock_code_list)

