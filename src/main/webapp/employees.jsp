<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="employees" class="java.util.ArrayList" scope="session">
    <c:set target='${employees}'  value='${sessionScope.get("employees")}'/>
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
    <th>Employee ID</th>
    <th>First Name</th>
    <th>Last Name</th>
    <th>Vacation Balance</th>

<c:forEach items="${employees}" var="employee">
    <tr>

        <td><c:out value="${employee.getEmployeeId()}"/></td>
        <td><c:out value="${employee.getFirstName()}"/></td>
        <td><c:out value="${employee.getLastName()}"/></td>
        <td><c:out value="${employee.getVacationBalance()}"/></td>
    </tr>
</c:forEach>
</table>

<P>
</P>

</body>
</html>
</body>
