<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="${resultMap.globLang}">
<head>
    <meta charset="UTF-8">
    <title>공지사항 상세</title>
</head>
<body>

<%-- 공통 header include (운영에서는 /WEB-INF/jsp/common/include/header.jsp) --%>
<%-- <jsp:include page="/WEB-INF/jsp/common/include/header.jsp"/> --%>

<div id="contents">
    <article>
        <h2>${resultMap.bbcTitl}</h2>
        <div id="ancmCntn">
            ${resultMap.bbcCntn}
        </div>
    </article>
</div>

<%-- 공통 footer include --%>
<%-- <jsp:include page="/WEB-INF/jsp/common/include/footer.jsp"/> --%>

</body>
</html>
