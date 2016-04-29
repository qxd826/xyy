package com.example.xyy.xyyapplication.source.common;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 16/4/29.
 */
public class DateUtil {

    public static String convertDateToStr(Date date, String formatStr) {
        if (date == null || StringUtils.isBlank(formatStr)) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat(formatStr);
        return f.format(date);
    }

    public static String convertDateToYMDHMS(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(date);
    }

    public static String convertDateToYMDHM(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return f.format(date);
    }
}
