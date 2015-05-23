
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


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
  <fmt:message bundle="${loc}" key="local.text.table.login" var="login"/>
  <fmt:message bundle="${loc}" key="local.action.buttonLogout" var="logout_button"/>
  <fmt:message bundle="${loc}" key="local.action.button.homeButton" var="button_home"/>
  <fmt:message bundle="${loc}" key="local.text.table.login" var="login"/>
  <fmt:message bundle="${loc}" key="local.text.table.name" var="name"/>
  <fmt:message bundle="${loc}" key="local.text.table.email" var="email"/>
  <fmt:message bundle="${loc}" key="local.text.table.projectName" var="project_name"/>
  <fmt:message bundle="${loc}" key="local.action.button.showProjects" var="show_projects_button"/>
  <fmt:message bundle="${loc}" key="local.text.table.addProjectTime" var="add_working_time"/>
  <fmt:message bundle="${loc}" key="local.action.button.setProjectTime" var="set_time_button"/>
  <fmt:message bundle="${loc}" key="local.text.noProject" var="no_projects"/>
  <fmt:message bundle="${loc}" key="local.text.table.projectTime" var="working_time"/>
  <fmt:message bundle="${loc}" key="local.text.table.workOurs" var="working_ours"/>
  <fmt:message bundle="${loc}" key="local.text.table.didntWork" var="didnt_work"/>

  <c:if test="${not empty requestScope.project_timeset_message}">
    <fmt:message bundle="${loc}" key="${requestScope.project_timeset_message}" var="message_timeset" />
  </c:if>

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
          <td align="left" style="padding-left: 10px">
            <form action="<%=request.getContextPath()%>/controller" method="post" >
              <input type="hidden" name="action" value="logout">
              <input type="submit" value="${logout_button}" class="buttonLoc"/>
              <br />
            </form>
          </td>
          <td>
            <form action="<%=request.getContextPath()%>/controller" method="post" style="margin-top: 0px">
              <input type="hidden" name="action" value="reference" />
              <input type="hidden" name="ref_page" value="WEB-INF/jsp/developer/main.jsp">
              <input type="submit" value="${button_home}" class="buttonLoc"/>
            </form>
          </td>
        </tr>
      </table>

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
      ${message_timeset}
</div>
<table class="show-table" >
  <c:if test="${fn:length(requestScope.project_list) gt 0}" >
    <tr>
      <th>${project_name}</th>
      <th>${add_working_time}</th>
    </tr>
    <c:forEach items="${requestScope.project_list}" var="project">
      <tr style="font-weight: bolder">
        <td>
          <c:out value="${project.projectName}"></c:out>
          <br/>
          <c:out value="id${project.idProject}"></c:out>
        </td>

        <td>
          <c:choose>
            <c:when test="${project.timeOnProject == -1}">
              ${didnt_work}
            </c:when>
            <c:otherwise>
              <c:out value="${project.timeOnProject}"></c:out> ${working_ours}
            </c:otherwise>
          </c:choose>

          <form action="<%=request.getContextPath()%>/controller" method="post" >
            <input type="hidden" name="action" value="add_project_timeworking"/>
            <input type="hidden" name="project_id" value="${project.idProject}" />
            <table>
              <tr>
                <td>
                  <input type="text" name="project_time"/>
                </td>
              </tr>
              <tr>
                <td align="center">
                  <input type="submit" value="${set_time_button}" class="buttonLoc" />
                </td>
              </tr>
            </table>
          </form>
        </td>
      </tr>
    </c:forEach>
  </c:if>

  <c:if test="${fn:length(requestScope.project_list) == 0}" >
    <table class="show-table">
      <tr><td>${no_projects}</td></tr>
    </table>
  </c:if>
</table>
</body>
</html>
