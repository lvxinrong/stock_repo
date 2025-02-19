# 股票评分工程

> 本系统一共由3部分组成，分别是python相关的数据获取，java项目对数据进行分析和打分，vue项目进行展示。如果需要运行本系统，需要在本地安装mysql和elasticsearch

之前一直有种想法就是寻找强势股，俗话说该弱不弱，必有一强。按照本人投资经历观察下来，如果一直股票，在某段时间强于大盘或者强于指数（那么就意味着这只股票是可以关注起来的）
那么按照我个人的理解，我选择从指数入手(这里忽略大盘指数，比如沪指和深指这种，重点在沪深300，中证上)，按月为维度，计算该月每天每只成分股和指数之间的差值(股票当日涨跌幅 - 所属指数当日涨跌幅)，并累加获得一个分数。
当然按照这种计算逻辑，很大概率排名靠前的都是当月妖股，这里不作为投资建议(个人会根据计算结果再次做过滤，比如市值小于多少的不看，当月涨跌幅超过多少的不看)

TODO:
1. 后续将增加python一键初始化功能，目前需要单个手动执行对应的py文件进行数据库表的创建和数据的抓取。
2. python在处理累计数据的时候做的不好，后续需要增加更新方式（全量更新还是增量更新）
3. 提供更多的计算策略(最近在思考，可否从成交额占比入手，比如某个板块的成交额在一段时间内不断地增加，或者简单拍个脑袋，如果某个板块成交额大于当日大盘成交额10% 就买入，超过40%就卖出)

# 项目初始化
1. 安装docker
2. 安装 mysql, elasticSearch镜像
3. 安装vue


docker 启动 mysql 命令:
```
docker run -d  --name good_stock -e MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 -v H:\stock\docker_mysql_volume:/var/lib/mysql mysql:latest
```

docker 启动elasticSearch的命令:
```
docker run -d --name es -e "ES_JAVA_OPTS=-Xms1024m -Xmx2048m"  -e "discovery.type=single-node" -e "xpack.security.enabled=false" -e "xpack.security.transport.ssl.enabled=false" -v es-data:\h\stock\docker_elasticsearch_volume\data  -v es-plugins:\h\stock\docker_elasticsearch_volume\plugins  --privileged --network elastic -p 9200:9200 -p 9300:9300 docker.elastic.co/elasticsearch/elasticsearch:8.15.4
```

## python 工程
> python 工程用于初始化数据库表结构和对应的数据抓取，三方数据采用tushare，需要自行获取token。目前尚未做到一件初始化和读取对应数据。

### 主要表结构
index_basic (指数基础信息)
index_basic_daily(指数当日交易表)
stock_basic(股票基础信息)
trade_daily(股票每日交易信息)
hs_300_stock ~ zh_1000_stock（指数成分表，分别统计沪深300， 中证100，中证500， 中证800和中证10000）

## java 工程
> java工程主要用于计算和评估股票表现，并按照自定义逻辑进行打分

## vue工程
前端页面展示

# 效果展示
沪深300:
![image](https://github.com/user-attachments/assets/4cd5b5b8-31c3-4799-84e8-777d2d043c5d)

中证100:
![image](https://github.com/user-attachments/assets/08fe6464-7dbb-42f8-80d4-ab95cfbc328a)

中证500:
![image](https://github.com/user-attachments/assets/0425546f-4c24-4298-b236-09bd020b26f0)





