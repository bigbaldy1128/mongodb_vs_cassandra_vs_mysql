# 测试结果(绿色代表最快，红色代表最慢)
| 数据库    |数据库类型|   写  |  读  | 聚合函数 |数据分布| 数据迁移 | 查询功能
|------|:-----:|:-----:| :----: |:-----: | :-----:|:-----:|:-----:|
|MongoDB|Document store|24.2s|1.5s|120ms|小数据不均匀，大数据均匀|录入数据时自动平衡节点数据|功能全面，性能较好|
|Cassandra|Wide column store|<font color=green>10s</font>|<font color=red>3.9s</font>|<font color=red>2.3s</font>|非常均匀|新节点上线后自动平衡节点数据|功能有限，性能较差|
|Mysql|RDBMS|<font color=red>69s</font>|<font color=green>1.3s</font>|<font color=green>113ms</font>|N/A|N/A|功能全面，性能优异|
## 测试说明
* 数据总量：50万
* "读"是根据pkTask字段进行的查询
* “聚合函数”是进行count操作，索引对性能的影响非常明显
* Mongodb若使用hashed分片方式，数据存储将非常均匀，但写入速度大幅下降
* Cassandra进行条件查询时，性能与数据大小成正比，聚合操作性能很差，查询与聚合功能都十分有限
* Mongodb与Cassandra集群性能都要低于单机
* JPA方式操作总体性能都较差，以上测试结果都是采用我目前已知的最快方式得出的结果
* **Cassandra插入50万数据，但查出的数据小于50万，会有几万数据丢失，原因暂时不清**
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