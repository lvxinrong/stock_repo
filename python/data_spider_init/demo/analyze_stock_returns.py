import pandas as pd
import matplotlib.pyplot as plt
import matplotlib


def analyze_stock_returns(file_path):
    # 设置字体为SimHei，确保该字体在你的系统中存在
    matplotlib.rcParams['font.sans-serif'] = ['SimHei']  # 指定默认字体
    matplotlib.rcParams['axes.unicode_minus'] = False  # 解决保存图像时负号'-'显示为方块的问题
    try:
        # 读取数据并处理异常值
        df = pd.read_csv(file_path, sep=': ', header=None,
                         names=['股票代码', '收益率'], engine='python')
        df['收益率'] = pd.to_numeric(df['收益率'], errors='coerce')
        df = df.dropna()

        if df.empty:
            raise ValueError("文件未包含有效数据")

        # 添加市场类型列（深交所/上交所）
        df['市场'] = df['股票代码'].apply(lambda x: '深市' if x.endswith('SZ') else '沪市')

        # 添加股票类型分类（根据代码前缀）
        def classify_stock(code):
            prefix = code[:3]
            if prefix.startswith('00'):
                return '深市主板'
            elif prefix.startswith('30'):
                return '创业板'
            elif prefix.startswith('60'):
                return '沪市主板'
            elif prefix.startswith('688'):
                return '科创板'
            else:
                return '其他'

        df['类型'] = df['股票代码'].apply(classify_stock)

        # 基础统计分析
        stats = {
            "总股票数": len(df),
            "有效数据数": df['收益率'].count(),
            "平均收益率": df['收益率'].mean(),
            "收益率中位数": df['收益率'].median(),
            "最大收益率": df['收益率'].max(),
            "最小收益率": df['收益率'].min(),
            "收益率标准差": df['收益率'].std(),
            "正收益股票数": (df['收益率'] > 0).sum(),
            "负收益股票数": (df['收益率'] < 0).sum(),
            "收益率总和": df['收益率'].sum()
        }

        # 分组统计
        market_stats = df.groupby('市场')['收益率'].describe()
        type_stats = df.groupby('类型')['收益率'].agg(['mean', 'count', 'std'])

        # 生成可视化图表
        plt.figure(figsize=(15, 6))

        plt.subplot(1, 2, 1)
        df['收益率'].plot(kind='hist', bins=20, edgecolor='black')
        plt.title('收益率分布直方图')
        plt.xlabel('收益率')
        plt.ylabel('频数')

        plt.subplot(1, 2, 2)
        df['市场'].value_counts().plot(kind='pie', autopct='%1.1f%%')
        plt.title('市场分布饼图')
        plt.ylabel('')

        plt.tight_layout()
        plt.savefig('stock_analysis.png')
        plt.close()

        # 保存统计结果
        with pd.ExcelWriter('stock_analysis_report.xlsx') as writer:
            pd.DataFrame([stats]).to_excel(writer, sheet_name='总体统计', index=False)
            market_stats.to_excel(writer, sheet_name='市场统计')
            type_stats.to_excel(writer, sheet_name='类型统计')

        print("分析完成！结果已保存到 stock_analysis_report.xlsx")
        print("可视化图表已保存到 stock_analysis.png")

    except Exception as e:
        print(f"发生错误: {str(e)}")


if __name__ == "__main__":
    file_path = "H:\\yield_rate.txt"
    analyze_stock_returns(file_path)
