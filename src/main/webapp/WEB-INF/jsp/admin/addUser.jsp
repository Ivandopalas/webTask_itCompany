
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<head>
  <link rel="stylesheet" type="text/css" href="css/mainStyle.css">
  <link rel="stylesheet" type="text/css" href="css/buttonLoc.css">
  <link rel="stylesheet" type="text/css" href="css/headTable.css">

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
  <c:if test="${not empty requestScope.add_user_message}">
    <fmt:message bundle="${loc}" key="${requestScope.add_user_message}" var="add_user_message" />
  </c:if>
  <fmt:message bundle="${loc}" key="local.action.buttonRU" var="ru_button"/>
  <fmt:message bundle="${loc}" key="local.action.buttonEN" var="en_button"/>
  <fmt:message bundle="${loc}" key="local.action.buttonLogout" var="logout_button"/>
  <fmt:message bundle="${loc}" key="local.action.button.homeButton" var="button_home"/>
  <fmt:message bundle="${loc}" key="local.text.table.login" var="login"/>
  <fmt:message bundle="${loc}" key="local.text.table.password" var="password"/>
  <fmt:message bundle="${loc}" key="local.text.table.name" var="name"/>
  <fmt:message bundle="${loc}" key="local.text.table.email" var="email"/>
  <fmt:message bundle="${loc}" key="local.action.button.addUser" var="add_user_button"/>
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
        <input type="hidden" name="page_name" value="WEB-INF/jsp/admin/addUser.jsp">
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
        <input type="hidden" name="page_name" value="WEB-INF/jsp/admin/addUser.jsp">
        <input type="submit" value="${en_button}" class="buttonLoc" />
        <br />
      </form>
    </td>
  </tr>
</table>




<form action="<%=request.getContextPath()%>/controller" method="post" class="bootstrap-frm">
  <h1>${header_registration}
    <span>${subheader_registration}</span>
  </h1>
  <input type="hidden" name="action" value="add_user" />
  <label>
    <span>${login}</span>
    <input type="text"  oninput="valid(this)" name="login"/>
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
  <label style="margin-left: 40%">
    <input type="radio" name="user_type" value="CLIENT"> client<Br>
    <input type="radio" name="user_type" value="MANAGER"> manager<Br>
    <input type="radio" name="user_type" value="DEVELOPER"> developer<Br>
  </label>
  <div align="center"> <span style="color: #000000;align-content: center">${add_user_message}</span></div>
  <table align="center" border="0">
    <tr>
      <td><input type="submit" value="${add_user_button}" class="button"/></td>
    </tr>
  </table>
</form>
</body>
</html>
