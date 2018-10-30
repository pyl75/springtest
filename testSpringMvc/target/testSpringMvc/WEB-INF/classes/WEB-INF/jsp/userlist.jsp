<%--
  Created by IntelliJ IDEA.
  User: pengyunlong
  Date: 2018/10/21
  Time: 18:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" isELIgnored="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h2> This is SpringMVC demo page</h2>
<c:forEach items="${users}" var="user">
    <c:out value="${user.username}"/><br/>
    <c:out value="${user.age}"/><br/>
</c:forEach>