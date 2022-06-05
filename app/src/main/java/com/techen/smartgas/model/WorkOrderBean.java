package com.techen.smartgas.model;

import org.itheima.recycler.bean.BasePageBean;

import java.util.List;

public class WorkOrderBean  extends BasePageBean<WorkOrderBean.ResultBean.DataListBean> {

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
        private Integer totalPage;
        private List<DataListBean> dataList;
        private Integer pageSize;
        private Integer currentPage;
        private Integer totalCount;

        public Integer getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(Integer totalPage) {
            this.totalPage = totalPage;
        }

        public List<DataListBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<DataListBean> dataList) {
            this.dataList = dataList;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

        public static class DataListBean {
            private String appointment_time;
            private String code;
            private String dispstate;
            private String worker_name;
            private String account_phone;
            private String description;
            private String reporter_name;
            private String disporder_source;
            private Long worker_id;
            private String order_code;
            private Long danger_id;
            private String report_time;
            private String order_source;
            private String work_content;
            private String account_address;
            private Long id;
            private String state;
            private Long report_user_id;

            public String getAppointment_time() {
                return appointment_time;
            }

            public void setAppointment_time(String appointment_time) {
                this.appointment_time = appointment_time;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getDispstate() {
                return dispstate;
            }

            public void setDispstate(String dispstate) {
                this.dispstate = dispstate;
            }

            public String getWorker_name() {
                return worker_name;
            }

            public void setWorker_name(String worker_name) {
                this.worker_name = worker_name;
            }

            public String getAccount_phone() {
                return account_phone;
            }

            public void setAccount_phone(String account_phone) {
                this.account_phone = account_phone;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getReporter_name() {
                return reporter_name;
            }

            public void setReporter_name(String reporter_name) {
                this.reporter_name = reporter_name;
            }

            public String getDisporder_source() {
                return disporder_source;
            }

            public void setDisporder_source(String disporder_source) {
                this.disporder_source = disporder_source;
            }

            public Long getWorker_id() {
                return worker_id;
            }

            public void setWorker_id(Long worker_id) {
                this.worker_id = worker_id;
            }

            public String getOrder_code() {
                return order_code;
            }

            public void setOrder_code(String order_code) {
                this.order_code = order_code;
            }

            public Long getDanger_id() {
                return danger_id;
            }

            public void setDanger_id(Long danger_id) {
                this.danger_id = danger_id;
            }

            public String getReport_time() {
                return report_time;
            }

            public void setReport_time(String report_time) {
                this.report_time = report_time;
            }

            public String getOrder_source() {
                return order_source;
            }

            public void setOrder_source(String order_source) {
                this.order_source = order_source;
            }

            public String getWork_content() {
                return work_content;
            }

            public void setWork_content(String work_content) {
                this.work_content = work_content;
            }

            public String getAccount_address() {
                return account_address;
            }

            public void setAccount_address(String account_address) {
                this.account_address = account_address;
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

            public Long getReport_user_id() {
                return report_user_id;
            }

            public void setReport_user_id(Long report_user_id) {
                this.report_user_id = report_user_id;
            }
        }
    }
}
