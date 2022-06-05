package com.techen.smartgas.model;

import java.util.List;

public class SecurityDangerBean {

    private Long groupId;
    private String groupName;
    private List<DangerListBean> dangerList;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<DangerListBean> getDangerList() {
        return dangerList;
    }

    public void setDangerList(List<DangerListBean> dangerList) {
        this.dangerList = dangerList;
    }

    public static class DangerListBean {
        private String itemName;
        private String itemVal;
        private String isReport;
        private String method;
        private String demo;
        private List<ImgListBean> imgList;

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemVal() {
            return itemVal;
        }

        public void setItemVal(String itemVal) {
            this.itemVal = itemVal;
        }

        public String getIsReport() {
            return isReport;
        }

        public void setIsReport(String isReport) {
            this.isReport = isReport;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getDemo() {
            return demo;
        }

        public void setDemo(String demo) {
            this.demo = demo;
        }

        public List<ImgListBean> getImgList() {
            return imgList;
        }

        public void setImgList(List<ImgListBean> imgList) {
            this.imgList = imgList;
        }

        public static class ImgListBean {
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
