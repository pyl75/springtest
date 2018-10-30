package com.excelib;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

public class CustomDatabaseIdProvider implements DatabaseIdProvider {
    private Properties properties;

    @Override
    public void setProperties(Properties p) {
        this.properties = p;
    }

    @Override
    public String getDatabaseId(DataSource dataSource) throws SQLException {
        String dbType = null;
        if (properties!=null){
            dbType = properties.getProperty("dbType");
        }
        if (!StringUtils.hasText(dbType)){
            dbType = dataSource.getConnection().getMetaData().getDatabaseProductName();
        }
        String dbId = (String) this.properties.get(dbType);
        return dbId;
    }
}
