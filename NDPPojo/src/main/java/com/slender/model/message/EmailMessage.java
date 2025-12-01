package com.slender.model.message;

import com.slender.enumeration.authentication.CaptchaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {
    private CaptchaType type;
    private String toEmail;
}
