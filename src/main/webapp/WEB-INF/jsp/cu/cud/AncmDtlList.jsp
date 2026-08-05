<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>공지사항 목록</title>
</head>
<body>

<div id="contents">
    <h2>공지사항</h2>
    <ul>
        <c:forEach var="item" items="${resultMap.list}">
            <li>
                <a href="/ancmDtlView.nhd?bbcId=${item.bbcId}">${item.bbcTitl}</a>
            </li>
        </c:forEach>
    </ul>
</div>

</body>
</html>
