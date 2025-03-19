from money_flow.get_ind_money_flow_dc_day import update_ind_money_flow_dc_by_trade_date
from money_flow.get_ind_money_flow_ths_day import update_ind_money_flow_ths_by_trade_date
from get_mkt_money_flow_dc_day import update_mkt_money_flow_dc_by_trade_date
from get_stock_money_flow_day import update_stock_money_flow_by_trade_date
from get_stock_money_flow_dc_day import update_stock_money_flow_dc_by_trade_date
from get_stock_money_flow_ths_day import update_stock_money_flow_ths_by_trade_date
from datetime import datetime


def update_curr_money_flow_data():
    currDate = datetime.today().strftime('%Y%m%d')
    try:
        update_ind_money_flow_dc_by_trade_date(currDate)
    except Exception as e:
        print(f"Error occurred: update_ind_money_flow_dc_by_trade_date. currDate: {currDate}, Exception: {e}")

    try:
        update_ind_money_flow_ths_by_trade_date(currDate)
    except Exception as e:
        print(f"Error occurred: update_ind_money_flow_ths_by_trade_date. currDate: {currDate}, Exception: {e}")

    try:
        update_mkt_money_flow_dc_by_trade_date(currDate)
    except Exception as e:
        print(f"Error occurred: update_mkt_money_flow_dc_by_trade_date. currDate: {currDate}, Exception: {e}")

    try:
        update_stock_money_flow_by_trade_date(currDate)
    except Exception as e:
        print(f"Error occurred: update_mkt_money_flow_dc_by_trade_date. currDate: {currDate}, Exception: {e}")

    try:
        update_stock_money_flow_dc_by_trade_date(currDate)
    except Exception as e:
        print(f"Error occurred: update_stock_money_flow_dc_by_trade_date. currDate: {currDate}, Exception: {e}")

    try:
        update_stock_money_flow_ths_by_trade_date(currDate)
    except Exception as e:
        print(f"Error occurred: update_stock_money_flow_ths_by_trade_date. currDate: {currDate}, Exception: {e}")


if __name__ == '__main__':
    update_curr_money_flow_data()
