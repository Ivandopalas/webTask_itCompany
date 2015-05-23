
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
  <fmt:setBundle basename="local" var="loc"/>
  <c:if test="${not empty requestScope.reg_error}">
    <fmt:message bundle="${loc}" key="${requestScope.reg_error}" var="regErrorMessage" />
  </c:if>
  <fmt:message bundle="${loc}" key="local.action.buttonRU" var="ru_button"/>
  <fmt:message bundle="${loc}" key="local.action.buttonEN" var="en_button"/>
  <fmt:message bundle="${loc}" key="local.text.table.button.registration" var="registration_button"/>
  <fmt:message bundle="${loc}" key="local.text.header.registration" var="header_registration"/>
  <fmt:message bundle="${loc}" key="local.text.subheader.registration" var="subheader_registration"/>
  <fmt:message bundle="${loc}" key="local.text.table.login" var="login"/>
  <fmt:message bundle="${loc}" key="local.text.table.password" var="password"/>
  <fmt:message bundle="${loc}" key="local.text.table.name" var="name"/>
  <fmt:message bundle="${loc}" key="local.text.table.email" var="email"/>
</head>

<body>
<tabel align="right">
  <tr><td>
    <form action="<%=request.getContextPath()%>/controller" method="post" >
      <input type="hidden" name="action" value="change_local">
      <input type="hidden" name="local" value="ru" />
      <input type="hidden" name="page_name" value="registration.jsp">
      <input type="submit" value="${ru_button}" class="buttonLoc"/>
      <br />
    </form>
  </td></tr>
  <tr><td>
    <form action="<%=request.getContextPath()%>/controller" method="post" >
      <input type="hidden" name="action" value="change_local">
      <input type="hidden" name="local" value="en" />
      <input type="hidden" name="page_name" value="registration.jsp">
      <input type="submit" value="${en_button}" class="buttonLoc" />
      <br />
    </form>
  </td></tr>
</tabel>
<form action="<%=request.getContextPath()%>/controller" method="post" class="bootstrap-frm">
  <h1>${header_registration}
    <span>${subheader_registration}</span>
  </h1>
  <input type="hidden" name="action" value="registration" />
  <label>
    <span>${login}</span>
    <input type="text" oninput="valid(this)" name="login" />
  </label>
  <label>
    <span>${password}</span>
    <input type="password" name="password"/>
  </label>
  <label>
    <span>${name}</span>
    <input type="text" oninput="valid(this)" name="client_name"/>
  </label>
  <label>
    <span>${email}</span>
    <input type="email" oninput="valid(this)" name="email"/>
  </label>
  <div align="center"> <span style="color: red;align-content: center">${regErrorMessage}</span></div>
  <table align="center" border="0">
    <tr>
      <td><input type="submit" value="${registration_button}" class="button"/></td>
    </tr>
  </table>
</form>
</body>
</html>
