package com.wjz.mongodb_vs_cassandra.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataRepository4Mysql extends JpaRepository<MysqlBugVO,Long> {
}
