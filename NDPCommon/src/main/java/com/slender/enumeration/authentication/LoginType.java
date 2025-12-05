package com.slender.enumeration.authentication;

import com.slender.constant.user.UserColumn;

public enum LoginType {
    USER_NAME,
    PHONE_NUMBER,
    EMAIL;

    public String getDataBaseColumn(){
        return switch (this){
            case USER_NAME -> UserColumn.USER_NAME;
            case PHONE_NUMBER -> UserColumn.PHONE_NUMBER;
            case EMAIL -> UserColumn.EMAIL;
        };
    }
}
