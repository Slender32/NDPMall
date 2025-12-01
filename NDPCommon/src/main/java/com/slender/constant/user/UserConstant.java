package com.slender.constant.user;

public interface UserConstant {
    
    interface Authority {
        String USER = "USER";
        String MERCHANT = "MERCHANT";
        String ADMINISTRATION = "ADMINISTRATION";
    }

    interface Status {
        String NORMAL = "NORMAL";
        String FROZEN = "FROZEN";
    }

    interface Gender {
        String MALE = "MALE";
        String FEMALE = "FEMALE";
        String PROTECTED = "PROTECTED";
    }


    interface Message {
        String VALIDATION_ERROR = "用户信息验证失败";
    }
}

