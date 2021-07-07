package com.ateam.mannajob.recycleReview.recycleCalendar;

public class ReviewDTO {
    private int r_num;
    private int mat_num;
    private String r_good;
    private String r_contents;
    private String r_wdate;
    private String r_del;
    private String r_w_m_id;
    private String r_mat_m_id;
    private int countG;
    private int countR;

    public ReviewDTO(int r_num, int mat_num, String r_good, String r_contents, String r_wdate, String r_del, String r_w_m_id, String r_mat_m_id, int countG, int countR) {
        this.r_num = r_num;
        this.mat_num = mat_num;
        this.r_good = r_good;
        this.r_contents = r_contents;
        this.r_wdate = r_wdate;
        this.r_del = r_del;
        this.r_w_m_id = r_w_m_id;
        this.r_mat_m_id = r_mat_m_id;
        this.countG = countG;
        this.countR = countR;
    }

    public int getR_num() {
        return r_num;
    }

    public void setR_num(int r_num) {
        this.r_num = r_num;
    }

    public int getMat_num() {
        return mat_num;
    }

    public void setMat_num(int mat_num) {
        this.mat_num = mat_num;
    }

    public String getR_good() {
        return r_good;
    }

    public void setR_good(String r_good) {
        this.r_good = r_good;
    }

    public String getR_contents() {
        return r_contents;
    }

    public void setR_contents(String r_contents) {
        this.r_contents = r_contents;
    }

    public String getR_wdate() {
        return r_wdate;
    }

    public void setR_wdate(String r_wdate) {
        this.r_wdate = r_wdate;
    }

    public String getR_del() {
        return r_del;
    }

    public void setR_del(String r_del) {
        this.r_del = r_del;
    }

    public String getR_w_m_id() {
        return r_w_m_id;
    }

    public void setR_w_m_id(String r_w_m_id) {
        this.r_w_m_id = r_w_m_id;
    }

    public String getR_mat_m_id() {
        return r_mat_m_id;
    }

    public void setR_mat_m_id(String r_mat_m_id) {
        this.r_mat_m_id = r_mat_m_id;
    }

    public int getCountG() {
        return countG;
    }

    public void setCountG(int countG) {
        this.countG = countG;
    }

    public int getCountR() {
        return countR;
    }

    public void setCountR(int countR) {
        this.countR = countR;
    }
}
