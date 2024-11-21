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


def get_date_range(start_date_str):
    # 将字符串格式的日期转换为 datetime 对象
    start_date = datetime.strptime(start_date_str, "%Y%m%d")

    # 获取当前最新日期
    today = datetime.today()

    # 初始化日期范围列表
    date_list = []

    # 循环生成每一天的日期
    while start_date <= today:
        date_list.append(start_date.strftime("%Y%m%d"))
        start_date += timedelta(days=1)
    return date_list
