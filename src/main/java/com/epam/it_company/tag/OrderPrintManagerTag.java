package com.epam.it_company.tag;

import com.epam.it_company.domain.Job;
import com.epam.it_company.domain.Order;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;


public class OrderPrintManagerTag extends TagSupport {
    private List<Order> allOrders;

    public List<Order> getAllOrders() {
        return allOrders;
    }

    public void setAllOrders(List<Order> allOrders) {
        this.allOrders = allOrders;
    }

    public List<Order> getOrder() {
        return allOrders;
    }
    public void setOrder(List<Order> order) {
        this.allOrders = order;
    }
    @Override
    public int doStartTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            String noOrderMessage = (String)pageContext.getAttribute(
                    "no_order_message", PageContext.PAGE_SCOPE);
            out.write("<table align=\"center\" style=\"table-layout: fixed;width:100%\" class=\"show-table\" cellspacing=\'0\'>");
            if (allOrders == null || allOrders.size() == 0) {
                out.write("<tr><td><div>"+noOrderMessage+"<div></td></tr>");
            }else{
                setUpTable(out);
            }
            out.write("</table>");
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }

    private void setUpTable(JspWriter out) throws IOException {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        fillUpHeadTable(out);

        String projectCreated = (String)pageContext.getAttribute(
                "project_created", PageContext.PAGE_SCOPE);

        String projectCreateButton = (String)pageContext.getAttribute(
                "create_project_button", PageContext.PAGE_SCOPE);
        for(Order i: allOrders) {
            if(i != null) {
                List<Job> jobList = i.getJobList();
                Job firstJob = jobList.get(0);
                out.write("<tr>");
                out.write("<td rowspan=\""+jobList.size()+"\">");
                out.write(i.getName());
                out.write("</td>");
                out.write("<td style=\"word-wrap:break-word;\">"+firstJob.getJobDescription()+"</td>");
                out.write("<td>"+firstJob.getDevQualification()+"</td>");
                out.write("<td>"+firstJob.getDevNum()+"</td>");
                out.write("<td rowspan=\""+jobList.size()+"\">");
                if(i.isConfirmed()) {
                    out.write(projectCreated);
                }else{
                    out.write("<form action=\""+request.getContextPath()+"/controller\" method=\"post\" >");
                    out.write("<input type=\"hidden\" name=\"action\" value=\"check_order\">\n");
                    out.write("<input type=\"hidden\" name=\"order_id\" value=\""+i.getOrderId()+"\" />");
                    out.write("<input type=\"submit\" value=\""+projectCreateButton+"\" class=\"buttonLoc\" />");
                    out.write("</form>");
                }
                out.write("</td>");
                out.write("</tr>");
                for(int j = 1 ; j < jobList.size() ; j++) {
                    out.write("<tr>");
                    out.write("<td style=\"word-wrap:break-word; text-align:center;\">"+
                            jobList.get(j).getJobDescription()+"</td>");
                    out.write("<td>"+jobList.get(j).getDevQualification()+"</td>");
                    out.write("<td>"+jobList.get(j).getDevNum()+"</td>");
                    out.write("</tr>");
                }
            }
        }
    }
    private void fillUpHeadTable(JspWriter out) throws IOException{

        String orderName = (String)pageContext.getAttribute(
                "order_name", PageContext.PAGE_SCOPE);
        String devQualification = (String)pageContext.getAttribute(
                "dev_qualification", PageContext.PAGE_SCOPE);
        String jobDescription = (String)pageContext.getAttribute(
                "job_description", PageContext.PAGE_SCOPE);
        String devNum = (String)pageContext.getAttribute(
                "dev_num", PageContext.PAGE_SCOPE);
        String createProject = (String)pageContext.getAttribute(
                "create_project", PageContext.PAGE_SCOPE);

        out.write("<tr>");
        out.write("<th>"+orderName+"</th>");
        out.write("<th>"+jobDescription+"</th>");
        out.write("<th>"+devQualification+"</th>");
        out.write("<th>"+devNum+"</th>");
        out.write("<th>"+createProject+"</th>");
        out.write("</tr>");
    }
}
