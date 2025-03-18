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


def generate_date_range(startYear, startMonth, endYear, endMonth):
    start_date = datetime(startYear, startMonth, 1)
    end_date = datetime(endYear, endMonth, 1)

    # 获取 end_month 的最后一天
    next_month = end_date.replace(day=28) + timedelta(days=4)
    end_date = next_month - timedelta(days=next_month.day)

    # 用于保存所有日期
    date_list = []

    # 遍历日期范围
    current_date = start_date
    while current_date <= end_date:
        date_list.append(current_date.strftime('%Y%m%d'))
        current_date += timedelta(days=1)

    return date_list


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


def generate_date_range_1(startYear, startMonth, endYear, endMonth):
    # 用于保存每个月的开始和结束日期
    date_range_list = []

    # 初始化开始日期
    current_year = startYear
    current_month = startMonth

    while current_year < endYear or (current_year == endYear and current_month <= endMonth):
        # 获取当前月的第一天
        start_date = datetime(current_year, current_month, 1)

        # 获取当前月的最后一天
        if current_month == 12:
            end_date = datetime(current_year, current_month, 31)
        else:
            next_month = datetime(current_year, current_month + 1, 1)
            end_date = next_month - timedelta(days=1)

        # 将开始和结束日期以元组的形式添加到列表中
        date_range_list.append((start_date.strftime('%Y%m%d'), end_date.strftime('%Y%m%d')))

        # 进入下一个月
        if current_month == 12:
            current_month = 1
            current_year += 1
        else:
            current_month += 1

    return date_range_list
