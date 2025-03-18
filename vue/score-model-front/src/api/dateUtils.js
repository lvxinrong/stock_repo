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
export const defaultMonthOptions = generateMonthOptions(2024, 9, 2025, 2)

/**
 * 将当前时间转换为 yyyymmdd 格式的字符串
 * @returns {string} 返回格式为 yyyymmdd 的字符串
 */
export function getCurrentDateFormatted() {
    const now = new Date(); // 获取当前时间

    const year = now.getFullYear(); // 获取年份（4 位数字）
    const month = String(now.getMonth() + 1).padStart(2, '0'); // 获取月份（补零）
    const day = String(now.getDate()).padStart(2, '0'); // 获取日期（补零）

    // 拼接成 yyyymmdd 格式
    return `${year}${month}${day}`;
}