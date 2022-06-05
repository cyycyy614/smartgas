package com.techen.smartgas.model;

import org.itheima.recycler.L;

public class SecurityDetailBean {

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
        private String end_date;
        private String plan_type;
        private String dispstate;
        private String description;
        private Integer repetition_flag;
        private Long createuser;
        private String disprepetition_flag;
        private String plan_name;
        private String enabled;
        private Integer in_amount;
        private Long modifieduser;
        private String modifiedon;
        private Long user_id;
        private Integer total_amount;
        private String dispplan_type;
        private Long createorgid;
        private Long template_id;
        private String state;
        private Long id;
        private String start_date;

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getPlan_type() {
            return plan_type;
        }

        public void setPlan_type(String plan_type) {
            this.plan_type = plan_type;
        }

        public String getDispstate() {
            return dispstate;
        }

        public void setDispstate(String dispstate) {
            this.dispstate = dispstate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getRepetition_flag() {
            return repetition_flag;
        }

        public void setRepetition_flag(Integer repetition_flag) {
            this.repetition_flag = repetition_flag;
        }

        public Long getCreateuser() {
            return createuser;
        }

        public void setCreateuser(Long createuser) {
            this.createuser = createuser;
        }

        public String getDisprepetition_flag() {
            return disprepetition_flag;
        }

        public void setDisprepetition_flag(String disprepetition_flag) {
            this.disprepetition_flag = disprepetition_flag;
        }

        public String getPlan_name() {
            return plan_name;
        }

        public void setPlan_name(String plan_name) {
            this.plan_name = plan_name;
        }

        public String getEnabled() {
            return enabled;
        }

        public void setEnabled(String enabled) {
            this.enabled = enabled;
        }

        public Integer getIn_amount() {
            return in_amount;
        }

        public void setIn_amount(Integer in_amount) {
            this.in_amount = in_amount;
        }

        public Long getModifieduser() {
            return modifieduser;
        }

        public void setModifieduser(Long modifieduser) {
            this.modifieduser = modifieduser;
        }

        public String getModifiedon() {
            return modifiedon;
        }

        public void setModifiedon(String modifiedon) {
            this.modifiedon = modifiedon;
        }

        public Long getUser_id() {
            return user_id;
        }

        public void setUser_id(Long user_id) {
            this.user_id = user_id;
        }

        public Integer getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(Integer total_amount) {
            this.total_amount = total_amount;
        }

        public String getDispplan_type() {
            return dispplan_type;
        }

        public void setDispplan_type(String dispplan_type) {
            this.dispplan_type = dispplan_type;
        }

        public Long getCreateorgid() {
            return createorgid;
        }

        public void setCreateorgid(Long createorgid) {
            this.createorgid = createorgid;
        }

        public Long getTemplate_id() {
            return template_id;
        }

        public void setTemplate_id(Long template_id) {
            this.template_id = template_id;
        }


        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

    }
}
