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
        private String cons_tel;
        private String msg_mobile;
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

        public String getCons_tel() {
            return cons_tel;
        }

        public void setCons_tel(String cons_tel) {
            this.cons_tel = cons_tel;
        }


        public String getMsg_mobile() {
            return msg_mobile;
        }

        public void setMsg_mobile(String msg_mobile) {
            this.msg_mobile = msg_mobile;
        }

        public Integer getCons_status() {
            return cons_status;
        }

        public void setCons_status(Integer cons_status) {
            this.cons_status = cons_status;
        }
    }
}
