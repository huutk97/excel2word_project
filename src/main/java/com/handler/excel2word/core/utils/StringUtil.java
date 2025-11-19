package com.handler.excel2word.core.utils;


import org.apache.commons.text.WordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

public class StringUtil {
    private static String string = "abcdefghijklmnopqrstuvwxyz";
    public static char[] randomMetaData = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public StringUtil() {
    }

    public static boolean isBlank(String str) {
        return str == null || "".equals(str) || "null".equalsIgnoreCase(str);
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String trim(String str) {
        return str == null ? "" : str.trim();
    }

    public static boolean isNum(String obj) {
        if (isBlank(obj)) {
            return false;
        } else {
            boolean flag = true;
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNumber = pattern.matcher(obj);
            if (!isNumber.matches()) {
                flag = false;
            }

            return flag;
        }
    }

    public static boolean isDouble(String obj) {
        if (isBlank(obj)) {
            return false;
        } else {
            boolean flag = true;
            Pattern pattern = Pattern.compile("^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$");
            Matcher isNumber = pattern.matcher(obj);
            if (!isNumber.matches()) {
                flag = false;
            }

            return flag;
        }
    }

    public static boolean isSex(String obj) {
        boolean flag = true;
        Pattern pattern = Pattern.compile("[男女]{1}");
        Matcher isRightSex = pattern.matcher(obj);
        if (!isRightSex.matches()) {
            flag = false;
        }

        return flag;
    }

    public static double convertPrecision(double value, int precision) {
        String strDoubleValue = String.valueOf(value);
        int len = strDoubleValue.indexOf(".");
        if (len != -1) {
            String integerValue = strDoubleValue.substring(0, len);
            String precisionValue = strDoubleValue.substring(len + 1);
            if (precisionValue.length() > precision) {
                precisionValue = precisionValue.substring(0, precision);
            }

            strDoubleValue = integerValue + "." + precisionValue;
        }

        return Double.parseDouble(strDoubleValue);
    }

    public static List<Byte> strToByteArray(String str, String reg) {
        String[] ss = str.split(reg);
        List<Byte> list = new ArrayList();
        String[] var4 = ss;
        int var5 = ss.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String string = var4[var6];
            if (!string.equals("")) {
                list.add(Byte.parseByte(string));
            }
        }

        return list;
    }

    public static List<Integer> strToIntArray(String str, String reg) {
        String[] ss = str.split(reg);
        List<Integer> list = new ArrayList();
        String[] var4 = ss;
        int var5 = ss.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String string = var4[var6];
            if (!string.equals("")) {
                list.add(Integer.parseInt(string));
            }
        }

        return list;
    }

    public static List<Long> strToLongArray(String str, String reg) {
        String[] ss = str.split(reg);
        List<Long> list = new ArrayList();
        String[] var4 = ss;
        int var5 = ss.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String string = var4[var6];
            if (!string.equals("")) {
                list.add(Long.parseLong(string));
            }
        }

        return list;
    }

    public static List<String> strToStringArray(String str, String reg) {
        String[] ss = str.split(reg);
        List<String> list = new ArrayList();
        String[] var4 = ss;
        int var5 = ss.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String string = var4[var6];
            if (!string.equals("")) {
                list.add(string);
            }
        }

        return list;
    }

    public static String listToStr(List<String> array, String reg) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < array.size(); ++i) {
            sb.append((String)array.get(i));
            if (i < array.size() - 1) {
                sb.append(reg);
            }
        }

        return sb.toString();
    }

    public static boolean isYYYYMMDD(String str) {
        if (trim(str).length() != 10) {
            return false;
        } else {
            String datePattern = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
            Pattern r = Pattern.compile(datePattern);
            Matcher m = r.matcher(str);
            return m.find();
        }
    }

    public static Integer JudgeIsMoblie(HttpServletRequest request) {
        Integer user_agent = 0;
        String[] mobileAgents = new String[]{"iphone", "android", "phone", "mobile", "wap", "netfront", "java", "opera mobi", "opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod", "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma", "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos", "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem", "wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos", "pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320", "240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac", "blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs", "kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi", "mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port", "prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem", "smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tsm-", "upg1", "upsi", "vk-v", "voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-", "Googlebot-Mobile"};
        if (request.getHeader("User-Agent") != null) {
            String[] var3 = mobileAgents;
            int var4 = mobileAgents.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String mobileAgent = var3[var5];
                if (request.getHeader("User-Agent").toLowerCase().indexOf(mobileAgent) >= 0) {
                    if (mobileAgent.equals("android")) {
                        user_agent = 2;
                    } else if (mobileAgent.equals("iphone")) {
                        user_agent = 3;
                    } else {
                        user_agent = 1;
                    }
                    break;
                }
            }
        }

        return user_agent;
    }

    public static String getRandomStr(int charLength, int numLength) {
        StringBuffer sb = new StringBuffer();
        int len = string.length();

        int i;
        for(i = 0; i < charLength; ++i) {
            sb.append(string.charAt((int)Math.round(Math.random() * (double)(len - 1))));
        }

        for(i = 0; i < numLength; ++i) {
            int anInt = (new Random()).nextInt(9);
            sb.append(anInt);
        }

        return sb.toString();
    }

    public static String filterByRegex(String content, String regex) {
        return content.replaceAll(regex, "");
    }

    public static String randomStr(int num) {
        Random random = new Random();
        String tNonceStr = "";

        for(int i = 0; i < num; ++i) {
            tNonceStr = tNonceStr + randomMetaData[random.nextInt(randomMetaData.length - 1)];
        }

        return tNonceStr;
    }

    public static String capitalizeFully(String value) {
        if (value == null) {
            return "";
        }
        return WordUtils.capitalizeFully(value);
    }

    public static void main(String[] args) {
        System.out.println(getRandomStr(1, 5));
    }
}
