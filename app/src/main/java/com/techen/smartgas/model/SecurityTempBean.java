package com.techen.smartgas.model;

import org.itheima.recycler.bean.BasePageBean;

import java.util.List;

public class SecurityTempBean extends BasePageBean<SecurityTempBean.ResultBean.GroupInfoListBean> {

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
    public List<ResultBean.GroupInfoListBean> getItemDatas() {
        return result.groupInfoList;
    }

    public static class ResultBean {
        private Long id;
        private Long org_id;
        private String disporg_id;
        private String template_name;
        private String template_code;
        private String state;
        private String state_name;
        private Object sortcode;
        private List<GroupInfoListBean> groupInfoList;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getOrg_id() {
            return org_id;
        }

        public void setOrg_id(Long org_id) {
            this.org_id = org_id;
        }

        public String getDisporg_id() {
            return disporg_id;
        }

        public void setDisporg_id(String disporg_id) {
            this.disporg_id = disporg_id;
        }

        public String getTemplate_name() {
            return template_name;
        }

        public void setTemplate_name(String template_name) {
            this.template_name = template_name;
        }

        public String getTemplate_code() {
            return template_code;
        }

        public void setTemplate_code(String template_code) {
            this.template_code = template_code;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getState_name() {
            return state_name;
        }

        public void setState_name(String state_name) {
            this.state_name = state_name;
        }

        public Object getSortcode() {
            return sortcode;
        }

        public void setSortcode(Object sortcode) {
            this.sortcode = sortcode;
        }

        public List<GroupInfoListBean> getGroupInfoList() {
            return groupInfoList;
        }

        public void setGroupInfoList(List<GroupInfoListBean> groupInfoList) {
            this.groupInfoList = groupInfoList;
        }

        public static class GroupInfoListBean {
            private Long id;
            private String group_name;
            private String group_code;
            private String group_image;
            private String group_type;
            private Integer hidden_danger_flag;
            private Integer sortcode;
            private List<ItemListBean> itemList;

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getGroup_name() {
                return group_name;
            }

            public void setGroup_name(String group_name) {
                this.group_name = group_name;
            }

            public String getGroup_code() {
                return group_code;
            }

            public void setGroup_code(String group_code) {
                this.group_code = group_code;
            }

            public String getGroup_image() {
                return group_image;
            }

            public void setGroup_image(String group_image) {
                this.group_image = group_image;
            }

            public String getGroup_type() {
                return group_type;
            }

            public void setGroup_type(String group_type) {
                this.group_type = group_type;
            }

            public Integer getHidden_danger_flag() {
                return hidden_danger_flag;
            }

            public void setHidden_danger_flag(Integer hidden_danger_flag) {
                this.hidden_danger_flag = hidden_danger_flag;
            }

            public Object getSortcode() {
                return sortcode;
            }

            public void setSortcode(Integer sortcode) {
                this.sortcode = sortcode;
            }

            public List<ItemListBean> getItemList() {
                return itemList;
            }

            public void setItemList(List<ItemListBean> itemList) {
                this.itemList = itemList;
            }

            public static class ItemListBean {
                private Long id;
                private Object createuser;
                private Object createon;
                private Object createorgid;
                private Object modifieduser;
                private Object modifiedon;
                private Object sortcode;
                private Object description;
                private Object enabled;
                private Long template_id;
                private Long group_id;
                private String item_name;
                private String item_code;
                private String item_type;
                private String item_option;
                private String placeholder;
                private String verification;
                private String default_value;
                private Integer required_flag;
                private Integer visible_flag;
                private Integer editable_flag;
                private Integer hidden_danger_flag;
                private String hidden_danger_judge;
                private String hidden_danger_value;
                private String rectification_method;
                private String tableName;
                private List<String> declaredFieldNames;

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
                    this.id = id;
                }

                public Object getCreateuser() {
                    return createuser;
                }

                public void setCreateuser(Object createuser) {
                    this.createuser = createuser;
                }

                public Object getCreateon() {
                    return createon;
                }

                public void setCreateon(Object createon) {
                    this.createon = createon;
                }

                public Object getCreateorgid() {
                    return createorgid;
                }

                public void setCreateorgid(Object createorgid) {
                    this.createorgid = createorgid;
                }

                public Object getModifieduser() {
                    return modifieduser;
                }

                public void setModifieduser(Object modifieduser) {
                    this.modifieduser = modifieduser;
                }

                public Object getModifiedon() {
                    return modifiedon;
                }

                public void setModifiedon(Object modifiedon) {
                    this.modifiedon = modifiedon;
                }

                public Object getSortcode() {
                    return sortcode;
                }

                public void setSortcode(Object sortcode) {
                    this.sortcode = sortcode;
                }

                public Object getDescription() {
                    return description;
                }

                public void setDescription(Object description) {
                    this.description = description;
                }

                public Object getEnabled() {
                    return enabled;
                }

                public void setEnabled(Object enabled) {
                    this.enabled = enabled;
                }

                public Long getTemplate_id() {
                    return template_id;
                }

                public void setTemplate_id(Long template_id) {
                    this.template_id = template_id;
                }

                public Long getGroup_id() {
                    return group_id;
                }

                public void setGroup_id(Long group_id) {
                    this.group_id = group_id;
                }

                public String getItem_name() {
                    return item_name;
                }

                public void setItem_name(String item_name) {
                    this.item_name = item_name;
                }

                public String getItem_code() {
                    return item_code;
                }

                public void setItem_code(String item_code) {
                    this.item_code = item_code;
                }

                public String getItem_type() {
                    return item_type;
                }

                public void setItem_type(String item_type) {
                    this.item_type = item_type;
                }

                public String getItem_option() {
                    return item_option;
                }

                public void setItem_option(String item_option) {
                    this.item_option = item_option;
                }

                public String getPlaceholder() {
                    return placeholder;
                }

                public void setPlaceholder(String placeholder) {
                    this.placeholder = placeholder;
                }

                public String getVerification() {
                    return verification;
                }

                public void setVerification(String verification) {
                    this.verification = verification;
                }

                public String getDefault_value() {
                    return default_value;
                }

                public void setDefault_value(String default_value) {
                    this.default_value = default_value;
                }

                public Integer getRequired_flag() {
                    return required_flag;
                }

                public void setRequired_flag(Integer required_flag) {
                    this.required_flag = required_flag;
                }

                public Integer getVisible_flag() {
                    return visible_flag;
                }

                public void setVisible_flag(Integer visible_flag) {
                    this.visible_flag = visible_flag;
                }

                public Integer getEditable_flag() {
                    return editable_flag;
                }

                public void setEditable_flag(Integer editable_flag) {
                    this.editable_flag = editable_flag;
                }

                public Integer getHidden_danger_flag() {
                    return hidden_danger_flag;
                }

                public void setHidden_danger_flag(Integer hidden_danger_flag) {
                    this.hidden_danger_flag = hidden_danger_flag;
                }

                public String getHidden_danger_judge() {
                    return hidden_danger_judge;
                }

                public void setHidden_danger_judge(String hidden_danger_judge) {
                    this.hidden_danger_judge = hidden_danger_judge;
                }

                public String getHidden_danger_value() {
                    return hidden_danger_value;
                }

                public void setHidden_danger_value(String hidden_danger_value) {
                    this.hidden_danger_value = hidden_danger_value;
                }

                public String getRectification_method() {
                    return rectification_method;
                }

                public void setRectification_method(String rectification_method) {
                    this.rectification_method = rectification_method;
                }

                public String getTableName() {
                    return tableName;
                }

                public void setTableName(String tableName) {
                    this.tableName = tableName;
                }

                public List<String> getDeclaredFieldNames() {
                    return declaredFieldNames;
                }

                public void setDeclaredFieldNames(List<String> declaredFieldNames) {
                    this.declaredFieldNames = declaredFieldNames;
                }
            }
        }
    }
}
