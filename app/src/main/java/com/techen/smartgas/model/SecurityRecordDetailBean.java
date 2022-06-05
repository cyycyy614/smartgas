package com.techen.smartgas.model;

import java.util.List;

public class SecurityRecordDetailBean {

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
        private Integer repetition_flag;
        private String state;
        private String disp_state;
        private String elec_signature;
        private String operate_time;
        private AccountBean account;
        private List<SecurityCheckItemValueListBean> securityCheckItemValueList;
        private List<SecurityCheckRecordPhotoListBean> securityCheckRecordPhotoList;
        private List<HiddenDangerInfoListBean> hiddenDangerInfoList;
        private List<WorkOrderListBean> workOrderList;

        public Integer getRepetition_flag() {
            return repetition_flag;
        }

        public void setRepetition_flag(Integer repetition_flag) {
            this.repetition_flag = repetition_flag;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getDisp_state() {
            return disp_state;
        }

        public void setDisp_state(String disp_state) {
            this.disp_state = disp_state;
        }

        public String getElec_signature() {
            return elec_signature;
        }

        public void setElec_signature(String elec_signature) {
            this.elec_signature = elec_signature;
        }

        public String getOperate_time() {
            return operate_time;
        }

        public void setOperate_time(String operate_time) {
            this.operate_time = operate_time;
        }

        public AccountBean getAccount() {
            return account;
        }

        public void setAccount(AccountBean account) {
            this.account = account;
        }

        public List<SecurityCheckItemValueListBean> getSecurityCheckItemValueList() {
            return securityCheckItemValueList;
        }

        public void setSecurityCheckItemValueList(List<SecurityCheckItemValueListBean> securityCheckItemValueList) {
            this.securityCheckItemValueList = securityCheckItemValueList;
        }

        public List<SecurityCheckRecordPhotoListBean> getSecurityCheckRecordPhotoList() {
            return securityCheckRecordPhotoList;
        }

        public void setSecurityCheckRecordPhotoList(List<SecurityCheckRecordPhotoListBean> securityCheckRecordPhotoList) {
            this.securityCheckRecordPhotoList = securityCheckRecordPhotoList;
        }

        public List<HiddenDangerInfoListBean> getHiddenDangerInfoList() {
            return hiddenDangerInfoList;
        }

        public void setHiddenDangerInfoList(List<HiddenDangerInfoListBean> hiddenDangerInfoList) {
            this.hiddenDangerInfoList = hiddenDangerInfoList;
        }

        public List<WorkOrderListBean> getWorkOrderList() {
            return workOrderList;
        }

        public void setWorkOrderList(List<WorkOrderListBean> workOrderList) {
            this.workOrderList = workOrderList;
        }

        public static class AccountBean {
            private Long id;
            private String cons_no;
            private String cons_name;
            private String cons_addr;
            private String cons_tel;

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
        }

        public static class SecurityCheckItemValueListBean {
            private Long id;
            private Long item_id;
            private Long record_id;
            private String item_value;

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public Long getItem_id() {
                return item_id;
            }

            public void setItem_id(Long item_id) {
                this.item_id = item_id;
            }

            public Long getRecord_id() {
                return record_id;
            }

            public void setRecord_id(Long record_id) {
                this.record_id = record_id;
            }

            public String getItem_value() {
                return item_value;
            }

            public void setItem_value(String item_value) {
                this.item_value = item_value;
            }
        }

        public static class SecurityCheckRecordPhotoListBean {
            private Long id;
            private Long record_id;
            private Long group_id;
            private String photo_url;

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public Long getRecord_id() {
                return record_id;
            }

            public void setRecord_id(Long record_id) {
                this.record_id = record_id;
            }

            public Long getGroup_id() {
                return group_id;
            }

            public void setGroup_id(Long group_id) {
                this.group_id = group_id;
            }

            public String getPhoto_url() {
                return photo_url;
            }

            public void setPhoto_url(String photo_url) {
                this.photo_url = photo_url;
            }
        }

        public static class HiddenDangerInfoListBean {
            private Long id;
            private Long record_id;
            private Object handle_time;
            private Long group_id;
            private Long item_id;
            private String report_time;
            private Object handle_user_id;
            private String description;
            private String state;
            private String disp_state;
            private Long report_user_id;
            private String info_source;
            private List<PhotolistBean> photolist;

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public Long getRecord_id() {
                return record_id;
            }

            public void setRecord_id(Long record_id) {
                this.record_id = record_id;
            }

            public Object getHandle_time() {
                return handle_time;
            }

            public void setHandle_time(Object handle_time) {
                this.handle_time = handle_time;
            }

            public Long getGroup_id() {
                return group_id;
            }

            public void setGroup_id(Long group_id) {
                this.group_id = group_id;
            }

            public Long getItem_id() {
                return item_id;
            }

            public void setItem_id(Long item_id) {
                this.item_id = item_id;
            }

            public String getReport_time() {
                return report_time;
            }

            public void setReport_time(String report_time) {
                this.report_time = report_time;
            }

            public Object getHandle_user_id() {
                return handle_user_id;
            }

            public void setHandle_user_id(Object handle_user_id) {
                this.handle_user_id = handle_user_id;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getDisp_state() {
                return disp_state;
            }

            public void setDisp_state(String disp_state) {
                this.disp_state = disp_state;
            }

            public Long getReport_user_id() {
                return report_user_id;
            }

            public void setReport_user_id(Long report_user_id) {
                this.report_user_id = report_user_id;
            }

            public String getInfo_source() {
                return info_source;
            }

            public void setInfo_source(String info_source) {
                this.info_source = info_source;
            }

            public List<PhotolistBean> getPhotolist() {
                return photolist;
            }

            public void setPhotolist(List<PhotolistBean> photolist) {
                this.photolist = photolist;
            }

            public static class PhotolistBean {
                private Long id;
                private Long danger_id;
                private String photo_url;

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
                    this.id = id;
                }

                public Long getDanger_id() {
                    return danger_id;
                }

                public void setDanger_id(Long danger_id) {
                    this.danger_id = danger_id;
                }

                public String getPhoto_url() {
                    return photo_url;
                }

                public void setPhoto_url(String photo_url) {
                    this.photo_url = photo_url;
                }
            }
        }

        public static class WorkOrderListBean {
            private Object appointment_time;
            private String account_phone;
            private Object description;
            private Object worker_id;
            private String order_code;
            private Long danger_id;
            private String report_time;
            private String order_source;
            private String disp_order_source;
            private String work_content;
            private String account_address;
            private Long id;
            private String state;
            private String disp_state;
            private Long report_user_id;
            private List<RecordlistBean> recordlist;

            public Object getAppointment_time() {
                return appointment_time;
            }

            public void setAppointment_time(Object appointment_time) {
                this.appointment_time = appointment_time;
            }

            public String getAccount_phone() {
                return account_phone;
            }

            public void setAccount_phone(String account_phone) {
                this.account_phone = account_phone;
            }

            public Object getDescription() {
                return description;
            }

            public void setDescription(Object description) {
                this.description = description;
            }

            public Object getWorker_id() {
                return worker_id;
            }

            public void setWorker_id(Object worker_id) {
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

            public String getDisp_order_source() {
                return disp_order_source;
            }

            public void setDisp_order_source(String disp_order_source) {
                this.disp_order_source = disp_order_source;
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

            public String getDisp_state() {
                return disp_state;
            }

            public void setDisp_state(String disp_state) {
                this.disp_state = disp_state;
            }

            public Long getReport_user_id() {
                return report_user_id;
            }

            public void setReport_user_id(Long report_user_id) {
                this.report_user_id = report_user_id;
            }

            public List<RecordlistBean> getRecordlist() {
                return recordlist;
            }

            public void setRecordlist(List<RecordlistBean> recordlist) {
                this.recordlist = recordlist;
            }

            public static class RecordlistBean {
                private Long id;
                private Long repair_id;
                private String repair_content;
                private String repair_time;
                private Long worker_id;
                private List<PhotolistBean> photolist;

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
                    this.id = id;
                }

                public Long getRepair_id() {
                    return repair_id;
                }

                public void setRepair_id(Long repair_id) {
                    this.repair_id = repair_id;
                }

                public String getRepair_content() {
                    return repair_content;
                }

                public void setRepair_content(String repair_content) {
                    this.repair_content = repair_content;
                }

                public String getRepair_time() {
                    return repair_time;
                }

                public void setRepair_time(String repair_time) {
                    this.repair_time = repair_time;
                }

                public Long getWorker_id() {
                    return worker_id;
                }

                public void setWorker_id(Long worker_id) {
                    this.worker_id = worker_id;
                }

                public List<PhotolistBean> getPhotolist() {
                    return photolist;
                }

                public void setPhotolist(List<PhotolistBean> photolist) {
                    this.photolist = photolist;
                }

                public static class PhotolistBean {
                    private Long id;
                    private Long record_id;
                    private String photo_url;

                    public Long getId() {
                        return id;
                    }

                    public void setId(Long id) {
                        this.id = id;
                    }

                    public Long getRecord_id() {
                        return record_id;
                    }

                    public void setRecord_id(Long record_id) {
                        this.record_id = record_id;
                    }

                    public String getPhoto_url() {
                        return photo_url;
                    }

                    public void setPhoto_url(String photo_url) {
                        this.photo_url = photo_url;
                    }
                }
            }
        }
    }
}
