package com.techen.smartgas.model;

public class SecurityInfoBean {

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
        private LastRepetitionPlanBean lastRepetitionPlan;
        private LastSecurityPlanBean lastSecurityPlan;
        private LastTimeSpanPlanInfoBean lastTimeSpanPlanInfo;

        public LastRepetitionPlanBean getLastRepetitionPlan() {
            return lastRepetitionPlan;
        }

        public void setLastRepetitionPlan(LastRepetitionPlanBean lastRepetitionPlan) {
            this.lastRepetitionPlan = lastRepetitionPlan;
        }

        public LastSecurityPlanBean getLastSecurityPlan() {
            return lastSecurityPlan;
        }

        public void setLastSecurityPlan(LastSecurityPlanBean lastSecurityPlan) {
            this.lastSecurityPlan = lastSecurityPlan;
        }

        public LastTimeSpanPlanInfoBean getLastTimeSpanPlanInfo() {
            return lastTimeSpanPlanInfo;
        }

        public void setLastTimeSpanPlanInfo(LastTimeSpanPlanInfoBean lastTimeSpanPlanInfo) {
            this.lastTimeSpanPlanInfo = lastTimeSpanPlanInfo;
        }

        public static class LastRepetitionPlanBean {
            private Integer dangerAccountAmount;
            private Integer totalAmount;
            private String endDate;
            private Integer inAmount;
            private String dispState;
            private RecordStateInfoBean recordStateInfo;
            private String planName;
            private Integer repetitionAccountAmount;
            private String startDate;

            public Integer getDangerAccountAmount() {
                return dangerAccountAmount;
            }

            public void setDangerAccountAmount(Integer dangerAccountAmount) {
                this.dangerAccountAmount = dangerAccountAmount;
            }

            public Integer getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(Integer totalAmount) {
                this.totalAmount = totalAmount;
            }

            public String getEndDate() {
                return endDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
            }

            public Integer getInAmount() {
                return inAmount;
            }

            public void setInAmount(Integer inAmount) {
                this.inAmount = inAmount;
            }

            public String getDispState() {
                return dispState;
            }

            public void setDispState(String dispState) {
                this.dispState = dispState;
            }

            public RecordStateInfoBean getRecordStateInfo() {
                return recordStateInfo;
            }

            public void setRecordStateInfo(RecordStateInfoBean recordStateInfo) {
                this.recordStateInfo = recordStateInfo;
            }

            public String getPlanName() {
                return planName;
            }

            public void setPlanName(String planName) {
                this.planName = planName;
            }

            public Integer getRepetitionAccountAmount() {
                return repetitionAccountAmount;
            }

            public void setRepetitionAccountAmount(Integer repetitionAccountAmount) {
                this.repetitionAccountAmount = repetitionAccountAmount;
            }

            public String getStartDate() {
                return startDate;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }

            public static class RecordStateInfoBean {
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

        public static class LastSecurityPlanBean {
            private Integer dangerAccountAmount;
            private Integer totalAmount;
            private String endDate;
            private Integer inAmount;
            private String dispState;
            private RecordStateInfoBean recordStateInfo;
            private String planName;
            private Integer repetitionAccountAmount;
            private String startDate;

            public Integer getDangerAccountAmount() {
                return dangerAccountAmount;
            }

            public void setDangerAccountAmount(Integer dangerAccountAmount) {
                this.dangerAccountAmount = dangerAccountAmount;
            }

            public Integer getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(Integer totalAmount) {
                this.totalAmount = totalAmount;
            }

            public String getEndDate() {
                return endDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
            }

            public Integer getInAmount() {
                return inAmount;
            }

            public void setInAmount(Integer inAmount) {
                this.inAmount = inAmount;
            }

            public String getDispState() {
                return dispState;
            }

            public void setDispState(String dispState) {
                this.dispState = dispState;
            }

            public RecordStateInfoBean getRecordStateInfo() {
                return recordStateInfo;
            }

            public void setRecordStateInfo(RecordStateInfoBean recordStateInfo) {
                this.recordStateInfo = recordStateInfo;
            }

            public String getPlanName() {
                return planName;
            }

            public void setPlanName(String planName) {
                this.planName = planName;
            }

            public Integer getRepetitionAccountAmount() {
                return repetitionAccountAmount;
            }

            public void setRepetitionAccountAmount(Integer repetitionAccountAmount) {
                this.repetitionAccountAmount = repetitionAccountAmount;
            }

            public String getStartDate() {
                return startDate;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }

            public static class RecordStateInfoBean {
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

        public static class LastTimeSpanPlanInfoBean {
            private Integer securityAmount;
            private Integer securityUncloseAmount;
            private Integer dangerAmount;
            private Integer repetitionUncloseAmount;
            private Integer repetitionAmount;

            public Integer getSecurityAmount() {
                return securityAmount;
            }

            public void setSecurityAmount(Integer securityAmount) {
                this.securityAmount = securityAmount;
            }

            public Integer getSecurityUncloseAmount() {
                return securityUncloseAmount;
            }

            public void setSecurityUncloseAmount(Integer securityUncloseAmount) {
                this.securityUncloseAmount = securityUncloseAmount;
            }

            public Integer getDangerAmount() {
                return dangerAmount;
            }

            public void setDangerAmount(Integer dangerAmount) {
                this.dangerAmount = dangerAmount;
            }

            public Integer getRepetitionUncloseAmount() {
                return repetitionUncloseAmount;
            }

            public void setRepetitionUncloseAmount(Integer repetitionUncloseAmount) {
                this.repetitionUncloseAmount = repetitionUncloseAmount;
            }

            public Integer getRepetitionAmount() {
                return repetitionAmount;
            }

            public void setRepetitionAmount(Integer repetitionAmount) {
                this.repetitionAmount = repetitionAmount;
            }
        }
    }
}
