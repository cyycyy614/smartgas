package com.techen.smartgas.model;

import java.util.List;

public class OrderInfoBean {

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
        private Integer handlingAmount;
        private Integer handledAmount;
        private List<OrderInfoListBean> orderInfoList;

        public Integer getHandlingAmount() {
            return handlingAmount;
        }

        public void setHandlingAmount(Integer handlingAmount) {
            this.handlingAmount = handlingAmount;
        }

        public Integer getHandledAmount() {
            return handledAmount;
        }

        public void setHandledAmount(Integer handledAmount) {
            this.handledAmount = handledAmount;
        }

        public List<OrderInfoListBean> getOrderInfoList() {
            return orderInfoList;
        }

        public void setOrderInfoList(List<OrderInfoListBean> orderInfoList) {
            this.orderInfoList = orderInfoList;
        }

        public static class OrderInfoListBean {
            private String appointment_time;
            private String account_phone;
            private String mobile;
            private Object description;
            private Long worker_id;
            private String order_code;
            private String disp_order_source;
            private Object danger_id;
            private String report_time;
            private String order_source;
            private String work_content;
            private String name;
            private String account_address;
            private Long id;
            private String state;
            private Long report_user_id;
            private String disp_state;

            public String getAppointment_time() {
                return appointment_time;
            }

            public void setAppointment_time(String appointment_time) {
                this.appointment_time = appointment_time;
            }

            public String getAccount_phone() {
                return account_phone;
            }

            public void setAccount_phone(String account_phone) {
                this.account_phone = account_phone;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public Object getDescription() {
                return description;
            }

            public void setDescription(Object description) {
                this.description = description;
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

            public String getDisp_order_source() {
                return disp_order_source;
            }

            public void setDisp_order_source(String disp_order_source) {
                this.disp_order_source = disp_order_source;
            }

            public Object getDanger_id() {
                return danger_id;
            }

            public void setDanger_id(Object danger_id) {
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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public String getDisp_state() {
                return disp_state;
            }

            public void setDisp_state(String disp_state) {
                this.disp_state = disp_state;
            }
        }
    }
}
