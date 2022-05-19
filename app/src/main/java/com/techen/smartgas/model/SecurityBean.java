package com.techen.smartgas.model;

import org.itheima.recycler.bean.BasePageBean;

import java.util.List;

public class SecurityBean extends BasePageBean<SecurityBean.ResultBean.ItemsBean> {

    private Integer code;
    private String message;
    private ResultBean result;
    private String time;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public List<ResultBean.ItemsBean> getItemDatas() {
        return result.items;
    }

    public static class ResultBean {
        private List<ItemsBean> items;
        private String nextPageToken;
        private String prevPageToken;
        private Integer requestCount;
        private Integer responseCount;
        private Integer totalResults;

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public String getNextPageToken() {
            return nextPageToken;
        }

        public void setNextPageToken(String nextPageToken) {
            this.nextPageToken = nextPageToken;
        }

        public String getPrevPageToken() {
            return prevPageToken;
        }

        public void setPrevPageToken(String prevPageToken) {
            this.prevPageToken = prevPageToken;
        }

        public Integer getRequestCount() {
            return requestCount;
        }

        public void setRequestCount(Integer requestCount) {
            this.requestCount = requestCount;
        }

        public Integer getResponseCount() {
            return responseCount;
        }

        public void setResponseCount(Integer responseCount) {
            this.responseCount = responseCount;
        }

        public Integer getTotalResults() {
            return totalResults;
        }

        public void setTotalResults(Integer totalResults) {
            this.totalResults = totalResults;
        }

        public static class ItemsBean {
            private String detail;
            private String href;
            private Integer id;
            private String img;
            private String name;
            private String pubDate;
            private Integer type;
            private Integer weight;

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPubDate() {
                return pubDate;
            }

            public void setPubDate(String pubDate) {
                this.pubDate = pubDate;
            }

            public Integer getType() {
                return type;
            }

            public void setType(Integer type) {
                this.type = type;
            }

            public Integer getWeight() {
                return weight;
            }

            public void setWeight(Integer weight) {
                this.weight = weight;
            }
        }
    }
}
