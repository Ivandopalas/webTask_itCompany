
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="print_orders" uri="/WEB-INF/tld/printorder.tld" %>


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
  <fmt:message bundle="${loc}" key="local.text.table.noOrderMessage" var="no_order_message"/>
  <fmt:message bundle="${loc}" key="local.text.table.orderName" var="order_name"/>
  <fmt:message bundle="${loc}" key="local.text.table.devNum" var="dev_num"/>
  <fmt:message bundle="${loc}" key="local.text.table.devQualification" var="dev_qualification"/>
  <fmt:message bundle="${loc}" key="local.text.table.isConfirmed" var="is_confirmed"/>
  <fmt:message bundle="${loc}" key="local.text.table.orderCost" var="order_cost"/>
  <fmt:message bundle="${loc}" key="local.text.table.jobDescription" var="job_description"/>


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
              <input type="hidden" name="ref_page" value="WEB-INF/jsp/client/main.jsp">
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
<print_orders:client_print allOrders="${requestScope.order_list}"/>
</body>
</html>
