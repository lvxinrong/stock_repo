// 生成指定范围内的 yyyymm 数据
export function generateMonthOptions(startYear, startMonth, endYear, endMonth) {
    const options = [];
    let year = startYear;
    let month = startMonth;

    while (year < endYear || (year === endYear && month <= endMonth)) {
        const formattedMonth = month < 10 ? `0${month}` : `${month}`;
        options.push(`${year}${formattedMonth}`);
        month++;

        if (month > 12) {
            month = 1;
            year++;
        }
    }

    return options;
}

// 默认导出一个生成 2023 年 1 月到当前时间的 yyyymm 数组
export const defaultMonthOptions = ["202411", "202410"]