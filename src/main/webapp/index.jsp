<html>
<body>

    <script src="https://code.jquery.com/jquery-1.10.2.js"></script>
    <script>
    $.post("servlets/PayrollServlet/", {param:param})
    </script>

<%--  onsubmit="post();" --%>

<form name="getAll" action="servlets/PayrollServlet/" method="post">
    <input type="submit" value="Get Values">
    <input type="HIDDEN" name="submit" value="true">
</form>
<%@include file="employees.jsp" %>

</body>
</html>
