package com.slender.enumeration.authentication;

public enum LoginType {
    USER_NAME,
    PHONE_NUMBER,
    EMAIL;

    public String getDataBaseColumn(){
        return switch (this){
            case USER_NAME -> "user_name";
            case PHONE_NUMBER -> "phone_number";
            case EMAIL -> "email";
        };
    }
}
