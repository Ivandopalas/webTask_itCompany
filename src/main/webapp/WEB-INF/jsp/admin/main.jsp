<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<head>
  <link rel="stylesheet" type="text/css" href="css/headTable.css">
  <link rel="stylesheet" type="text/css" href="css/buttonLoc.css">
  <link rel="stylesheet" type="text/css" href="css/mainStyle.css">

  <c:choose>
    <c:when test="${sessionScope.local != null}">
      <fmt:setLocale value="${sessionScope.local}" />
    </c:when>
    <c:otherwise>
      <fmt:setLocale value="ru" />
    </c:otherwise>
  </c:choose>
  <fmt:setBundle basename="local" var="loc" />

  <fmt:message bundle="${loc}" key="local.action.buttonRU" var="ru_button"/>
  <fmt:message bundle="${loc}" key="local.action.buttonEN" var="en_button"/>
  <fmt:message bundle="${loc}" key="local.text.table.login" var="login"/>
  <fmt:message bundle="${loc}" key="local.action.buttonLogout" var="logout_button"/>
  <fmt:message bundle="${loc}" key="local.action.button.showUsers" var="show_users_button"/>
  <fmt:message bundle="${loc}" key="local.text.table.showUsers" var="show_users"/>
  <fmt:message bundle="${loc}" key="local.action.button.addUser" var="add_user_button"/>
  <fmt:message bundle="${loc}" key="local.text.table.addUser" var="add_user"/>

</head>

<body>

<table border="0" class="headtable">
  <tr>
    <td style="padding-left: 10px">
      <span>${login} ${sessionScope.login}</span>
    </td>
    <td align="right">
      <form action="<%=request.getContextPath()%>/controller" method="post" >
        <input type="hidden" name="action" value="change_local">
        <input type="hidden" name="local" value="ru" />
        <input type="hidden" name="page_name" value="WEB-INF/jsp/admin/main.jsp">
        <input type="submit" value="${ru_button}" class="buttonLoc"/>
        <br />
      </form>
    </td>
  </tr>
  <tr>
    <td align="left">
      <form action="<%=request.getContextPath()%>/controller" method="post" style="padding-left: 10px">
        <input type="hidden" name="action" value="logout">
        <input type="submit" value="${logout_button}" class="buttonLoc"/>
        <br />
      </form>
    </td>
    <td colspan="2" align="right">
      <form action="<%=request.getContextPath()%>/controller" method="post" >
        <input type="hidden" name="action" value="change_local">
        <input type="hidden" name="local" value="en" />
        <input type="hidden" name="page_name" value="WEB-INF/jsp/admin/main.jsp">
        <input type="submit" value="${en_button}" class="buttonLoc" />
        <br />
      </form>
    </td>
  </tr>
</table>
<form action="<%=request.getContextPath()%>/controller" method="post" class="bootstrap-frm">
  <label>
    <input type="hidden" name="action" value="get_all_users" />
    <span>${show_users}</span>
    <input type="submit" value="${show_users_button}" class="button"/>
  </label>
</form>
<form action="<%=request.getContextPath()%>/controller" style="margin-top: 0px" method="post" class="bootstrap-frm">
  <label>
    <input type="hidden" name="action" value="reference" />
    <input type="hidden" name="ref_page" value="WEB-INF/jsp/admin/addUser.jsp">
    <span>${add_user}</span>
    <input type="submit" value="${add_user_button}" class="button"/>
  </label>
</form>
</body>
</html>
