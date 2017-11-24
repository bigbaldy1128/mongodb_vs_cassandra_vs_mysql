# 测试结果(绿色代表最快，红色代表最慢)
## 单机测试

| 数据库    |   写  |  读  |  count  | 文档 | 编码难易度 | 集群环境配置 |
| --------    | :-----:  | :----:   |:----:   | :----:   |:----:   |:----:|
|  MongoDB    | 19.1s     |   2.8s | 129ms | 非常详尽 | 完全使用JPA，极其简单| 比较麻烦 |
|   Mysql     |<font color=red>69s</font>     |   <font color=green>1.3s</font>  |  <font color=green>115ms</font> | 非常详尽 | 可以使用JPA，但批量写入性能极差| N/A|
|  Cassandra  |<font color=green>9s</font>    |  <font color=red>8.3s</font>      | <font color=red>2.4s</font> | 一般 | 可以使用JPA，但批量写入性能较差| 极其简单|
## 集群测试
| 数据库    |   写  |  读  | 数据分布                             |count| 数据迁移 |
| --------    | :-----:  | :----: |:-----:                      | :-----:|:-----:|
|  MongoDB    |  <font color=red>24.2s</font>     |   <font color=green>2.8s</font> |  小数据不均匀，大数据均匀   |<font color=green>120ms</font>| 录入数据时自动平衡节点数据|
|  Cassandra  |  <font color=green>13.7s</font>   |  <font color=red>10.2s</font> | 非常均匀                    |<font color=red>2.3s</font>| 新节点上线后自动平衡节点数据|
## 测试说明
* 数据总量：50万
* "读"是根据pkTask字段进行的查询，由于返回数据量巨大，索引对速度几乎没有影响，性能瓶颈在网络传输上
* count是根据pkTask字段进行数量统计，由于返回数据量很小，索引对性能的影响就非常明显
* mongodb若使用hashed分片方式，数据存储将非常均匀，但写入速度大幅下降
* cassandra进行条件查询时，若条件字段不是主键则必须配置Secondary Index，否则不能进行条件查询，非主键即使加了索引查询速度也很慢，主键查询瞬间返回
* cassandra的读性能还需继续调研
# 数据库具体配置
## MongoDB
* 节点数量：3
* 分片算法：Ranged，分片键：id
* config节点使用单节点复制集，分片节点没有使用复制集，分片复制集配置需要至少9台机器
* 索引：pkTask
* 写入方式：JPA默认的insert方法

## Mysql
* 节点数量：1
* 写入方式：使用Mybatis拼接insert语句
* 建表语句：
```sql
CREATE TABLE "chk_res_bug" (
  "id" int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  "pkTask" int(11) DEFAULT NULL COMMENT '检测任务',
  "engine_id" varchar(100) DEFAULT NULL COMMENT '引擎信息',
  "bug_id" varchar(100) DEFAULT NULL COMMENT '缺陷ID',
  "bug_level" tinyint(4) DEFAULT NULL COMMENT '缺陷等级\r\n            1 低\r\n            3 中\r\n            5 高',
  "rule_code" varchar(100) DEFAULT NULL COMMENT '规则编码',
  "standard_name" varchar(100) DEFAULT NULL COMMENT '规范名称（一级）',
  "rule_type" varchar(100) DEFAULT NULL COMMENT '规则类型名称（二级）',
  "rule_name" varchar(200) DEFAULT NULL COMMENT '规则名称（三级）',
  "file_path" varchar(2000) DEFAULT NULL COMMENT '文件路径',
  "file_type" varchar(100) DEFAULT NULL COMMENT '文件类型',
  "file_encoding" varchar(100) DEFAULT NULL COMMENT '文件编码',
  "bug_file" varchar(2000) DEFAULT NULL COMMENT '缺陷文件',
  "bug_file_md5" varchar(500) DEFAULT NULL,
  "bug_func" text COMMENT '缺陷所在方法',
  "bug_begin_line" int(11) DEFAULT NULL COMMENT '缺陷行起始',
  "bug_begin_col" int(11) DEFAULT NULL COMMENT '缺陷列起始',
  "bug_end_line" int(11) DEFAULT NULL COMMENT '缺陷行结束',
  "bug_end_col" int(11) DEFAULT NULL COMMENT '缺陷列结束',
  "audit_result" tinyint(4) DEFAULT NULL COMMENT '审计结果\n            0 未审计\n            1 低\n            3 中\n            5 高\n            6 不是问题\n            7 确认\n            8 忽略',
  "audit_memo" varchar(500) DEFAULT NULL,
  "pk_user" int(11) DEFAULT NULL COMMENT '审计人',
  "audit_time" int(11) DEFAULT NULL COMMENT '审计时间',
  "bug_url" varchar(500) DEFAULT NULL COMMENT '缺陷跟踪平台bug链接信息',
  "svn_git_info" varchar(500) DEFAULT NULL COMMENT 'SVNGIT信息',
  "tool" varchar(500) DEFAULT NULL COMMENT '检测工具\r\n            Ft:1,Sky:1,Cx:1\r\n            ',
  "spoint" varchar(32) DEFAULT NULL,
  "bug_status" tinyint(4) DEFAULT NULL COMMENT '9 遗留\n',
  PRIMARY KEY ("id"),
  KEY "pkTaskIndex" ("pkTask")
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='检测结果';

```

## Cassandra
* 节点数量：3
* 写入方式：ingest
* 节点状态：

|--|Address|        Load   |    Tokens |      Owns (effective) | Host ID                            |  Rack|
|--|-------|:----:|:----:|:----:|:----:|:----:|
|UN  |172.24.62.162  |278.63 KiB |  256|          33.6%|             b5f650fa-1257-4d30-ac95-99e963a65156|  rack1|
|UN  |172.24.62.181  |242.98 KiB  |  256|          35.3%|             76d6000a-61de-4daa-b366-4ee10cc3674f|  rack1|
|UN  |172.24.62.166  | 328.43 KiB|  256 |         31.0%|             a7810e04-9774-47e0-8ebc-e045e29f29c7|  rack1|
* 新建keyspace：
```sql
CREATE KEYSPACE test WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};
```
* 建表：
```sql
CREATE TABLE bug (
    id bigint PRIMARY KEY,
    pkTask bigint,
    engine_id varchar,
    bug_id varchar,
    bug_level int,
    rule_code varchar,
    standard_name varchar,
    rule_type varchar,
    rule_name varchar,
    file_path varchar,
    file_type varchar,
    file_encoding varchar,
    bug_file varchar,
    bug_file_md5 varchar,
    bug_func varchar,
    bug_begin_line bigint,
    bug_begin_col bigint,
    bug_end_line bigint,
    bug_end_col bigint,
    audit_result int,
    audit_memo varchar,
    pk_user bigint,
    audit_time bigint,
    bug_url varchar,
    svn_git_info varchar,
    tool varchar,
    spoint varchar,
    bug_status int,
    PRIMARY KEY ((id, pkTask))
);
```
* 添加索引：
```sql
create index pkTaskIndex on bug(pkTask);
```