package com.epam.it_company.domain;


/**
 * Bean contain information about jobs.
 */
public class Job {
    private String jobDescription;
    private String devQualification;
    private int devNum;

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
    public String getDevQualification() {
        return devQualification;
    }

    public void setDevQualification(String devQualification) {
        this.devQualification = devQualification;
    }

    public int getDevNum() {
        return devNum;
    }

    public void setDevNum(int devNum) {
        this.devNum = devNum;
    }
}
