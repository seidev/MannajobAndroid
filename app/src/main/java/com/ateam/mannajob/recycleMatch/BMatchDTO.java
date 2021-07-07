package com.ateam.mannajob.recycleMatch;

import java.io.Serializable;
import java.util.Date;

public class BMatchDTO implements Serializable {
    private int b_num;
    private String b_category;
    private String m_id;
    private String b_corp;
    private String b_task;
    private int b_price;
    private String b_location;
    private String b_stdate;
    private String b_endate;
    private String b_period;
    private String b_del;
    private String b_state;
    private String b_subject;
    private String b_wdate;
    private String b_contents;
    private String profileImage;


    public BMatchDTO(int b_num, String b_category, String m_id, String b_corp, String b_task, int b_price, String b_location, String b_stdate, String b_endate, String b_period, String b_del, String b_state, String b_subject, String b_wdate, String b_contents, String profileImage) {
        this.b_num = b_num;
        this.b_category = b_category;
        this.m_id = m_id;
        this.b_corp = b_corp;
        this.b_task = b_task;
        this.b_price = b_price;
        this.b_location = b_location;
        this.b_stdate = b_stdate;
        this.b_endate = b_endate;
        this.b_period = b_period;
        this.b_del = b_del;
        this.b_state = b_state;
        this.b_subject = b_subject;
        this.b_wdate = b_wdate;
        this.b_contents = b_contents;
        this.profileImage = profileImage;
    }

    public String getB_del() {
        return b_del;
    }

    public void setB_del(String b_del) {
        this.b_del = b_del;
    }


    public String getB_contents() {
        return b_contents;
    }

    public void setB_contents(String b_contents) {
        this.b_contents = b_contents;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public int getB_num() {
        return b_num;
    }

    public void setB_num(int b_num) {
        this.b_num = b_num;
    }

    public String getB_category() {
        return b_category;
    }

    public void setB_category(String b_category) {
        this.b_category = b_category;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getB_corp() {
        return b_corp;
    }

    public void setB_corp(String b_corp) {
        this.b_corp = b_corp;
    }

    public String getB_task() {
        return b_task;
    }

    public void setB_task(String b_task) {
        this.b_task = b_task;
    }

    public int getB_price() {
        return b_price;
    }

    public void setB_price(int b_price) {
        this.b_price = b_price;
    }

    public String getB_location() {
        return b_location;
    }

    public void setB_location(String b_location) {
        this.b_location = b_location;
    }

    public String getB_stdate() {
        return b_stdate;
    }

    public void setB_stdate(String b_stdate) {
        this.b_stdate = b_stdate;
    }

    public String getB_endate() {
        return b_endate;
    }

    public void setB_endate(String b_endate) {
        this.b_endate = b_endate;
    }

    public String getB_period() {
        return b_period;
    }

    public void setB_period(String b_period) {
        this.b_period = b_period;
    }

    public String getB_state() {
        return b_state;
    }

    public void setB_state(String b_state) {
        this.b_state = b_state;
    }

    public String getB_subject() {
        return b_subject;
    }

    public void setB_subject(String b_subject) {
        this.b_subject = b_subject;
    }

    public String getB_wdate() {
        return b_wdate;
    }

    public void setB_wdate(String b_wdate) {
        this.b_wdate = b_wdate;
    }
}
