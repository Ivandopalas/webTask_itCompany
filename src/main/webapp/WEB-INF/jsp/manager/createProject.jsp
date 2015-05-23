
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<head>
  <link rel="stylesheet" type="text/css" href="css/headTable.css">
  <link rel="stylesheet" type="text/css" href="css/buttonLoc.css">
  <link rel="stylesheet" type="text/css" href="css/mainStyle.css">

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
  <fmt:message bundle="${loc}" key="local.action.buttonRU" var="ru_button"/>
  <fmt:message bundle="${loc}" key="local.action.buttonEN" var="en_button"/>
  <fmt:message bundle="${loc}" key="local.text.table.login" var="login"/>
  <fmt:message bundle="${loc}" key="local.action.buttonLogout" var="logout_button"/>
  <fmt:message bundle="${loc}" key="local.action.button.homeButton" var="button_home"/>
  <c:if test="${not empty requestScope.project_result_message}">
    <fmt:message bundle="${loc}" key="${requestScope.project_result_message}" var="project_result_message" />
  </c:if>
  <fmt:message bundle="${loc}" key="local.text.header.createProject" var="header_project"/>
  <fmt:message bundle="${loc}" key="local.text.table.projectName" var="project_name"/>
  <fmt:message bundle="${loc}" key="local.text.table.setDevelopers" var="set_dev"/>
  <fmt:message bundle="${loc}" key="local.action.button.createProject" var="button_create_project"/>
  <fmt:message bundle="${loc}" key="local.text.table.projectCost" var="project_cost"/>

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
              <input type="hidden" name="ref_page" value="WEB-INF/jsp/manager/main.jsp">
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

<form action="<%=request.getContextPath()%>/controller" method="post" class="bootstrap-frm">
  <h1>${header_project}</h1>
  <input type="hidden" name="action" value="create_project" />
  <input type="hidden" name="order_id" value="${requestScope.order_id}"/>
  <label>
    <span>${project_name}</span>
    <input type="text" oninput="valid(this)" name="project_name"/>
  </label>
  <label>
    <span>${project_cost}</span>
    <input type="text" name="project_cost"/>
  </label>
  <label style="margin-left: auto; margin-right: auto">
    <span>${set_dev}</span>
    <div style="padding-left: 43%">
      <table class="show-table">
        <tr>
          <th>${login}</th>
          <th></th>
        </tr>
        <c:set var="count" value="0" scope="page" />
        <c:forEach items="${requestScope.developer_list}" var="developers">
          <tr>
            <td><c:out value="${developers.login}"></c:out></td>
            <td>
              <input type="checkbox" name="dev_${count}" value="${developers.login}"/>
            </td>
          </tr>
          <c:set var="count" value="${count + 1}" scope="page" />
        </c:forEach>
      </table>
      <input type="hidden" name="dev_count" value="${count}"/>
    </div>
  </label>
  <div>
    <span style="color: #464646; padding-left: 165px">${project_result_message}</span>
  </div>
  <table align="center" border="0">
    <tr>
      <td><input type="submit" value="${button_create_project}" class="button"/></td>
    </tr>
  </table>
</form>

</body>
</html>
