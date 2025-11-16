package com.handler.excel2word.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    public static final int TIMESTART_SUFFIX = 1;
    public static final int TIMEEND_SUFFIX = 2;
    public static final int SECSTART_SUFFIX = 3;
    public static final int SECEND_SUFFIX = 4;
    public static final String YYYYMM = "yyyyMM";
    public static final String YYYY = "yyyy";
    public static final String MM = "MM";
    public static final String TIME_START = " 00:00:00";
    public static final String TIME_END = " 23:59:59";
    public static final String SEC_START = ":00";
    public static final String SEC_END = ":59";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yyyyMMdd";
    private static int weeks = 0;

    public DateUtil() {
    }

    public static SimpleDateFormat simpleDateFormat(String format) {
        return simpleDateFormat(format, Locale.getDefault());
    }

    public static SimpleDateFormat simpleDateFormat(String format, Locale locale) {
        return new SimpleDateFormat(format, locale);
    }

    public static SimpleDateFormat simpleDateFormat() {
        return simpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static String trim(String str) {
        return str == null ? "" : str.trim();
    }

    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(5, 1);
        return calendar.getTime();
    }

    public static String getFirstDayOfMonth2(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(5, 1);
        return simpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    public static String getFisrtDayOfMonth(int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(2, month - 1);
        int firstDay = cal.getActualMinimum(5);
        cal.set(5, firstDay);
        return simpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    public static String getLastDayOfMonth(int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(2, month - 1);
        int firstDay = cal.getActualMaximum(5);
        cal.set(5, firstDay);
        return simpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(5, 1);
        calendar.roll(5, -1);
        return calendar.getTime();
    }

    public static String getNextMonthDateStr2() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(2, 1);
        SimpleDateFormat sdfLong = simpleDateFormat("yyyy-MM");
        String date = sdfLong.format(calendar.getTime());
        return date;
    }

    public static String getNextMonthDateStr3() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(2, -1);
        SimpleDateFormat sdfLong = simpleDateFormat("yyyy-MM");
        String date = sdfLong.format(calendar.getTime());
        return date;
    }

    public static long getCurLong() {
        SimpleDateFormat sdfLong = simpleDateFormat("yyyyMMddHHmmss");
        String date = sdfLong.format(new Date());
        return Long.parseLong(date);
    }

    public static long getCurMinuteLong() {
        SimpleDateFormat sdfLong = simpleDateFormat("yyyyMMddHHmm00");
        String date = sdfLong.format(new Date());
        return Long.parseLong(date);
    }

    public static long getCurrLong() {
        Date date = new Date();
        return date.getTime() / 1000L;
    }

    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = simpleDateFormat("yyyyMMddHHmmsss");
        String time = sdf.format(date);
        return time;
    }

    public static String getDateToString(Date date, String format) {
        SimpleDateFormat sdf = simpleDateFormat(format);
        String time = sdf.format(date);
        return time;
    }

    public static String getDateToString(Date date) {
        SimpleDateFormat sdf = simpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(date);
        return time;
    }

    public static String getNowDate() {
        Date date = new Date();
        SimpleDateFormat sdf = simpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(date);
        return time;
    }

    public static String getNowTime() {
        Date date = new Date();
        SimpleDateFormat sdf = simpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);
        return time;
    }

    public static String getNowTime(String timeZone) {
        Date date = new Date();
        SimpleDateFormat sdf = simpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        String time = sdf.format(date);
        return time;
    }

    public static String getCurrentDate(String str) {
        SimpleDateFormat format = simpleDateFormat(str);
        String date = format.format(Calendar.getInstance().getTime());
        return date;
    }

    public static String parseDate(String strDate) {
        strDate = trim(strDate);
        if (strDate.length() == 10) {
            StringBuffer buffer = new StringBuffer("");
            buffer.append(strDate.substring(0, 4));
            buffer.append(strDate.substring(5, 7));
            buffer.append(strDate.substring(8, 10));
            strDate = buffer.toString();
        }

        return strDate;
    }

    public static String parseDateEx(String strDate) {
        strDate = trim(strDate);
        if (strDate.length() == 8) {
            StringBuffer buffer = new StringBuffer("");
            buffer.append(strDate.substring(0, 4));
            buffer.append("-");
            buffer.append(strDate.substring(4, 6));
            buffer.append("-");
            buffer.append(strDate.substring(6, 8));
            strDate = buffer.toString();
        }

        return strDate;
    }

    public static synchronized int monthDay(int year, int mon) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, mon - 1);
        int maxDate = cal.getActualMaximum(5);
        return maxDate;
    }

    public static String getNowYear() {
        Date date = new Date();
        SimpleDateFormat sdf = simpleDateFormat("yyyy");
        String time = sdf.format(date);
        return time;
    }

    public static String getNowMonth() {
        Date date = new Date();
        SimpleDateFormat sdf = simpleDateFormat("MM");
        String time = sdf.format(date);
        return time;
    }

    public static String getNowYearMonth(Date date) {
        SimpleDateFormat sdf = simpleDateFormat("yyyyMM");
        String time = sdf.format(date);
        return time;
    }

    public static String getDiffDate(Integer diffday) {
        SimpleDateFormat sdf = simpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, diffday);
        return sdf.format(calendar.getTime());
    }

    public static String getDiffDate(String datestr, Integer diffday) {
        SimpleDateFormat sdf = simpleDateFormat("yyyy-MM-dd");
        String d = null;

        try {
            Date date = sdf.parse(datestr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(5, diffday);
            d = sdf.format(calendar.getTime());
        } catch (ParseException var6) {
            System.out.println(var6.getMessage());
        }

        return d;
    }

    public static Date getDiffDate(Date date, Integer diffday) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, diffday);
        return calendar.getTime();
    }

    public static String getDiffDateAgo(Integer diffday) {
        return getStrForDiffDateAgo(new Date(), diffday);
    }

    public static String getStrForDiffDate(Date date, Integer diffday) {
        SimpleDateFormat sdf = simpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, diffday);
        return sdf.format(calendar.getTime());
    }

    public static String getStrForDiffDateAgo(Date date, Integer diffday) {
        SimpleDateFormat sdf = simpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, -diffday);
        return sdf.format(calendar.getTime());
    }

    public static Date getParseTime(String datestr) {
        SimpleDateFormat sdf = simpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;

        try {
            date = sdf.parse(datestr);
        } catch (ParseException var4) {
            var4.printStackTrace();
        }

        return date;
    }

    public static Date getParseDate(String datestr) {
        SimpleDateFormat sdf = simpleDateFormat("yyyy-MM-dd");
        Date date = null;

        try {
            date = sdf.parse(datestr);
        } catch (ParseException var4) {
            var4.printStackTrace();
        }

        return date;
    }

    public static Date getDay(Date date, int n) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(5, n);
            return cal.getTime();
        } catch (Exception var3) {
            return null;
        }
    }

    public static Date getHour(Date date, int n) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(10, n);
            return cal.getTime();
        } catch (Exception var3) {
            return null;
        }
    }

    public static Date getMinute(Date date, int m) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(12, m);
            return cal.getTime();
        } catch (Exception var3) {
            return null;
        }
    }

    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(12);
    }

    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(11);
    }

    public static Date getSecond(Date date, int m) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(13, m);
            return cal.getTime();
        } catch (Exception var3) {
            return null;
        }
    }

    public static int getSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(13);
    }

    public static Date getMonth(Date date, int n) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(2, n);
            return cal.getTime();
        } catch (Exception var3) {
            return null;
        }
    }

    public static String getDateLimit(Integer n) {
        try {
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(2, n);
            return simpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        } catch (Exception var3) {
            return null;
        }
    }

    public static String getDateLimit(Integer n, String d) {
        try {
            Date date = simpleDateFormat("yyyy-MM-dd").parse(d);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(2, n);
            return simpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        } catch (Exception var4) {
            return null;
        }
    }

    public static String getNewFileName(String filename) {
        String temp = getCurrentTime();
        String newFilename = temp + filename.substring(filename.lastIndexOf("."));
        return newFilename;
    }

    public static String getFirstMonth() {
        SimpleDateFormat dateFormat = simpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, 1);
        Date DateTime = calendar.getTime();
        String sTime = dateFormat.format(DateTime);
        return sTime;
    }

    public static String getEndMonth() {
        SimpleDateFormat dateFormat = simpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, 1);
        calendar.roll(5, -1);
        Date endTime = calendar.getTime();
        String eTime = dateFormat.format(endTime);
        return eTime;
    }

    public static String getFirstMonth(String month) {
        SimpleDateFormat dateFormat = simpleDateFormat("yyyy-MM-dd");

        try {
            Date date = dateFormat.parse(month + "-01");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(5, 1);
            Date DateTime = calendar.getTime();
            String sTime = dateFormat.format(DateTime);
            return sTime;
        } catch (ParseException var6) {
            return null;
        }
    }

    public static String getEndMonth(String month) {
        SimpleDateFormat dateFormat = simpleDateFormat("yyyy-MM-dd");

        try {
            Date date = dateFormat.parse(month + "-01");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(5, 1);
            calendar.roll(5, -1);
            Date endTime = calendar.getTime();
            String eTime = dateFormat.format(endTime);
            return eTime;
        } catch (ParseException var6) {
            return null;
        }
    }

    public static String getNowAtteMonth(String d) throws ParseException {
        Date date = simpleDateFormat("yyyy-MM-dd").parse(d);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(5) > 20) {
            cal.add(2, 1);
        }

        String str = simpleDateFormat("yyyyMM").format(cal.getTime());
        return str;
    }

    public static List<String> getDateList(String beginDate, String endDate) {
        List<String> dateList = new ArrayList();
        SimpleDateFormat sdf = simpleDateFormat("yyyy-MM-dd");
        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        try {
            end.setTime(sdf.parse(endDate));
            begin.setTime(sdf.parse(beginDate));

            while(begin.before(end)) {
                dateList.add(getDateToString(begin.getTime()));
                begin.add(5, 1);
            }

            dateList.add(getDateToString(end.getTime()));
        } catch (ParseException var7) {
        }

        return dateList;
    }

    public static String formatDateToStr(Date date, String format) {
        if (date != null) {
            SimpleDateFormat df = simpleDateFormat(format);
            return df.format(date);
        } else {
            return null;
        }
    }

    public static String getFormatDate(Date timeDate) {
        SimpleDateFormat sdf = simpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(timeDate);
        return time;
    }

    public static Date getDateLast(Date date, int n) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(11, 23);
            cal.set(12, 59);
            cal.set(13, 59);
            cal.add(5, n);
            return cal.getTime();
        } catch (Exception var3) {
            return null;
        }
    }

    public static Date getDateFirst(Date date, int n) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(11, 0);
            cal.set(12, 0);
            cal.set(13, 0);
            cal.add(5, n);
            return cal.getTime();
        } catch (Exception var3) {
            return null;
        }
    }

    public static Date getDateLast(Date date) {
        if (date == null) {
            return null;
        } else {
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(11, 23);
                cal.set(12, 59);
                cal.set(13, 59);
                return cal.getTime();
            } catch (Exception var2) {
                return null;
            }
        }
    }

    public static Date getDateFirst(Date date) {
        if (date == null) {
            return null;
        } else {
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(11, 0);
                cal.set(12, 0);
                cal.set(13, 0);
                return cal.getTime();
            } catch (Exception var2) {
                return null;
            }
        }
    }

    public static Date getDate(Date date, int n, int hour, int minute, int second) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(11, hour);
            cal.set(12, minute);
            cal.set(13, second);
            cal.add(5, n);
            return cal.getTime();
        } catch (Exception var6) {
            return null;
        }
    }

    public static long[] getDistanceTimes(Date str1, Date str2) throws Exception {
        long day = 0L;
        long hour = 0L;
        long min = 0L;
        int flag = -1;
        long time1 = str1.getTime();
        long time2 = str2.getTime();
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
            flag = 0;
        } else {
            diff = time1 - time2;
        }

        day = diff / 86400000L;
        hour = diff / 3600000L - day * 24L;
        min = diff / 60000L - day * 24L * 60L - hour * 60L;
        int sec = (int)(diff / 1000L - day * 24L * 60L * 60L - hour * 60L * 60L - min * 60L);
        long[] times = new long[]{day, hour, min, (long)sec, (long)flag};
        return times;
    }

    public static long getTimeStampByDateDistance(Date start, Date end) {
        return end.getTime() - start.getTime();
    }

    public static Date strToDate(String str) {
        if (StringUtil.isBlank(str)) {
            return null;
        } else {
            DateFormat dateformat = simpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date tmp = null;

            try {
                tmp = dateformat.parse(str);
            } catch (ParseException var8) {
                dateformat = simpleDateFormat("yyyy-MM-dd HH:mm");

                try {
                    tmp = dateformat.parse(str);
                } catch (ParseException var7) {
                    dateformat = simpleDateFormat("yyyy-MM-dd");

                    try {
                        tmp = dateformat.parse(str);
                    } catch (ParseException var6) {
                        tmp = new Date();
                    }
                }
            }

            return tmp;
        }
    }

    public static String dateToMD(Date date) {
        return dateToStr(date, "MM-dd");
    }

    public static String dateToHM(Date date) {
        return dateToStr(date, "HH:mm");
    }

    public static String dateToHMSNo(Date date) {
        return dateToStr(date, "HHmmss");
    }

    public static String dateToYMD(Date date) {
        return dateToStr(date, "yyyy-MM-dd");
    }

    public static String dateToYMDHMS(Date date) {
        return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String dateToYMDHMSNo(Date date) {
        return dateToStr(date, "yyyyMMddHHmmss");
    }

    public static Date parseDate(int num) {
        Calendar cal = Calendar.getInstance();
        cal.add(5, num);
        DateFormat dateformat = simpleDateFormat("yyyy-MM-dd");

        try {
            return dateformat.parse(dateformat.format(cal.getTime()));
        } catch (ParseException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static boolean dateTimeBijiao(String beginDatetime, String endDatetime) {
        boolean flag = false;
        DateFormat df = simpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        try {
            c1.setTime(df.parse(beginDatetime));
            c2.setTime(df.parse(endDatetime));
        } catch (ParseException var7) {
            System.err.println("格式不正确");
        }

        int result = c1.compareTo(c2);
        if (result == 0) {
            flag = true;
        } else if (result < 0) {
            flag = false;
        } else {
            flag = true;
        }

        return flag;
    }

    public static boolean dateCompareByYmd(Date date, Date startDate, Date endDate) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        long curTime = dateCal.getTimeInMillis();
        Calendar starCal = Calendar.getInstance();
        starCal.setTime(startDate);
        long startTime = starCal.getTimeInMillis();
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        long endTime = endCal.getTimeInMillis();
        return curTime >= startTime && curTime <= endTime;
    }

    public static boolean dateCompareByYmdhms(Date date, Date startDate, Date endDate) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        dateCal.set(14, 0);
        long curTime = dateCal.getTimeInMillis();
        Calendar starCal = Calendar.getInstance();
        starCal.setTime(startDate);
        starCal.set(14, 0);
        long startTime = starCal.getTimeInMillis();
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        endCal.set(14, 0);
        long endTime = endCal.getTimeInMillis();
        return curTime >= startTime && curTime <= endTime;
    }

    public static boolean dateCompareByYmdhms(Date date, Date startDate) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        dateCal.set(14, 0);
        long curTime = dateCal.getTimeInMillis();
        Calendar starCal = Calendar.getInstance();
        starCal.setTime(startDate);
        starCal.set(14, 0);
        long startTime = starCal.getTimeInMillis();
        return startTime >= curTime;
    }

    public static String getTimeZone(String zone_type) throws Exception {
        Date date = new Date();
        SimpleDateFormat dateformat = simpleDateFormat("HH:mm:ssa E MMM dd,yyyy", Locale.US);
        TimeZone timeZone = TimeZone.getTimeZone(zone_type);
        dateformat.setTimeZone(timeZone);
        return dateformat.format(date);
    }

    public static String getTimeZoneJson(String zone_type) {
        Date date = new Date();
        DateFormat dateformat = simpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        TimeZone timeZone = TimeZone.getTimeZone(zone_type);
        dateformat.setTimeZone(timeZone);
        return dateformat.format(date);
    }

    public static String getTimeZoneYMD(String zone_type) {
        Date date = new Date();
        DateFormat dateformat = simpleDateFormat("yyyy-MM-dd", Locale.US);
        TimeZone timeZone = TimeZone.getTimeZone(zone_type);
        dateformat.setTimeZone(timeZone);
        return dateformat.format(date);
    }

    public static Date getAMESToUS(Date date) {
        return getHour(date, 12);
    }

    public static Date getUSToAMES(Date date) {
        return getHour(date, -12);
    }

    public static Date getUTCToUS(Date date) {
        return getHour(date, 8);
    }

    public static String dateToStr(Date date, String pattern, Locale locale) {
        if (date == null) {
            return null;
        } else {
            DateFormat dateformat = simpleDateFormat(pattern, locale);
            return dateformat.format(date);
        }
    }

    public static String dateToStr(Date date, String pattern) {
        return dateToStr(date, pattern, Locale.getDefault());
    }

    public static Date strToDate(String string, String format) {
        Date tmp = null;
        if (!StringUtil.isBlank(string) && !StringUtil.isBlank(format)) {
            DateFormat dateformat = simpleDateFormat(format);

            try {
                tmp = dateformat.parse(string);
            } catch (ParseException var5) {
                var5.printStackTrace();
            }

            return tmp;
        } else {
            return tmp;
        }
    }

    public static Date getDateStart(Date date) {
        if (date != null) {
            String day = dateToStr(date, "yyyy-MM-dd");
            return strToDate(day + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        } else {
            return null;
        }
    }

    public static Date getDateEnd(Date date) {
        if (date != null) {
            String day = dateToStr(date, "yyyy-MM-dd");
            return strToDate(day + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        } else {
            return null;
        }
    }

    public static int getWeekDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(7);
    }

    public static String getMondayDate(Date date) {
        Calendar cal = Calendar.getInstance();
        if (cal.get(7) == 1) {
            cal.add(3, -1);
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(7, 2);
        return df.format(cal.getTime());
    }

    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = strToDate(sdf.format(smdate), "yyyy-MM-dd");
        bdate = strToDate(sdf.format(bdate), "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / 86400000L;
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static int betweenMinute(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        smdate = strToDate(sdf.format(smdate), "yyyy-MM-dd HH:mm");
        bdate = strToDate(sdf.format(bdate), "yyyy-MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / 60000L;
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static int getUnixTimestamp(Date date) {
        return (int)(date.getTime() / 1000L);
    }

    public static Date unixTimestampToDate(int i) {
        return new Date((long)i * 1000L);
    }

    public static List<Date> findDates(Date dBegin, Date dEnd) {
        List<Date> lDate = new ArrayList();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);

        while(dEnd.after(calBegin.getTime())) {
            calBegin.add(5, 1);
            lDate.add(calBegin.getTime());
        }

        return lDate;
    }

    public static Date addDate(Date date, int days) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(5, days);
            date = calendar.getTime();
            return date;
        } else {
            return null;
        }
    }

    public static String addDateStr(String dateStr, int days) {
        Date date = strToDate(dateStr, "yyyy-MM-dd");
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(5, days);
            date = calendar.getTime();
            return dateToStr(date, "yyyy-MM-dd");
        } else {
            return null;
        }
    }

    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        int dayOfWeek = cd.get(7);
        return dayOfWeek == 1 ? -6 : 2 - dayOfWeek;
    }

    public static Date getPreviousMonday() {
        --weeks;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        return monday;
    }

    public static Date getPreviousSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, mondayPlus + 7 * weeks + 6);
        Date monday = currentDate.getTime();
        return monday;
    }

    public static Date getCurrentMonday() {
        weeks = 0;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, mondayPlus);
        Date monday = currentDate.getTime();
        return monday;
    }

    public static Date getCurrentSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, mondayPlus + 7 * weeks + 6);
        Date monday = currentDate.getTime();
        return monday;
    }

    public static Date getNextMonday() {
        ++weeks;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        return monday;
    }

    public static Date getNextSunday() {
        ++weeks;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, mondayPlus + 7 * weeks + 6);
        Date monday = currentDate.getTime();
        return monday;
    }

    public static boolean isValid(String checkStr, String format) {
        if (StringUtil.isBlank(checkStr)) {
            return false;
        } else {
            if (StringUtil.isBlank(format)) {
                format = "yyyy-MM-dd";
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            if (checkStr.trim().length() != dateFormat.toPattern().length()) {
                return false;
            } else {
                dateFormat.setLenient(false);

                try {
                    dateFormat.parse(checkStr.trim());
                    return true;
                } catch (ParseException var4) {
                    return false;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getHour(new Date()));
    }
}

