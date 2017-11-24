package com.wjz.mongodb_vs_cassandra;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        List<List<?>> bugList=new ArrayList<>();
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
    @Transactional
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
    public void d_mongodbRead() {
        dataRepository4Mongodb.findByPkTask(7L);
        //dataRepository4Mongodb.countByPkTask(7L);
        //dataRepository4Mongodb.countById(499999);
    }

    @Test
    public void e_cassandraRead() {
        dataRepository4Cassandra.findByPkTask(7L);
        //cassandraTemplate.execute("select count(1) from bug where id=1 and pkTask=7");
    }

    @Test
    public void f_mysqlRead() {
        dataMapper4Mysql.queryByPkTask(7L);
        //dataMapper4Mysql.getCount(7L);
    }

    @Test
    public void z_clear() {
        dataRepository4Mongodb.deleteAll();
        dataRepository4Cassandra.deleteAll();
        dataRepository4Mysql.deleteAll();
    }

}
