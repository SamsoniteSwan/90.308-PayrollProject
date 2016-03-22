<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="payperiods" class="java.util.ArrayList" scope="session">
    <c:set target='${payperiods}'  value='${sessionScope.get("payperiods")}'/>
</jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Pay Periods</title>
    <style>
        div.space {
            line-height: 2;
        }
        table { text-align: right; }

        td.center { text-align: center; }

    </style>
</head>
<body>
<%@include file="navigator.jsp" %>
<h2>
    Filter
</h2>
<table style="width:100%">
    <th>start</th>
    <th>end</th>
    <th>hourly rate</th>
    <th>gross Pay</th>
    <th>Taxes Withheld</th>
    <th>Take Home Pay</th>

<c:forEach items="${payperiods}" var="payperiod">
    <tr>
        <td><c:out value="${payperiod.getStartDay()}"/></td>
        <td><c:out value="${payperiod.getEndDay()}"/></td>
        <td><c:out value="${payperiod.getHourlyRate()}"/></td>
        <td><c:out value="${payperiod.grossPay()}"/></td>
        <td><c:out value="${payperiod.taxesWithheld()}"/></td>
        <td><c:out value="${payperiod.takeHomePay()}"/></td>



    </tr>
</c:forEach>
</table>

<P>
</P>

</body>
</html>
</body>
