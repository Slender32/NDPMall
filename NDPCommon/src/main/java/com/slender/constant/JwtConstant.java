package com.slender.constant;

import com.slender.utils.TimeToolkit;

public interface JwtConstant {
    int ACCESS_TOKEN_EXPIRATION_TIME = TimeToolkit.Unit.HOUR/2;
    int REFRESH_TOKEN_EXPIRATION_TIME = TimeToolkit.Unit.DAY*3;

    String ACCESS_KEY = "slender@0fjjjroiner0jhf84hf920fj03j50gj2n295h5JVhf-0J-vjJR-nc03i3nr0duegrs4545swef";
    String REFRESH_KEY = "slender@0fjjjroiner0jhf84hb39f348g34ij2n295h5JVhf-0J-vjJR-nc03i3nr0duegrsdccs4wef";
}
