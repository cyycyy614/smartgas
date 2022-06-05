package com.techen.smartgas.model;

public class SecurityUserDetailBean {

    private Boolean success;
    private String message;
    private Integer code;
    private ResultBean result;
    private Long timestamp;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public static class ResultBean {
        private Long id;
        private String cons_no;
        private Long area_id;
        private String cons_name;
        private String cons_addr;
        private Object identity_card;
        private String cons_tel;
        private Object contact_name;
        private Object postal_code;
        private Object cons_email;
        private Integer inuse_num;
        private Object living_area;
        private Integer balance;
        private String balance_verify;
        private Object balance_time;
        private String balance_limit;
        private String msg_mobile;
        private String autosms_flag;
        private String autotz_flag;
        private String autohz_flag;
        private String fee_month;
        private Integer fee_day;
        private Integer cons_status;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCons_no() {
            return cons_no;
        }

        public void setCons_no(String cons_no) {
            this.cons_no = cons_no;
        }

        public Long getArea_id() {
            return area_id;
        }

        public void setArea_id(Long area_id) {
            this.area_id = area_id;
        }

        public String getCons_name() {
            return cons_name;
        }

        public void setCons_name(String cons_name) {
            this.cons_name = cons_name;
        }

        public String getCons_addr() {
            return cons_addr;
        }

        public void setCons_addr(String cons_addr) {
            this.cons_addr = cons_addr;
        }

        public Object getIdentity_card() {
            return identity_card;
        }

        public void setIdentity_card(Object identity_card) {
            this.identity_card = identity_card;
        }

        public String getCons_tel() {
            return cons_tel;
        }

        public void setCons_tel(String cons_tel) {
            this.cons_tel = cons_tel;
        }

        public Object getContact_name() {
            return contact_name;
        }

        public void setContact_name(Object contact_name) {
            this.contact_name = contact_name;
        }

        public Object getPostal_code() {
            return postal_code;
        }

        public void setPostal_code(Object postal_code) {
            this.postal_code = postal_code;
        }

        public Object getCons_email() {
            return cons_email;
        }

        public void setCons_email(Object cons_email) {
            this.cons_email = cons_email;
        }

        public Integer getInuse_num() {
            return inuse_num;
        }

        public void setInuse_num(Integer inuse_num) {
            this.inuse_num = inuse_num;
        }

        public Object getLiving_area() {
            return living_area;
        }

        public void setLiving_area(Object living_area) {
            this.living_area = living_area;
        }

        public Integer getBalance() {
            return balance;
        }

        public void setBalance(Integer balance) {
            this.balance = balance;
        }

        public String getBalance_verify() {
            return balance_verify;
        }

        public void setBalance_verify(String balance_verify) {
            this.balance_verify = balance_verify;
        }

        public Object getBalance_time() {
            return balance_time;
        }

        public void setBalance_time(Object balance_time) {
            this.balance_time = balance_time;
        }

        public String getBalance_limit() {
            return balance_limit;
        }

        public void setBalance_limit(String balance_limit) {
            this.balance_limit = balance_limit;
        }

        public String getMsg_mobile() {
            return msg_mobile;
        }

        public void setMsg_mobile(String msg_mobile) {
            this.msg_mobile = msg_mobile;
        }

        public String getAutosms_flag() {
            return autosms_flag;
        }

        public void setAutosms_flag(String autosms_flag) {
            this.autosms_flag = autosms_flag;
        }

        public String getAutotz_flag() {
            return autotz_flag;
        }

        public void setAutotz_flag(String autotz_flag) {
            this.autotz_flag = autotz_flag;
        }

        public String getAutohz_flag() {
            return autohz_flag;
        }

        public void setAutohz_flag(String autohz_flag) {
            this.autohz_flag = autohz_flag;
        }

        public String getFee_month() {
            return fee_month;
        }

        public void setFee_month(String fee_month) {
            this.fee_month = fee_month;
        }

        public Integer getFee_day() {
            return fee_day;
        }

        public void setFee_day(Integer fee_day) {
            this.fee_day = fee_day;
        }

        public Integer getCons_status() {
            return cons_status;
        }

        public void setCons_status(Integer cons_status) {
            this.cons_status = cons_status;
        }
    }
}
