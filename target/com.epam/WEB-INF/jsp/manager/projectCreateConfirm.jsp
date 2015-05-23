
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<head>
  <link rel="stylesheet" type="text/css" href="css/mainStyle.css">
  <link rel="stylesheet" type="text/css" href="css/buttonLoc.css">
  <c:choose>
    <c:when test="${sessionScope.local != null}">
      <fmt:setLocale value="${sessionScope.local}" />
    </c:when>
    <c:otherwise>
      <fmt:setLocale value="ru" />
    </c:otherwise>
  </c:choose>
  <fmt:setBundle basename="local" var="loc"/>

  <fmt:message bundle="${loc}" key="local.action.buttonRU" var="ru_button"/>
  <fmt:message bundle="${loc}" key="local.action.buttonEN" var="en_button"/>
  <fmt:message bundle="${loc}" key="local.action.button.projectConfirmDone" var="done_button"/>
  <fmt:message bundle="${loc}" key="local.text.header.createProjectConfirm" var="header_confirm"/>


</head>

<body>
<tabel align="right">
  <tr><td>
    <form action="<%=request.getContextPath()%>/controller" method="post" >
      <input type="hidden" name="action" value="change_local">
      <input type="hidden" name="local" value="ru" />
      <input type="hidden" name="page_name" value="WEB-INF/jsp/manager/projectCreateConfirm.jsp">
      <input type="submit" value="${ru_button}" class="buttonLoc"/>
      <br />
    </form>
  </td></tr>
  <tr><td>
    <form action="<%=request.getContextPath()%>/controller" method="post" >
      <input type="hidden" name="action" value="change_local">
      <input type="hidden" name="local" value="en" />
      <input type="hidden" name="page_name" value="WEB-INF/jsp/manager/projectCreateConfirm.jsp">
      <input type="submit" value="${en_button}" class="buttonLoc" />
      <br />
    </form>
  </td></tr>
</tabel>
<form action="<%=request.getContextPath()%>/controller" method="post" class="bootstrap-frm">
  <h1>${header_confirm}
  </h1>
  <input type="hidden" name="action" value="get_clients" />
  <table align="center" border="0">
    <tr><td><input type="submit" value="${done_button}" class="button"/></td></tr>
  </table>
</form>
</body>
</html>
