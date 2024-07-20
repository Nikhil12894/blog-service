package com.nk.blog.constants;

import java.time.ZoneId;

public abstract class DateConstants {

    private DateConstants() {}
    
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final ZoneId DEFAULT_ZONEID = ZoneId.of("Asia/Kolkata");

    public static final String DATE_TIME_PATTERN_REGX = "^([0-9]{4})-([0-1][0-9])-([0-3][0-9])\\s([0-1][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9])$";
    public static final String DATE_PATTERN_REGX = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|[3][01])";

}
