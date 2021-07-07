package com.ateam.mannajob.recycleNotice;

import java.io.Serializable;
import java.util.Date;

public class NoticeDTO implements Serializable {
    private int n_num;
    private String n_subject;
    private String ad_id;
    private String n_contents;
    private int n_cnt;
    private String n_udate;
    private String n_del;

    public NoticeDTO(int n_num, String n_subject, String ad_id, String n_contents, int n_cnt, String n_udate, String n_del) {
        this.n_num = n_num;
        this.n_subject = n_subject;
        this.ad_id = ad_id;
        this.n_contents = n_contents;
        this.n_cnt = n_cnt;
        this.n_udate = n_udate;
        this.n_del = n_del;
    }

    public int getN_num() {
        return n_num;
    }

    public void setN_num(int n_num) {
        this.n_num = n_num;
    }

    public String getN_subject() {
        return n_subject;
    }

    public void setN_subject(String n_subject) {
        this.n_subject = n_subject;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getN_contents() {
        return n_contents;
    }

    public void setN_contents(String n_contents) {
        this.n_contents = n_contents;
    }

    public int getN_cnt() {
        return n_cnt;
    }

    public void setN_cnt(int n_cnt) {
        this.n_cnt = n_cnt;
    }

    public String getN_udate() {
        return n_udate;
    }

    public void setN_udate(String n_udate) {
        this.n_udate = n_udate;
    }

    public String getN_del() {
        return n_del;
    }

    public void setN_del(String n_del) {
        this.n_del = n_del;
    }
}
