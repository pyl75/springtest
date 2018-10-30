package com.excelib.plugin;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Intercepts({@Signature(type = StatementHandler.class, //确定要拦截的对象
        method = "prepare", //确定要拦截的方法
        args = {Connection.class,Integer.class} //拦截方法的参数
)})
public class PagingPlugin implements Interceptor {
    private Integer defaultPage;//默认页码
    private Integer defaultPageSize;//默认每页条数
    private Boolean defaultUseFlag;//默认是否启用插件
    private Boolean defaultCheckFlag;//默认是否检查当前页码的正确性



    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler stmtHandler = getUnProxyObject(invocation);
        MetaObject metaStatementHandler = SystemMetaObject.forObject(stmtHandler);
        String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        if (!checkSelect(sql)){
            return invocation.proceed();
        }
        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        Object parameterObject = boundSql.getParameterObject();
        PageParams pageParams = getPageParams(parameterObject);
        if (pageParams == null)
            return invocation.proceed();
        //获取分页参数，获取不到的时候使用默认值
        Integer page = pageParams.getPage()==null?this.defaultPage:pageParams.getPage();
        Integer pageSize = pageParams.getPageSize()==null?this.defaultPageSize:pageParams.getPageSize();
        Boolean useFlag = pageParams.getUseFlag() == null?this.defaultUseFlag:pageParams.getUseFlag();
        Boolean checkFlag = pageParams.getCheckFlag()==null?this.defaultCheckFlag:pageParams.getCheckFlag();
        if (!useFlag){
            return invocation.proceed();
        }
        int total = getTotal(invocation, metaStatementHandler, boundSql);
        //回填总数到分页参数里
        setTotalToPageParams(pageParams,total,pageSize);
        //检查当前页码的有效性
        checkPage(checkFlag,page,pageParams.getTotalPage());
        //修改SQL
        return changeSQL(invocation,metaStatementHandler,boundSql,page,pageSize);
    }

    /**
     * 从代理对象中分离出真实对象
     */
    private StatementHandler getUnProxyObject(Invocation invocation) {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        //分离代理对象链（由于目标类可能被多个拦截器拦截，从而形成多次代理，通过循环可以分离出最原始的目标类）
        Object object = null;
        while (metaStatementHandler.hasGetter("h")){
            object = metaStatementHandler.getValue("h");
        }
        if (object==null){
            return statementHandler;
        }
        return (StatementHandler) object;
    }
    /**
     * 判断是否是selec语句
     */
    private boolean checkSelect(String sql){
        String trimSql = sql.trim();
        int idx = trimSql.toLowerCase().indexOf("select");
        return idx == 0;
    }

    /**
     * 获取分页参数
     */
    private PageParams getPageParams(Object parameterObject){
        if (parameterObject==null)
            return null;
        PageParams pageParams = null;
        if (parameterObject instanceof Map){
            Map paramMap = (Map<String,Object>)parameterObject;
            Set<String> keySet = paramMap.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                Object value = paramMap.get(key);
                if (value instanceof PageParams){
                    return (PageParams) value;
                }
            }
        }else if (parameterObject instanceof PageParams){
            pageParams = (PageParams) parameterObject;
        }
        return pageParams;
    }

    /**
     * 获取总数
     * @param ivt
     * @param metaStatementHandler
     * @param boundSql
     * @return
     * @throws SQLException
     */
    private int getTotal(Invocation ivt, MetaObject metaStatementHandler, BoundSql boundSql) throws SQLException {
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        Configuration configuration = mappedStatement.getConfiguration();
        String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        String countSql = "select count(*) as total from ( " +sql+" )$_paging";
        Connection connection = (Connection) ivt.getArgs()[0];
        PreparedStatement ps = null;
        int total = 0;
        try {
            ps = connection.prepareStatement(countSql);
            BoundSql countBoundSql = new BoundSql(configuration, countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            ParameterHandler handler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), countBoundSql);
            handler.setParameters(ps);
            //执行查询
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                total = resultSet.getInt("total");
            }
            return total;
        } finally {
            //这里不能关闭Connection，否则后续的SQL就没法继续了
            if (ps != null){
                ps.close();
            }
        }
    }

    /**
     * 回填总页数和总条数到分页参数
     * @param pageParams
     * @param total
     * @param pageSize
     */
    private void setTotalToPageParams(PageParams pageParams,int total,int pageSize){
        pageParams.setTotal(total);
        int totalPage = total%pageSize==0?total/pageSize:total/pageSize+1;
        pageParams.setTotalPage(totalPage);
    }

    /**
     * 检查当前页码的有效性
     * @param checkFlag
     * @param pageNum
     * @param pageTotal
     */
    private void checkPage(Boolean checkFlag,Integer pageNum,Integer pageTotal){
        if (checkFlag){
            //检查页码page是否合法
            if (pageNum>pageTotal){
                throw new IllegalArgumentException("查询失败，查询页码【"+pageNum+"】大于总页数【"+pageTotal+"】!!");
            }
        }
    }

    /**
     * 修改当前查询的SQL
     * @param invocation
     * @param metaStatementHandler
     * @param boundSql
     * @param page
     * @param pageSize
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    private Object changeSQL(Invocation invocation,MetaObject metaStatementHandler,BoundSql boundSql,int page,int pageSize) throws InvocationTargetException, IllegalAccessException, SQLException {
        String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        //修改SQL，这里使用的是MySQL，如果是其他数据库则需要修改
        String newSql = "select * from ( " +sql+" ) $_paging_table limit ?,?";
        //修改当前需要执行的SQL
        metaStatementHandler.setValue("delegate.boundSql.sql",newSql);
        //相当于套用了StatementHandler的prepare方法，预编译了当前SQL并设置原有的参数，但是少了两个分页参数，它返回的是一个PreParedStatement对象
        PreparedStatement ps = (PreparedStatement) invocation.proceed();
        //计算SQL总参数个数
        int count = ps.getParameterMetaData().getParameterCount();
        ps.setInt(count-1,Math.max(page-1,0)*pageSize);
        ps.setInt(count,pageSize);
        return ps;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler){
            return Plugin.wrap(target,this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties props) {
        defaultPage = Integer.parseInt(props.getProperty("default.page", "1"));
        defaultPageSize = Integer.parseInt(props.getProperty("default.pageSize", "20"));
        defaultUseFlag = Boolean.parseBoolean(props.getProperty("default.useFlag", "false"));
        defaultCheckFlag = Boolean.parseBoolean(props.getProperty("default.checkFlag", "false"));
    }

    public Integer getDefaultPage() {
        return defaultPage;
    }

    public void setDefaultPage(Integer defaultPage) {
        this.defaultPage = defaultPage;
    }

    public Integer getDefaultPageSize() {
        return defaultPageSize;
    }

    public void setDefaultPageSize(Integer defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

    public Boolean getDefaultUseFlag() {
        return defaultUseFlag;
    }

    public void setDefaultUseFlag(Boolean defaultUseFlag) {
        this.defaultUseFlag = defaultUseFlag;
    }

    public Boolean getDefaultCheckFlag() {
        return defaultCheckFlag;
    }

    public void setDefaultCheckFlag(Boolean defaultCheckFlag) {
        this.defaultCheckFlag = defaultCheckFlag;
    }
}
