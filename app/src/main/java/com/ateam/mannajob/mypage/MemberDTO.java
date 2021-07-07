package com.ateam.mannajob.mypage;

public class MemberDTO {
    private String m_id;
    private String m_api;
    private String m_name;
    private String m_phone;
    private String m_email;
    private String m_stop;
    private int e_num;
    private String stored_file_name;

    public MemberDTO(String m_id, String m_api, String m_name, String m_phone, String m_email, String m_stop, int e_num, String stored_file_name) {
        this.m_id = m_id;
        this.m_api = m_api;
        this.m_name = m_name;
        this.m_phone = m_phone;
        this.m_email = m_email;
        this.m_stop = m_stop;
        this.e_num = e_num;
        this.stored_file_name = stored_file_name;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getM_api() {
        return m_api;
    }

    public void setM_api(String m_api) {
        this.m_api = m_api;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getM_phone() {
        return m_phone;
    }

    public void setM_phone(String m_phone) {
        this.m_phone = m_phone;
    }

    public String getM_email() {
        return m_email;
    }

    public void setM_email(String m_email) {
        this.m_email = m_email;
    }

    public String getM_stop() {
        return m_stop;
    }

    public void setM_stop(String m_stop) {
        this.m_stop = m_stop;
    }

    public int getE_num() {
        return e_num;
    }

    public void setE_num(int e_num) {
        this.e_num = e_num;
    }

    public String getStored_file_name() {
        return stored_file_name;
    }

    public void setStored_file_name(String stored_file_name) {
        this.stored_file_name = stored_file_name;
    }
}
