package com.wjz.mongodb_vs_cassandra;

import com.datastax.driver.core.Row;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.wjz.mongodb_vs_cassandra.domain.*;
import lombok.val;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
public class MongodbVsCassandraApplicationTests {
    private final static long COUNT = 500000;

    @Autowired
    private DataRepository4Mongodb dataRepository4Mongodb;

    @Autowired
    private DataRepository4Cassandra dataRepository4Cassandra;

    @Autowired
    private DataRepository4Mysql dataRepository4Mysql;

    @Autowired
    private CassandraTemplate cassandraTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DataMapper4Mysql dataMapper4Mysql;

    @Test
    public void a_mongodbWriteTest() throws JsonProcessingException {
        List<MongodbBugVO> bugVO4MongodbList = new ArrayList<>();
        for (long i = 1; i <= COUNT; i++) {
            MongodbBugVO bugVO = new MongodbBugVO();
            bugVO.setId(i);
            bugVO.setPkTask(i % 10);
            bugVO.setEngine_id("structural");
            bugVO.setBug_id("F5BE2EA500E3150C29056666D08955BC");
            bugVO.setBug_level(1);
            bugVO.setRule_code("02000010110031");
            bugVO.setStandard_name("Sky规范");
            bugVO.setRule_type("代码质量");
            bugVO.setRule_name("遗留的调试信息");
            bugVO.setFile_path("0");
            bugVO.setFile_type("java");
            bugVO.setFile_encoding("UTF-8");
            bugVO.setBug_file("test/src/com/company/Main.java");
            bugVO.setBug_file_md5("e904e20ee42e301817f68ad37a56b805");
            bugVO.setBug_func("Main");
            bugVO.setBug_begin_line(5L);
            bugVO.setBug_begin_col(24L);
            bugVO.setBug_end_line(11L);
            bugVO.setBug_end_col(0L);
            bugVO.setAudit_result(3);
            bugVO.setPk_user(2L);
            bugVO.setAudit_time(1234567L);
            bugVO.setTool("Sky");
            bugVO.setSpoint("9b8bb3de4dec3fc68fcaff4a815133ae");
            bugVO.setBug_status(3);
            bugVO4MongodbList.add(bugVO);
        }
        dataRepository4Mongodb.insert(bugVO4MongodbList);
    }

    @Test
    public void b1_cassandraWriteTest() {
        String insertPreparedCql = "insert into bug (" +
                "id," +
                "pkTask," +
                "engine_id," +
                "bug_id," +
                "bug_level," +
                "rule_code," +
                "standard_name," +
                "rule_type," +
                "rule_name," +
                "file_path," +
                "file_type," +
                "file_encoding," +
                "bug_file," +
                "bug_file_md5," +
                "bug_func," +
                "bug_begin_line," +
                "bug_begin_col," +
                "bug_end_line," +
                "bug_end_col," +
                "audit_result," +
                "pk_user," +
                "audit_time," +
                "tool," +
                "spoint," +
                "bug_status) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        List<List<?>> bugList = new ArrayList<>();
        for (long i = 1; i <= COUNT; i++) {
            List<Object> singleBug = new ArrayList<>();
            singleBug.add(i);
            singleBug.add(i % 10);
            singleBug.add("structural");
            singleBug.add("F5BE2EA500E3150C29056666D08955BC");
            singleBug.add(1);
            singleBug.add("02000010110031");
            singleBug.add("Sky规范");
            singleBug.add("代码质量");
            singleBug.add("遗留的调试信息");
            singleBug.add("0");
            singleBug.add("java");
            singleBug.add("UTF-8");
            singleBug.add("test/src/com/company/Main.java");
            singleBug.add("e904e20ee42e301817f68ad37a56b805");
            singleBug.add("Main");
            singleBug.add(5L);
            singleBug.add(24L);
            singleBug.add(11L);
            singleBug.add(0L);
            singleBug.add(3);
            singleBug.add(2L);
            singleBug.add(1234567L);
            singleBug.add("Sky");
            singleBug.add("9b8bb3de4dec3fc68fcaff4a815133ae");
            singleBug.add(3);
            bugList.add(singleBug);
        }
        cassandraTemplate.ingest(insertPreparedCql, bugList);
    }

