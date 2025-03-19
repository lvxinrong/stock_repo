# A股复盘分析系统

> 本系统目前包含4个模块，分别是指数行情，涨停行情，资金行情，筹码结构。 系统架构上，使用python调用tushare获取基础数据，数据存储在Mysql中。Java负责读取数据和提供http接口供vue调用。前端使用vue进行展示。<br/>
> 本系统需要使用到mysql和es。

# 涨停行情
![image](https://github.com/user-attachments/assets/b48fbc32-3fd5-40a9-95dd-fe609e44838b)

### 涨停行情模块: 
1. 用于查看当日涨停股的统计信息，用于判断行情情绪，涨停数量越高证明短期情绪越乐观。

### deepseek模块：
接入Deepseek API接口，将当日的涨停股票列表通过DeepSeekAPI接口进行分析，并将分析结果保存在本地，提示词在python中。

# 资金行情
![image](https://github.com/user-attachments/assets/576a370a-61ed-4fab-8a1e-2736d6eb91eb)

资金行情主要分三个二级目录，分别是展示近90日大盘资金流向，当日同花顺板块资金流向， 当日东方财富板块资金流向。可用于板块情绪分析。

# 筹码结构
![image](https://github.com/user-attachments/assets/5acc2585-c86a-4d60-b336-402f33faa8c6)

筹码分布用于展示当日的A股所有股票的筹码结构，和胜率(胜率来自tushare api接口，仅供参考)

# 部署

## 基础环境准备
1. 安装docker
2. 安装mysql, es

## 配置修改
1. 修改python代码和java代码中的mysql配置，java中的es配置也需要配置。
2. 在本地磁盘中创建两个txt文件，用于保存tushare的token和deepseek api的token。目录修改在python代码中。

## 数据初始化
1. python项目中，根目录下有个create_mysql_table.sql 执行一下 库的名称我用的是good_stock
2. python中运行相应表的python代码进行初始化。
3. 后续更新可以创建定时任务，调用update_all_data_bu_day 定时任务设定晚上7点后执行，因为tushare的当日数据一般是下午5点后入库。

## vue项目
npm run dev

## java项目
springboot启动方式。



