package com.wjz.mongodb_vs_cassandra.domain;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper
@Transactional
public interface DataMapper4Mysql {
    void addBugs(@Param("bugList") List<MysqlBugVO> bugList);
    List<MysqlBugVO> queryByPkTask(@Param("pkTask") Long pkTask);
    int getCount(@Param("pkTask") Long pkTask);
}
