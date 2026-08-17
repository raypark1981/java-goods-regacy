<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>빌링 인증 결과</title>
</head>
<body>

<h3>빌링 인증 결과</h3>
<ul>
    <li>customerKey: ${param.customerKey}</li>
    <li>authKey: ${param.authKey}</li>
    <li>code: ${param.code}</li>
    <li>message: ${param.message}</li>
</ul>

<h3>빌링키 발급 결과</h3>
<p>${issueMessage}</p>

<button onclick="location.href='/mmall/pe/pea/billingAuthPage.nhd'">재시도</button>

<hr>

<h3>자동충전 결제 테스트</h3>

    <button onclick="location.href='/mmall/pe/pea/autoChargeTest.nhd'">자동충전 결제 테스트 화면으로 이동</button>

</body>
</html>
