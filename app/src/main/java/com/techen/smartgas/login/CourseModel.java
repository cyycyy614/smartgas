package com.techen.smartgas.login;

public class CourseModel {

    private String order_name;

    public CourseModel(String order_name, String order_mobile, String order_code, String order_report_time, String order_account_address, String order_source, String order_disp_state) {
        this.order_name = order_name;
        this.order_mobile = order_mobile;
        this.order_code = order_code;
        this.order_report_time = order_report_time;
        this.order_account_address = order_account_address;
        this.order_source = order_source;
        this.order_disp_state = order_disp_state;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getOrder_mobile() {
        return order_mobile;
    }

    public void setOrder_mobile(String order_mobile) {
        this.order_mobile = order_mobile;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getOrder_report_time() {
        return order_report_time;
    }

    public void setOrder_report_time(String order_report_time) {
        this.order_report_time = order_report_time;
    }

    public String getOrder_account_address() {
        return order_account_address;
    }

    public void setOrder_account_address(String order_account_address) {
        this.order_account_address = order_account_address;
    }

    public String getOrder_source() {
        return order_source;
    }

    public void setOrder_source(String order_source) {
        this.order_source = order_source;
    }

    public String getOrder_disp_state() {
        return order_disp_state;
    }

    public void setOrder_disp_state(String order_disp_state) {
        this.order_disp_state = order_disp_state;
    }

    private String order_mobile;
    private String order_code;
    private String order_report_time;
    private String order_account_address;
    private String order_source;
    private String order_disp_state;


}