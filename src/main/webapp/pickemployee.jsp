<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<jsp:useBean id="employee" class="com.bluelight.model.Employee" scope="request"/>
<jsp:setProperty name="employee" property="*"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Get Stock Form</title>
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
        Pick an employee:<br>
    </h2>

<P></P>

    <form name="employeesearchform" action="servlets/PayPeriodServlet/" method="post">
     <table>
      <tr>
       <td>Last Name</td>
       <td><input type="text" name="employeeLastName" title="employeeLastName"</td>
      </tr>
      <tr><td colspan="2"><input type="SUBMIT" value="Get Values"></td></tr>
     </table>
    </form>
</body>
</html>