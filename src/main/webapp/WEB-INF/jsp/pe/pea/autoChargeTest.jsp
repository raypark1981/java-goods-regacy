<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>자동충전 결제 테스트</title>
</head>
<body>

<h3>자동충전 결제 테스트</h3>
<label>mcustNo: <input type="text" id="mcustNo" value="TEST0001"></label><br>
<label>충전금액(부족금액): <input type="number" id="chargeAmt" value="26200"></label><br>
<button onclick="callAutoCharge()">자동충전 결제 API 호출</button>
<pre id="autoChargeResult"></pre>

<hr>

<h3>자동충전 결제 취소 테스트</h3>
<label>mcustNo: <input type="text" id="cancelMcustNo" value="TEST0001"></label><br>
<label>paymentKey: <input type="text" id="cancelPaymentKey" value="tviva20260816224847g73F5" placeholder="취소할 결제의 paymentKey" size="50"></label><br>
<button onclick="callAutoChargeCancel()">자동충전 결제 취소 API 호출</button>
<pre id="autoChargeCancelResult"></pre>

<script>
    function callAutoCharge() {
        var mcustNo = document.getElementById("mcustNo").value;
        var chargeAmt = document.getElementById("chargeAmt").value;
        var resultEl = document.getElementById("autoChargeResult");
        resultEl.textContent = "호출 중...";

        fetch("/mmall/pe/pea/autoCharge.nhd", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                mcustNo: mcustNo,
                chargeAmt: Number(chargeAmt),
                orderName: "자동충전 테스트"
            })
        })
            .then(function (res) { return res.json(); })
            .then(function (data) { resultEl.textContent = JSON.stringify(data, null, 2); })
            .catch(function (err) { resultEl.textContent = "호출 실패: " + err; });
    }

    function callAutoChargeCancel() {
        var mcustNo = document.getElementById("cancelMcustNo").value;
        var paymentKey = document.getElementById("cancelPaymentKey").value;
        var resultEl = document.getElementById("autoChargeCancelResult");
        resultEl.textContent = "호출 중...";

        fetch("/mmall/pe/pea/autoChargeCancel.nhd", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                mcustNo: mcustNo,
                paymentKey: paymentKey
            })
        })
            .then(function (res) { return res.json(); })
            .then(function (data) { resultEl.textContent = JSON.stringify(data, null, 2); })
            .catch(function (err) { resultEl.textContent = "호출 실패: " + err; });
    }
</script>

</body>
</html>