    //@Test
    public void b2_cassandraWriteTest() {
        List<CassandraBugVO> bugVO4CassandraList = new ArrayList<>();
        for (long i = 1; i <= COUNT; i++) {
            CassandraBugVO bugVO = new CassandraBugVO();
            bugVO.setId(i);
            bugVO.setPkTask(i % 10);
            bugVO.setEngine_id("structural");
            bugVO.setBug_id("F5BE2EA500E3150C29056666D08955BC");
            bugVO.setBug_level(1);
            bugVO.setRule_code("02000010110031");
            bugVO.setStandard_name("Sky规范");
            bugVO.setRule_type("代码质量");
            bugVO.setRule_name("遗留的调试信息");
            bugVO.setFile_path("0");
            bugVO.setFile_type("java");
            bugVO.setFile_encoding("UTF-8");
            bugVO.setBug_file("test/src/com/company/Main.java");
            bugVO.setBug_file_md5("e904e20ee42e301817f68ad37a56b805");
            bugVO.setBug_func("Main");
            bugVO.setBug_begin_line(5L);
            bugVO.setBug_begin_col(24L);
            bugVO.setBug_end_line(11L);
            bugVO.setBug_end_col(0L);
            bugVO.setAudit_result(3);
            bugVO.setPk_user(2L);
            bugVO.setAudit_time(1234567L);
            bugVO.setTool("Sky");
            bugVO.setSpoint("9b8bb3de4dec3fc68fcaff4a815133ae");
            bugVO.setBug_status(3);
            bugVO4CassandraList.add(bugVO);
        }
        List<CassandraBugVO> list = new ArrayList<>();
        for (val v : bugVO4CassandraList) {
            list.add(v);
            if (list.size() % 20 == 0) {
                cassandraTemplate.insert(list);
                list.clear();
            }
        }
    }

    @Test
    public void c_mysqlWriteTest() {
        List<MysqlBugVO> bugVO4MysqlList = new ArrayList<>();
        for (long i = 1; i <= COUNT; i++) {
            MysqlBugVO bugVO = new MysqlBugVO();
            bugVO.setId(i);
            bugVO.setPkTask(i % 10);
            bugVO.setEngine_id("structural");
            bugVO.setBug_id("F5BE2EA500E3150C29056666D08955BC");
            bugVO.setBug_level(1);
            bugVO.setRule_code("02000010110031");
            bugVO.setStandard_name("Sky规范");
            bugVO.setRule_type("代码质量");
            bugVO.setRule_name("遗留的调试信息");
            bugVO.setFile_path("0");
            bugVO.setFile_type("java");
            bugVO.setFile_encoding("UTF-8");
            bugVO.setBug_file("test/src/com/company/Main.java");
            bugVO.setBug_file_md5("e904e20ee42e301817f68ad37a56b805");
            bugVO.setBug_func("Main");
            bugVO.setBug_begin_line(5L);
            bugVO.setBug_begin_col(24L);
            bugVO.setBug_end_line(11L);
            bugVO.setBug_end_col(0L);
            bugVO.setAudit_result(3);
            bugVO.setPk_user(2L);
            bugVO.setAudit_time(1234567L);
            bugVO.setTool("Sky");
            bugVO.setSpoint("9b8bb3de4dec3fc68fcaff4a815133ae");
            bugVO.setBug_status(3);
            bugVO4MysqlList.add(bugVO);
            if (i % 10000 == 0) {
                dataMapper4Mysql.addBugs(bugVO4MysqlList);
                bugVO4MysqlList.clear();
            }
        }
    }

    @Test
    public void d_mongodbRead() throws Exception {
        //dataRepository4Mongodb.countByPkTask(7L);
        //dataRepository4Mongodb.countById(499999);
        mongoB();
    }

    private void mongoA(){
        long start= System.currentTimeMillis();
        dataRepository4Mongodb.findByPkTask(7L,null);
        long end= System.currentTimeMillis();
        System.out.println("timeA:"+(end-start));
    }


