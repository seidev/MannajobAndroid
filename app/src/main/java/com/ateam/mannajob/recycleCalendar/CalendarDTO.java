package com.ateam.mannajob.recycleCalendar;

import java.util.Date;

public class CalendarDTO {
    private int b_num;
    private String m_id;
    private String phone;
    private String mat_stdate;
    private String mat_hour;
    private String b_location;

    public CalendarDTO(int b_num, String m_id, String phone, String mat_stdate, String mat_hour, String b_location) {
        this.b_num = b_num;
        this.m_id = m_id;
        this.phone = phone;
        this.mat_stdate = mat_stdate;
        this.mat_hour = mat_hour;
        this.b_location = b_location;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMat_stdate() {
        return mat_stdate;
    }

    public void setMat_stdate(String mat_stdate) {
        this.mat_stdate = mat_stdate;
    }

    public String getMat_hour() {
        return mat_hour;
    }

    public void setMat_hour(String mat_hour) {
        this.mat_hour = mat_hour;
    }

    public String getB_location() {
        return b_location;
    }

    public void setB_location(String b_location) {
        this.b_location = b_location;
    }
}
