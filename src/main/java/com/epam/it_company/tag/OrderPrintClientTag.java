package com.epam.it_company.tag;

import com.epam.it_company.domain.Job;
import com.epam.it_company.domain.Order;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;


public class OrderPrintClientTag extends TagSupport {
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

        fillUpHeadTable(out);

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
                out.write("<td  rowspan=\""+jobList.size()+"\">");
                boolean isConfirmed = i.isConfirmed();
                out.write("<div style=\"font-size: 35px;\">"+(isConfirmed == true?"+":"-")+"</div>");
                out.write("</td>");
                out.write("<td rowspan=\""+jobList.size()+"\">");
                out.write(String.valueOf(i.getCost()));
                out.write("</td>");
                out.write("</tr>");
                for(int j = 1 ; j < jobList.size() ; j++) {
                    out.write("<tr>");
                    out.write("<td style=\"word-wrap:break-word;text-align:center;\">"+
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
        String isConfirmedOrder = (String)pageContext.getAttribute(
                "is_confirmed", PageContext.PAGE_SCOPE);
        String orderCost = (String)pageContext.getAttribute(
                "order_cost", PageContext.PAGE_SCOPE);

        out.write("<tr>");
        out.write("<th>"+orderName+"</th>");
        out.write("<th>"+jobDescription+"</th>");
        out.write("<th>"+devQualification+"</th>");
        out.write("<th>"+devNum+"</th>");
        out.write("<th>"+isConfirmedOrder+"</th>");
        out.write("<th>"+orderCost+"</th>");
        out.write("</tr>");
    }
}
