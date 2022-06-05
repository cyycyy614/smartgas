package com.techen.smartgas.model;

import com.google.gson.annotations.SerializedName;

import org.itheima.recycler.bean.BasePageBean;

import java.util.List;

public class SecurityBean extends BasePageBean<SecurityBean.ResultBean.DataListBean> {


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

    @Override
    public List<ResultBean.DataListBean> getItemDatas() {
        return result.dataList;
    }

    public static class ResultBean {
        @SerializedName("totalCount")
        private Integer totalCountX;
        @SerializedName("pageSize")
        private Integer pageSizeX;
        @SerializedName("totalPage")
        private Integer totalPageX;
        @SerializedName("currentPage")
        private Integer currentPageX;
        private List<DataListBean> dataList;

        public Integer getTotalCountX() {
            return totalCountX;
        }

        public void setTotalCountX(Integer totalCountX) {
            this.totalCountX = totalCountX;
        }

        public Integer getPageSizeX() {
            return pageSizeX;
        }

        public void setPageSizeX(Integer pageSizeX) {
            this.pageSizeX = pageSizeX;
        }

        public Integer getTotalPageX() {
            return totalPageX;
        }

        public void setTotalPageX(Integer totalPageX) {
            this.totalPageX = totalPageX;
        }

        public Integer getCurrentPageX() {
            return currentPageX;
        }

        public void setCurrentPageX(Integer currentPageX) {
            this.currentPageX = currentPageX;
        }

        public List<DataListBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<DataListBean> dataList) {
            this.dataList = dataList;
        }

        public static class DataListBean {
            private String end_date;
            private String plan_type;
            private String dispplan_type;
            private String description;
            private Integer repetition_flag;
            private String disprepetition_flag;
            private Long createuser;
            private String plan_name;
            private Integer in_amount;
            private String modifiedon;
            private Long modifieduser;
            private Long user_id;
            private Integer total_amount;
            private String rate;
            private Long createorgid;
            private String security_role_name;
            private String maker_name;
            private Long template_id;
            private String createon;
            private Long id;
            private String state;
            private String dispstate;
            private String start_date;
            private Object sortcode;

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

            public String getDispplan_type() {
                return dispplan_type;
            }

            public void setDispplan_type(String dispplan_type) {
                this.dispplan_type = dispplan_type;
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

            public String getDisprepetition_flag() {
                return disprepetition_flag;
            }

            public void setDisprepetition_flag(String disprepetition_flag) {
                this.disprepetition_flag = disprepetition_flag;
            }

            public Long getCreateuser() {
                return createuser;
            }

            public void setCreateuser(Long createuser) {
                this.createuser = createuser;
            }

            public String getPlan_name() {
                return plan_name;
            }

            public void setPlan_name(String plan_name) {
                this.plan_name = plan_name;
            }

            public Integer getIn_amount() {
                return in_amount;
            }

            public void setIn_amount(Integer in_amount) {
                this.in_amount = in_amount;
            }

            public String getModifiedon() {
                return modifiedon;
            }

            public void setModifiedon(String modifiedon) {
                this.modifiedon = modifiedon;
            }

            public Long getModifieduser() {
                return modifieduser;
            }

            public void setModifieduser(Long modifieduser) {
                this.modifieduser = modifieduser;
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

            public String getRate() {
                return rate;
            }

            public void setRate(String rate) {
                this.rate = rate;
            }

            public Long getCreateorgid() {
                return createorgid;
            }

            public void setCreateorgid(Long createorgid) {
                this.createorgid = createorgid;
            }

            public String getSecurity_role_name() {
                return security_role_name;
            }

            public void setSecurity_role_name(String security_role_name) {
                this.security_role_name = security_role_name;
            }

            public String getMaker_name() {
                return maker_name;
            }

            public void setMaker_name(String maker_name) {
                this.maker_name = maker_name;
            }

            public Long getTemplate_id() {
                return template_id;
            }

            public void setTemplate_id(Long template_id) {
                this.template_id = template_id;
            }

            public String getCreateon() {
                return createon;
            }

            public void setCreateon(String createon) {
                this.createon = createon;
            }

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getDispstate() {
                return dispstate;
            }

            public void setDispstate(String dispstate) {
                this.dispstate = dispstate;
            }

            public String getStart_date() {
                return start_date;
            }

            public void setStart_date(String start_date) {
                this.start_date = start_date;
            }

            public Object getSortcode() {
                return sortcode;
            }

            public void setSortcode(Object sortcode) {
                this.sortcode = sortcode;
            }
        }
    }
}
