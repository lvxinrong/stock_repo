from datetime import datetime

from daily_data.update_stock_trade_daily_data import update_trade_daily_last_data
from limit_list_ths.limit_up_ths_data_ import get_limit_up_basic_info
from daily_basic.get_daily_basic_info import get_daily_basic_info
from query_index_daily_to_mysql import query_all_index_daily_by_day
from create_table.create_stock_basic_table import update_stock_basic_all
from stock_cyq.get_stock_cyq_perf_day import update_stock_cyq_perf_day
from stock_stk_factor.get_stock_stk_factor_data import get_curr_data_stock_stk_factor_data_fast
import requests


def update_limit_up_basic_info(tradeDate: str):
    get_limit_up_basic_info(tradeDate)


def update_daily_basic_info(tradeDate):
    get_daily_basic_info(tradeDate)


def update_index_basic_daily():
    pass


def http_request_macd_20_result():
    url = "http://localhost:8080/macd20CalResultSearch/generateCurrentData"
    requests.get(url)


if __name__ == '__main__':
    currDate = datetime.today().strftime('%Y%m%d')
    try:
        update_daily_basic_info(currDate)
    except Exception as e:
        print(f"Error occurred: update_daily_basic_info. currDate: {currDate}, Exception: {e}")

    try:
        update_limit_up_basic_info(currDate)
    except Exception as e:
        print(f"Error occurred: update_limit_up_basic_info. currDate: {currDate}, Exception: {e}")

    try:
        query_all_index_daily_by_day(currDate)
    except Exception as e:
        print(f"Error occurred: query_all_index_daily_by_day. currDate: {currDate}, Exception: {e}")

    try:
        update_stock_basic_all()
    except Exception as e:
        print(f"Error occurred: update_stock_basic_all. currDate: {currDate}, Exception: {e}")

    try:
        update_stock_cyq_perf_day(currDate)
    except Exception as e:
        print(f"Error occurred: update_stock_basic_all. currDate: {currDate}, Exception: {e}")

    try:
        get_curr_data_stock_stk_factor_data_fast(currDate)
    except Exception as e:
        print(f"Error occurred: update_stock_basic_all. currDate: {currDate}, Exception: {e}")

    try:
        update_trade_daily_last_data()
    except Exception as e:
        print(f"Error occurred: update_trade_daily_start_with_date. currDate: {currDate}, Exception: {e}")

