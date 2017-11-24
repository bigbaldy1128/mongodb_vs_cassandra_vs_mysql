package com.wjz.mongodb_vs_cassandra.domain;

import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

public interface DataRepository4Cassandra  extends CassandraRepository<CassandraBugVO> {
    List<CassandraBugVO> findByPkTask(Long pkTask);
    int countByPkTask(long pkTask);
}
