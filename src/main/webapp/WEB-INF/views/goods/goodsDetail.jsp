<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>${goods.gdNm}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/goods.css">
</head>
<body>

<div class="goods-detail">
    <c:choose>
        <c:when test="${not empty goods}">
            <h1>${goods.gdNm}</h1>
            <p class="price">
                <fmt:formatNumber value="${goods.gdPrice}" type="number"/> 원
            </p>
            <c:if test="${not empty goods.gdImgUrl}">
                <img src="${goods.gdImgUrl}" alt="${goods.gdNm}">
            </c:if>
            <div class="desc">${goods.gdDesc}</div>
        </c:when>
        <c:otherwise>
            <p>상품 정보를 찾을 수 없습니다.</p>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>
