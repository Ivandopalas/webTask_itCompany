
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
  <fmt:message bundle="${loc}" key="local.text.table.clientOrders" var="table_order"/>
  <fmt:message bundle="${loc}" key="local.text.table.clientAddOrder" var="table_add_order"/>
  <fmt:message bundle="${loc}" key="local.action.button.clientAddOrder" var="button_add_order"/>
  <fmt:message bundle="${loc}" key="local.action.button.clientShowOrder" var="button_show_order"/>
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
        <input type="hidden" name="page_name" value="WEB-INF/jsp/client/main.jsp">
        <input type="submit" value="${ru_button}" class="buttonLoc"/>
        <br />
      </form>
    </td>
  </tr>
  <tr>
    <td align="left" style="padding-left: 10px">
      <form action="<%=request.getContextPath()%>/controller" method="post" >
        <input type="hidden" name="action" value="logout">
        <input type="submit" value="${logout_button}" class="buttonLoc"/>
        <br />
      </form>
    </td>
    <td colspan="2" align="right">
      <form action="<%=request.getContextPath()%>/controller" method="post" >
        <input type="hidden" name="action" value="change_local">
        <input type="hidden" name="local" value="en" />
        <input type="hidden" name="page_name" value="WEB-INF/jsp/client/main.jsp">
        <input type="submit" value="${en_button}" class="buttonLoc" />
        <br />
      </form>
    </td>
  </tr>
</table>
<form action="<%=request.getContextPath()%>/controller" method="post" class="bootstrap-frm">
  <label>
    <input type="hidden" name="action" value="get_client_orders" />
    <input type="hidden" name="client_login" value="${sessionScope.login}" />
    <span>${table_order}</span>
    <input type="submit" value="${button_show_order}" style="padding: 10px 75px 10px 25px" class="button"/>
  </label>
</form>
<form action="<%=request.getContextPath()%>/controller" method="post" style="margin-top: 0px" class="bootstrap-frm">
  <label>
    <input type="hidden" name="action" value="reference" />
    <input type="hidden" name="ref_page" value="WEB-INF/jsp/client/addOrder.jsp">
    <span>${table_add_order}</span>
    <input type="submit" value="${button_add_order}" style="padding: 10px 75px 10px 25px" class="button"/>
  </label>
</form>
</body>
</html>
