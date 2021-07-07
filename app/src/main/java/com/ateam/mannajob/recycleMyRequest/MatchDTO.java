package com.ateam.mannajob.recycleMyRequest;

import java.io.Serializable;
import java.util.Date;

public class MatchDTO implements Serializable {
    private int mat_num;
    private int b_num;
    private String m_id;
    private String mat_hour;
    private String mat_stdate;
    private String mat_wdate;
    private String mat_okdate;
    private String mat_state;
    private String b_subject;

    public MatchDTO(int mat_num, int b_num, String m_id, String mat_hour, String mat_stdate, String mat_wdate, String mat_okdate, String mat_state, String b_subject) {
        this.mat_num = mat_num;
        this.b_num = b_num;
        this.m_id = m_id;
        this.mat_hour = mat_hour;
        this.mat_stdate = mat_stdate;
        this.mat_wdate = mat_wdate;
        this.mat_okdate = mat_okdate;
        this.mat_state = mat_state;
        this.b_subject = b_subject;
    }

    public String getB_subject() {
        return b_subject;
    }

    public void setB_subject(String b_subject) {
        this.b_subject = b_subject;
    }

    public int getMat_num() {
        return mat_num;
    }

    public void setMat_num(int mat_num) {
        this.mat_num = mat_num;
    }

    public int getB_num() {
        return b_num;
    }

    public void setB_num(int b_num) {
        this.b_num = b_num;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getMat_hour() {
        return mat_hour;
    }

    public void setMat_hour(String mat_hour) {
        this.mat_hour = mat_hour;
    }

    public String getMat_stdate() {
        return mat_stdate;
    }

    public void setMat_stdate(String mat_stdate) {
        this.mat_stdate = mat_stdate;
    }

    public String getMat_wdate() {
        return mat_wdate;
    }

    public void setMat_wdate(String mat_wdate) {
        this.mat_wdate = mat_wdate;
    }

    public String getMat_okdate() {
        return mat_okdate;
    }

    public void setMat_okdate(String mat_okdate) {
        this.mat_okdate = mat_okdate;
    }

    public String getMat_state() {
        return mat_state;
    }

    public void setMat_state(String mat_state) {
        this.mat_state = mat_state;
    }
}
