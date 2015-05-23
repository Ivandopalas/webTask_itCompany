
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<head>
  <link rel="stylesheet" type="text/css" href="css/headTable.css">
  <link rel="stylesheet" type="text/css" href="css/showTableStyle.css">
  <link rel="stylesheet" type="text/css" href="css/buttonLoc.css">
  <link rel="stylesheet" type="text/css" href="css/mainStyle.css">

  <script src="js/jobForm.js" type="text/javascript"></script>
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
  <fmt:message bundle="${loc}" key="local.text.table.orderName" var="order_name"/>
  <fmt:message bundle="${loc}" key="local.text.header.addOrder" var="header_addOrder"/>
  <fmt:message bundle="${loc}" key="local.action.button.clientAddOrder" var="button_addOrder"/>
  <fmt:message bundle="${loc}" key="local.action.button.homeButton" var="button_home"/>
  <fmt:message bundle="${loc}" key="local.action.button.addJob" var="button_add_job"/>
  <fmt:message bundle="${loc}" key="local.text.table.devNum" var="dev_num"/>
  <fmt:message bundle="${loc}" key="local.text.table.devQualification" var="dev_qualification"/>
  <fmt:message bundle="${loc}" key="local.text.table.jobDescription" var="job_description"/>
  <fmt:message bundle="${loc}" key="local.action.button.clearJobs" var="clear_jobs"/>
  <c:if test="${not empty requestScope.order_result_message}">
    <fmt:message bundle="${loc}" key="${requestScope.order_result_message}" var="message_order" />
  </c:if>
  <fmt:message bundle="${loc}" key="local.text.table.jobList" var="job_list"/>

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
        <input type="hidden" name="page_name" value="WEB-INF/jsp/client/addOrder.jsp">
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
        <input type="hidden" name="page_name" value="WEB-INF/jsp/client/addOrder.jsp">
        <input type="submit" value="${en_button}" class="buttonLoc" />
        <br />
      </form>
    </td>
  </tr>
</table>

<div align="center" style="color: #464646;align-content: center">
  ${message_order}</div>
<form action="<%=request.getContextPath()%>/controller" style="margin-top: 25px;" method="post" class="bootstrap-frm">
  <h1 align="center">${header_addOrder}</h1>
  <input type="hidden" name="action" value="new_order" />
  <label>${order_name}
    <input type="text" name="order_name"  oninput="valid(this)" style="margin:-8px -6px 20px -10px;float:right; width:70%"/>
  </label>

  <table align="center" style="width: 100%;table-layout: fixed">
    <tr>
      <td style="table-layout: fixed">
        <form id='fields' class="bootstrap-frm">
          <table align="center" style="width:100%;margin-top: 0px;table-layout: fixed" class="bootstrap-frm">
            <tr style="table-layout: fixed">
              <td style="table-layout: fixed">

                <label>${job_description}</label>
                <textarea style="resize: none;width: 90%" rows="3" id='jobDescription'  oninput="valid(this)" name="jobDescription"></textarea>
                <label>${dev_qualification}</label>
                <input type="text" style="width: 90%;" id='devQualification'  oninput="valid(this)"   name="devQualification">
                <label>${dev_num}</label>
                <input type="text" style="width: 90%;" id='devNum' oninput="valid (this)" name="devNum">
              </td>
            </tr>
            <tr>
              <td>
                <div align="center">
                  <button id='Addbtn' value='Add' class="button">${button_add_job}</button>
                </div>
              </td>
            </tr>
          </table>
        </form>
      </td>
      <td style="column-span: 2;overflow: auto;table-layout: fixed" >
      <div> ${job_list}</div>
        <div id="hidden_job"/>
        <div id="jobs"/>
        <button id="Clearbtn" type="button" >${clear_jobs}</button>
      </td>
    </tr>
    <tr align="center" style="row-span: 2;">
      <td>
      </td>
    </tr>
  </table>
  <div align="center">
    <input type="submit" value="${button_addOrder}" class="button"/>
  </div>
  <div id="btns"></div>
</form>

</body>
</html>
