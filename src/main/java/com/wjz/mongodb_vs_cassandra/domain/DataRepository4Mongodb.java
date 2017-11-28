package com.wjz.mongodb_vs_cassandra.domain;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DataRepository4Mongodb extends MongoRepository<MongodbBugVO,ObjectId> {
    @Query(value = "{pkTask:?0}")//fields = "{_id:1}"
    List<MongodbBugVO> findByPkTask(long pkTask,Pageable pageable);

    int countByPkTask(long pkTask);
    int countById(long id);
}
