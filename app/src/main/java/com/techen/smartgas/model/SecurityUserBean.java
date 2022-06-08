package com.techen.smartgas.model;

import org.itheima.recycler.bean.BasePageBean;

import java.util.List;

public class SecurityUserBean extends BasePageBean<SecurityUserBean.ResultBean.DataListBean> {

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
        private Integer totalCount;
        private Integer pageSize;
        private Integer totalPage;
        private Integer currentPage;
        private List<DataListBean> dataList;

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Integer getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(Integer totalPage) {
            this.totalPage = totalPage;
        }

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }

        public List<DataListBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<DataListBean> dataList) {
            this.dataList = dataList;
        }

        public static class DataListBean {
            private String cons_tel;
            private String cons_addr;
            private String dispstate;
            private String cons_no;
            private String type;
            private Long template_id;
            private String cons_name;
            private String state;
            private Long record_id;
            private Long cons_id;
            private Long plan_id;
            private String plan_state;

            public String getPlan_state() {
                return plan_state;
            }

            public void setPlan_state(String plan_state) {
                this.plan_state = plan_state;
            }

            public String getCons_tel() {
                return cons_tel;
            }

            public void setCons_tel(String cons_tel) {
                this.cons_tel = cons_tel;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getCons_addr() {
                return cons_addr;
            }

            public void setCons_addr(String cons_addr) {
                this.cons_addr = cons_addr;
            }

            public String getDispstate() {
                return dispstate;
            }

            public void setDispstate(String dispstate) {
                this.dispstate = dispstate;
            }

            public String getCons_no() {
                return cons_no;
            }

            public void setCons_no(String cons_no) {
                this.cons_no = cons_no;
            }

            public Long getTemplate_id() {
                return template_id;
            }

            public void setTemplate_id(Long template_id) {
                this.template_id = template_id;
            }

            public Long getPlan_id() {
                return plan_id;
            }

            public void setPlan_id(Long plan_id) {
                this.plan_id = plan_id;
            }

            public String getCons_name() {
                return cons_name;
            }

            public void setCons_name(String cons_name) {
                this.cons_name = cons_name;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public Long getRecord_id() {
                return record_id;
            }

            public void setRecord_id(Long record_id) {
                this.record_id = record_id;
            }
            public Long getCons_id() {
                return cons_id;
            }

            public void setCons_id(Long cons_id) {
                this.cons_id = cons_id;
            }
        }
    }
}
