package com.techen.smartgas.model;

import java.util.List;

public class SecurityResultBean {

    private Long planId;
    private Long accountId;
    private Integer repetitionFlag;
    private String state;
    private String elecSignature;
    private String description;
    private List<SecurityCheckItemValueListBean> securityCheckItemValueList;
    private List<SecurityCheckRecordPhotoListBean> securityCheckRecordPhotoList;
    private List<HiddenDangerListBean> hiddenDangerList;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getRepetitionFlag() {
        return repetitionFlag;
    }

    public void setRepetitionFlag(Integer repetitionFlag) {
        this.repetitionFlag = repetitionFlag;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getElecSignature() {
        return elecSignature;
    }

    public void setElecSignature(String elecSignature) {
        this.elecSignature = elecSignature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<HiddenDangerListBean> getHiddenDangerList() {
        return hiddenDangerList;
    }

    public void setHiddenDangerList(List<HiddenDangerListBean> hiddenDangerList) {
        this.hiddenDangerList = hiddenDangerList;
    }

    public static class SecurityCheckItemValueListBean {
        private Long item_id;
        private String item_value;

        public Long getItem_id() {
            return item_id;
        }

        public void setItem_id(Long item_id) {
            this.item_id = item_id;
        }

        public String getItem_value() {
            return item_value;
        }

        public void setItem_value(String item_value) {
            this.item_value = item_value;
        }
    }

    public static class SecurityCheckRecordPhotoListBean {
        private Long group_id;
        private String photo_url;

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

    public static class HiddenDangerListBean {
        private Long groupId;
        private Long itemId;
        private String infoSource;
        private String description;
        private List<HiddenDangerInfoPhotoListBean> hiddenDangerInfoPhotoList;
        private WorkOrderRepairBean workOrderRepair;

        public Long getGroupId() {
            return groupId;
        }

        public void setGroupId(Long groupId) {
            this.groupId = groupId;
        }

        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public String getInfoSource() {
            return infoSource;
        }

        public void setInfoSource(String infoSource) {
            this.infoSource = infoSource;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<HiddenDangerInfoPhotoListBean> getHiddenDangerInfoPhotoList() {
            return hiddenDangerInfoPhotoList;
        }

        public void setHiddenDangerInfoPhotoList(List<HiddenDangerInfoPhotoListBean> hiddenDangerInfoPhotoList) {
            this.hiddenDangerInfoPhotoList = hiddenDangerInfoPhotoList;
        }

        public WorkOrderRepairBean getWorkOrderRepair() {
            return workOrderRepair;
        }

        public void setWorkOrderRepair(WorkOrderRepairBean workOrderRepair) {
            this.workOrderRepair = workOrderRepair;
        }

        public static class WorkOrderRepairBean {
            private String work_content;

            public String getWork_content() {
                return work_content;
            }

            public void setWork_content(String work_content) {
                this.work_content = work_content;
            }
        }

        public static class HiddenDangerInfoPhotoListBean {
            private String photo_url;

            public String getPhoto_url() {
                return photo_url;
            }

            public void setPhoto_url(String photo_url) {
                this.photo_url = photo_url;
            }
        }
    }
}
