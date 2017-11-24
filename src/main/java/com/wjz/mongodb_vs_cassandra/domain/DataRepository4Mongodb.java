package com.wjz.mongodb_vs_cassandra.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DataRepository4Mongodb extends MongoRepository<MongodbBugVO,ObjectId> {
    List<MongodbBugVO> findByPkTask(long pkTask);
    int countByPkTask(long pkTask);
    int countById(long id);
}
