from datetime import datetime, timedelta

from limit_list_ths.limit_up_ths_data_ import get_limit_up_basic_info
from daily_basic.get_daily_basic_info import get_daily_basic_info
from query_index_daily_to_mysql import query_all_index_daily_by_day
from create_table.create_stock_basic_table import update_stock_basic_all
from money_flow.get_stock_money_flow_day import update_stock_money_flow_by_trade_date
from stock_cyq.get_stock_cyq_perf_day import update_stock_cyq_perf_day
from stock_stk_factor.get_stock_stk_factor_data import get_curr_data_stock_stk_factor_data, \
    get_curr_data_stock_stk_factor_data_fast


def update_limit_up_basic_info(tradeDate: str):
    get_limit_up_basic_info(tradeDate)


def update_daily_basic_info(tradeDate):
    get_daily_basic_info(tradeDate)


def update_index_basic_daily():
    pass


if __name__ == '__main__':
    # currDate =
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
        update_stock_money_flow_by_trade_date(currDate)
    except Exception as e:
        print(f"Error occurred: update_stock_basic_all. currDate: {currDate}, Exception: {e}")

    try:
        update_stock_cyq_perf_day(currDate)
    except Exception as e:
        print(f"Error occurred: update_stock_basic_all. currDate: {currDate}, Exception: {e}")

    try:
        get_curr_data_stock_stk_factor_data_fast()
    except Exception as e:
        print(f"Error occurred: update_stock_basic_all. currDate: {currDate}, Exception: {e}")

    # try:
    #     get_curr_data_stock_stk_factor_data(currDate)
    # except Exception as e:
    #     print(f"Error occurred: update_stock_curr_data. currDate: {currDate}, Exception: {e}")

    # update_daily_basic_info(currDate)
    # update_limit_up_basic_info(currDate)
    # query_all_index_daily_by_day(currDate)
    # update_stock_basic_all()
    # update_stock_money_flow_by_trade_date(currDate)
    # update_stock_cyq_perf_day(currDate)
