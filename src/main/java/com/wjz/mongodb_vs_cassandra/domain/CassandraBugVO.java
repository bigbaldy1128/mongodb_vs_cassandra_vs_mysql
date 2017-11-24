package com.wjz.mongodb_vs_cassandra.domain;

import lombok.Data;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.Date;

@Data
@Table("bug")
public class CassandraBugVO {
    @PrimaryKey
    private Long id;

    private Long pkTask;
    private String engine_id;
    private String bug_id;
    private int bug_level;
    private String rule_code;
    private String standard_name;
    private String rule_type;
    private String rule_name;
    private String file_path;
    private String file_type;
    private String file_encoding;
    private String bug_file;
    private String bug_file_md5;
    private String bug_func;
    private Long bug_begin_line;
    private Long bug_begin_col;
    private Long bug_end_line;
    private Long bug_end_col;
    private int audit_result;
    private String audit_memo;
    private Long pk_user;
    private Long audit_time;
    private String bug_url;
    private String svn_git_info;
    private String tool;
    private String spoint;
    private int bug_status;
}
