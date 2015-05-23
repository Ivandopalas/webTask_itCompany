
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<head>
    <link rel="stylesheet" type="text/css" href="css/mainStyle.css">
    <link rel="stylesheet" type="text/css" href="css/buttonLoc.css">

    <script src="js/validateInput.js" type="text/javascript"></script>

    <c:choose>
        <c:when test="${sessionScope.local != null}">
            <fmt:setLocale value="${sessionScope.local}" />
        </c:when>
        <c:otherwise>
            <fmt:setLocale value="ru" />
        </c:otherwise>
    </c:choose>
    <fmt:setBundle basename="local" var="loc" />
    <c:if test="${not empty requestScope.log_error}">
        <fmt:message bundle="${loc}" key="${requestScope.log_error}" var="logingErrorMessage" />
    </c:if>
    <fmt:message bundle="${loc}" key="local.action.buttonRU" var="ru_button"/>
    <fmt:message bundle="${loc}" key="local.action.buttonEN" var="en_button"/>
    <fmt:message bundle="${loc}" key="local.text.header.login" var="header_login"/>
    <fmt:message bundle="${loc}" key="local.text.subheader.login" var="subheader_login"/>
    <fmt:message bundle="${loc}" key="local.text.table.login" var="table_login"/>
    <fmt:message bundle="${loc}" key="local.text.table.password" var="table_password"/>
    <fmt:message bundle="${loc}" key="local.text.table.button.login" var="login_button"/>
    <fmt:message bundle="${loc}" key="local.text.table.button.registration" var="registration_button"/>
</head>

<body>
<tabel align="right">
    <tr><td>
        <form action="/controller" method="post" >
            <input type="hidden" name="action" value="change_local">
            <input type="hidden" name="local" value="ru" />
            <input type="hidden" name="page_name" value="index.jsp">
            <input type="submit" value="${ru_button}" class="buttonLoc"/>
            <br />
        </form>
    </td></tr>
    <tr><td>
    <form action="/controller" method="post" >
        <input type="hidden" name="action" value="change_local">
        <input type="hidden" name="local" value="en" />
        <input type="hidden" name="page_name" value="index.jsp">
        <input type="submit" value="${en_button}" class="buttonLoc" />
        <br />
    </form>
    </td></tr>
</tabel>
<form action="<%=request.getContextPath()%>/controller" method="post" class="bootstrap-frm">
    <h1>${header_login}
        <span>${subheader_login}</span>
    </h1>
    <input type="hidden" name="action"  value="login" />
    <label>
        <span>${table_login}</span>
        <input type="text"  oninput="valid(this)" name="login"/>
    </label>
    <label>
        <span>${table_password}</span>
        <input type="password" name="password"/>
    </label>
    <div align="center"> <span style="color: red;align-content: center">${logingErrorMessage}</span></div>
    <table align="center" border="0">
        <tr>
            <td><input type="submit" value=${login_button} class="button"/></td>
            <td><input type="button" onclick="parent.location = 'registration.jsp'" value=${registration_button} class="button"></td>
        </tr>
    </table>
</form>

</body>
</html>
