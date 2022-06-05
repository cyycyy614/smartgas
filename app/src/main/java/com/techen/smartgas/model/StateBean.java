package com.techen.smartgas.model;

public class StateBean {

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
        private Integer normal;
        private Integer undo;
        private Integer reject;
        private Integer danger;
        private Integer miss;

        public Integer getNormal() {
            return normal;
        }

        public void setNormal(Integer normal) {
            this.normal = normal;
        }

        public Integer getUndo() {
            return undo;
        }

        public void setUndo(Integer undo) {
            this.undo = undo;
        }

        public Integer getReject() {
            return reject;
        }

        public void setReject(Integer reject) {
            this.reject = reject;
        }

        public Integer getDanger() {
            return danger;
        }

        public void setDanger(Integer danger) {
            this.danger = danger;
        }

        public Integer getMiss() {
            return miss;
        }

        public void setMiss(Integer miss) {
            this.miss = miss;
        }
    }
}
