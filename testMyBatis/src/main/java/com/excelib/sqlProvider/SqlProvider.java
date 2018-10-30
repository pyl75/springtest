package com.excelib.sqlProvider;

import java.util.HashMap;

public class SqlProvider {
    public String getQueryUserSql(HashMap params){
        StringBuilder sql = new StringBuilder();
        Integer id = (Integer) params.get("id");
        sql.append("select * from user where id = ")
                .append(id);
        return sql.toString();
    }
}
