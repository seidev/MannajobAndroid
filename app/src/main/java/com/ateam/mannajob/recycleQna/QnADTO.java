package com.ateam.mannajob.recycleQna;

import java.io.Serializable;
import java.util.Date;

public class QnADTO implements Serializable {
    private int q_num;
    private String q_category;
    private String m_id;
    private String q_contents;
    private String q_udate;
    private String q_subject;
    private String qs_contents;
    private String qs_udate;

    public QnADTO(int q_num, String q_category, String m_id, String q_contents, String q_udate, String q_subject, String qs_contents, String qs_udate) {
        this.q_num = q_num;
        this.q_category = q_category;
        this.m_id = m_id;
        this.q_contents = q_contents;
        this.q_udate = q_udate;
        this.q_subject = q_subject;
        this.qs_contents = qs_contents;
        this.qs_udate = qs_udate;
    }

    public int getQ_num() {
        return q_num;
    }

    public void setQ_num(int q_num) {
        this.q_num = q_num;
    }

    public String getQ_category() {
        return q_category;
    }

    public void setQ_category(String q_category) {
        this.q_category = q_category;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getQ_contents() {
        return q_contents;
    }

    public void setQ_contents(String q_contents) {
        this.q_contents = q_contents;
    }

    public String getQ_udate() {
        return q_udate;
    }

    public void setQ_udate(String q_udate) {
        this.q_udate = q_udate;
    }

    public String getQ_subject() {
        return q_subject;
    }

    public void setQ_subject(String q_subject) {
        this.q_subject = q_subject;
    }

    public String getQs_contents() {
        return qs_contents;
    }

    public void setQs_contents(String qs_contents) {
        this.qs_contents = qs_contents;
    }

    public String getQs_udate() {
        return qs_udate;
    }

    public void setQs_udate(String qs_udate) {
        this.qs_udate = qs_udate;
    }
}
