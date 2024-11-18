from datetime import datetime, timedelta


def generate_dates_for_month(year, month):
    # 获取该月的第一天
    first_day = datetime(year, month, 1)

    # 获取该月的最后一天
    if month == 12:
        last_day = datetime(year + 1, 1, 1) - timedelta(days=1)
    else:
        last_day = datetime(year, month + 1, 1) - timedelta(days=1)

    # 生成该月所有日期的字符串列表
    dates = []
    current_day = first_day
    while current_day <= last_day:
        dates.append(current_day.strftime('%Y%m%d'))
        current_day += timedelta(days=1)

    return dates
