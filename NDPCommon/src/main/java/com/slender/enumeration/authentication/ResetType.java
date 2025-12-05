package com.slender.enumeration.authentication;

import com.slender.constant.user.UserColumn;

public enum ResetType {
    EMAIL,
    PHONE_NUMBER,
    USER_NAME,
    PASSWORD;

    public String getColumn(){
        return switch (this){
            case EMAIL -> UserColumn.EMAIL;
            case PHONE_NUMBER -> UserColumn.PHONE_NUMBER;
            case USER_NAME -> UserColumn.USER_NAME;
            case PASSWORD -> UserColumn.PASSWORD;
        };
    }
}
