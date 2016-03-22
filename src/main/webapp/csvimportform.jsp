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

    <form name="csvimportform" action="servlets/CsvImportServlet/" method="post" enctype="multipart/form-data">
     <table>
      <tr>
       <td>Path to CSV file</td>
       <td><input type="file" name="filePath" title="filePath"</td>
      </tr>
      <tr><td colspan="2"><input type="SUBMIT" value="Upload"></td></tr>
     </table>
    </form>

</body>

</html>