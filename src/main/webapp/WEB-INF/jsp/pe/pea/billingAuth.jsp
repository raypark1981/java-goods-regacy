<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>빌링키 발급 테스트</title>
    <script src="https://js.tosspayments.com/v2/standard"></script>
</head>
<body>

<button id="billingAuthBtn">계좌 등록하기 (빌링 인증)</button>

<script>
    var clientKey = "test_ck_oEjb0gm23PwN09yJm7Rg8pGwBJn5";
    var customerKey = "customer_" + Date.now();

    var tossPayments = TossPayments(clientKey);
    var payment = tossPayments.payment({ customerKey: customerKey });

    document.getElementById("billingAuthBtn").addEventListener("click", function () {
        payment.requestBillingAuth({
            method: "TRANSFER",
            successUrl: window.location.origin + "/mmall/pe/pea/billingAuthResult.nhd",
            failUrl: window.location.origin + "/mmall/pe/pea/billingAuthResult.nhd",
            customerEmail: "test@example.com",
            customerName: "테스트"
        });
    });
</script>

</body>
</html>
