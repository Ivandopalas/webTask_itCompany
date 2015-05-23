
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<head>
  <link rel="stylesheet" type="text/css" href="css/headTable.css">
  <link rel="stylesheet" type="text/css" href="css/buttonLoc.css">
  <link rel="stylesheet" type="text/css" href="css/showTableStyle.css">

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
  <fmt:message bundle="${loc}" key="local.action.button.homeButton" var="button_home"/>
  <fmt:message bundle="${loc}" key="local.text.table.login" var="login"/>
  <fmt:message bundle="${loc}" key="local.action.buttonLogout" var="logout_button"/>
  <fmt:message bundle="${loc}" key="local.text.table.login" var="login"/>
  <fmt:message bundle="${loc}" key="local.text.table.name" var="name"/>
  <fmt:message bundle="${loc}" key="local.text.table.email" var="email"/>
  <fmt:message bundle="${loc}" key="local.text.table.clientOrders" var="orders"/>
  <c:if test="${not empty requestScope.user_delete_message}">
    <fmt:message bundle="${loc}" key="local.action.button.clientShowOrder" var="show_order_button"/>
  </c:if>
  <fmt:message bundle="${loc}" key="local.text.table.userType" var="user_type"/>
  <fmt:message bundle="${loc}" key="local.text.table.deleteUser" var="delete_act"/>
  <fmt:message bundle="${loc}" key="local.action.button.delete" var="delete_button"/>

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
        <input type="hidden" name="page_name" value="dynamic_page">
        <input type="submit" value="${ru_button}" class="buttonLoc"/>
        <br />
      </form>
    </td>
  </tr>
  <tr>
    <td align="left" style="padding-left: 10px">
      <table>
        <tr>
          <td>
            <form action="<%=request.getContextPath()%>/controller" method="post" >
              <input type="hidden" name="action" value="logout">
              <input type="submit" value="${logout_button}" class="buttonLoc"/>
              <br />
            </form>
          </td>
          <td>
            <form action="<%=request.getContextPath()%>/controller" method="post" style="margin-top: 0px">
              <input type="hidden" name="action" value="reference" />
              <input type="hidden" name="ref_page" value="WEB-INF/jsp/admin/main.jsp">
              <input type="submit" value="${button_home}" class="buttonLoc"/>
            </form>
          </td>
        </tr>
      </table>
    </td>
    <td colspan="2" align="right">
      <form action="<%=request.getContextPath()%>/controller" method="post" >
        <input type="hidden" name="action" value="change_local">
        <input type="hidden" name="local" value="en" />
        <input type="hidden" name="page_name" value="dynamic_page">
        <input type="submit" value="${en_button}" class="buttonLoc" />
        <br />
      </form>
    </td>
  </tr>
</table>
<div align="center"
     style="font-family: Helvetica Neue, Helvetica, Arial,
     sans-serif;font-weight: bold;">
  ${delete_message}
</div>
<table class="show-table">
  <tr>
    <th>${name}</th>
    <th>${login}</th>
    <th>${email}</th>
    <th>${user_type}</th>
    <th>${delete_act}</th>
  </tr>
  <c:forEach items="${requestScope.user_list}" var="users">
    <tr>
      <td><c:out value="${users.name}"></c:out></td>
      <td><c:out value="${users.login}"></c:out></td>
      <td><c:out value="${users.email}"></c:out></td>
      <td><c:out value="${users.userType}"></c:out></td>
      <td>
        <form action="<%=request.getContextPath()%>/controller" method="post" >
          <input type="hidden" name="action" value="delete_user"/>
          <input type="hidden" name="user_login" value="${users.login}" />
          <input type="submit" value="${delete_button}" class="buttonLoc"/>
        </form>
      </td>
    </tr>
  </c:forEach>
</table>
</body>
</html>
