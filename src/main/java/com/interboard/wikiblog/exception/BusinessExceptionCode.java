package com.interboard.wikiblog.exception;

public enum BusinessExceptionCode {

    USER_LOGIN_NAME_EXIST("登入名已存在"),
    LOGIN_USER_ERROR("帳號或密碼錯誤"),
    VOTE_REPEAT("已點讚過"),
    ;

    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