    private void mongoB(){
        long start= System.currentTimeMillis();
        BasicDBObject query = new BasicDBObject();
        BasicDBObject fields = new BasicDBObject();
        query.put("pkTask", 7);
        fields.put("_id", 1);
        val cursor = mongoTemplate.getCollection("bug").find(query);
        List<MongodbBugVO> dbObjectList = new ArrayList<>();
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            MongodbBugVO mongodbBugVO = new MongodbBugVO();
            mongodbBugVO.setId((Long) dbObject.get("id"));
            mongodbBugVO.setPkTask((Long) dbObject.get("pkTask"));
            mongodbBugVO.setEngine_id((String) dbObject.get("engine_id"));
            mongodbBugVO.setBug_id((String) dbObject.get("bug_id"));
            mongodbBugVO.setBug_level((int)dbObject.get("bug_level"));
            mongodbBugVO.setRule_code((String) dbObject.get("rule_code"));
            mongodbBugVO.setStandard_name((String) dbObject.get("standard_name"));
            mongodbBugVO.setRule_type((String) dbObject.get("rule_type"));
            mongodbBugVO.setFile_path((String) dbObject.get("file_path"));
            mongodbBugVO.setFile_type((String) dbObject.get("file_type"));
            mongodbBugVO.setFile_encoding((String) dbObject.get("file_encoding"));
            mongodbBugVO.setBug_file((String) dbObject.get("bug_file"));
            mongodbBugVO.setBug_file_md5((String) dbObject.get("bug_file_md5"));
            mongodbBugVO.setBug_func((String) dbObject.get("bug_func"));
            mongodbBugVO.setBug_begin_line((Long)dbObject.get("bug_begin_line"));
            mongodbBugVO.setBug_begin_col((Long)dbObject.get("bug_begin_col"));
            mongodbBugVO.setBug_end_line((Long)dbObject.get("bug_end_line"));
            mongodbBugVO.setBug_end_col((Long)dbObject.get("bug_end_col"));
            mongodbBugVO.setAudit_result((int)dbObject.get("audit_result"));
            mongodbBugVO.setPk_user((Long)dbObject.get("pk_user"));
            mongodbBugVO.setAudit_time((Long)dbObject.get("audit_time"));
            mongodbBugVO.setTool((String) dbObject.get("tool"));
            mongodbBugVO.setSpoint((String) dbObject.get("spoint"));
            mongodbBugVO.setBug_status((int)dbObject.get("bug_status"));
            dbObjectList.add(mongodbBugVO);
        }
        long end= System.currentTimeMillis();
        System.out.println("timeB:"+(end-start));
    }

    @Test
    public void e_cassandraRead() throws Exception {
        //dataRepository4Cassandra.findByPkTask(7L);
        val rs = cassandraTemplate.executeAsynchronously("select * from bug where pkTask=7").get();
        val iterator = rs.iterator();
        List<CassandraBugVO> list = new ArrayList<>();
        Set<Long> set = new HashSet<>();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            CassandraBugVO cassandraBugVO = new CassandraBugVO();
            Long id = row.get("id", Long.class);
            cassandraBugVO.setId(id);
            //set.add(id);
            cassandraBugVO.setPkTask(row.get("pkTask",Long.class));
            cassandraBugVO.setEngine_id(row.get("engine_id",String.class));
            cassandraBugVO.setBug_id(row.get("bug_id",String.class));
            cassandraBugVO.setBug_level(row.get("bug_level",int.class));
            cassandraBugVO.setRule_code(row.get("rule_code",String.class));
            cassandraBugVO.setStandard_name(row.get("standard_name",String.class));
            cassandraBugVO.setRule_type(row.get("rule_type",String.class));
            cassandraBugVO.setRule_name(row.get("rule_name",String.class));
            cassandraBugVO.setFile_path(row.get("file_path",String.class));
            cassandraBugVO.setFile_type(row.get("file_type",String.class));
            cassandraBugVO.setFile_encoding(row.get("file_encoding",String.class));
            cassandraBugVO.setBug_file(row.get("bug_file",String.class));
            cassandraBugVO.setBug_file_md5(row.get("bug_file_md5",String.class));
            cassandraBugVO.setBug_func(row.get("bug_func",String.class));
            cassandraBugVO.setBug_begin_line(row.get("bug_begin_line",Long.class));
            cassandraBugVO.setBug_begin_col(row.get("bug_begin_col",Long.class));
            cassandraBugVO.setBug_end_line(row.get("bug_end_line",Long.class));
            cassandraBugVO.setBug_end_col(row.get("bug_end_col",Long.class));
            cassandraBugVO.setAudit_result(row.get("audit_result",int.class));
            cassandraBugVO.setPk_user(row.get("pk_user",Long.class));
            cassandraBugVO.setAudit_time(row.get("audit_time",Long.class));
            cassandraBugVO.setTool(row.get("tool",String.class));
            cassandraBugVO.setSpoint(row.get("spoint",String.class));
            cassandraBugVO.setBug_status(row.get("bug_status",int.class));
            list.add(cassandraBugVO);
        }
//        for (Long i = 1L; i <= COUNT; i++) {
//            if (!set.contains(i)) {
//                System.out.println(i);
//            }
//        }
//        MappingManager mappingManager = new MappingManager(cassandraTemplate.getSession());
//        val cassandraBugVOList = mappingManager.mapper(CassandraBugVO.class).map(rs).all();
    }

    @Test
    public void f_mysqlRead() {
        //dataMapper4Mysql.queryByPkTask(7L);
        dataMapper4Mysql.getCount(7L);
    }

    @Test
    public void z_clear() {
        dataRepository4Mongodb.deleteAll();
        dataRepository4Cassandra.deleteAll();
        dataRepository4Mysql.deleteAll();
    }

}
