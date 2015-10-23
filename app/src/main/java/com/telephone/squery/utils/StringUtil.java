package com.telephone.squery.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ${ZhangYan}[Squery] on 2015/10/23.
 */
public class StringUtil {

    /**
     * 匹配的字符串忽略大小写
     *
     * @param str 字符串
     * @param regex 正则表达式
     * @return
     */
    public static boolean regexMatcherCaseInsensitive(String str, String regex) {
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    /**
     * 根据给定的小时和分钟数 返回 00:00 格式的时间字符串
     *
     * @param hourOfDay
     * @param minute
     * @return
     */
    public static String getTimeString(int hourOfDay, int minute) {
        String h = hourOfDay + "";
        String m = minute + "";
        if (h.length() == 1) {
            h = "0" + h;
        }
        if (m.length() == 1) {
            m = "0" + m;
        }
        return h + ":" + m;
    }

    /**
     * 修改过长的文件名
     *
     * @param filePath
     * @return
     */
    public static String modifyLongFileName(String filePath) {
        if (filePath != null && filePath.length() > 220) {
            filePath = filePath.substring(0, 100)
                    + filePath.substring(filePath.length() - 130, filePath.length());
        }
        return filePath;
    }

    /**
     * 格式化数字,控制显示长度
     *
     * @param count
     * @return
     */
    public static String formatCount(int count) {
        if (count / (10000 * 100) > 0) {
            return "100W+";
        } else if (count / 10000 > 0) {
            return count / 10000 + "W+";
        } else {
            return count + "";
        }
    }

    /**
     * 将byte数组转换为字符串,
     *
     * @param array       byte数组
     * @param length      截取的长度,从0开始,因中文字符等原因,实际截取的长度可能小于length
     * @param charsetName 生成的字符串的字符编码
     * @return
     */
    public static String getStringFromByteArray(byte[] array, int length, String charsetName) {
        String str = "";
        int sig = 1;
        if (length <= array.length) {
            for (int i = 0; i < length; i++) {
                sig = array[i] * sig >= 0 ? 1 : -1;
            }
            if (sig < 0) {
                length -= 1;
            }
            try {
                str = new String(array, 0, length, charsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    /**
     * 判断是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNotNullOrEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    /**
     * 昵称判断
     *
     * @param str
     * @return
     */
    public static boolean isNickname(final String str) {
        try {
            byte[] bytes = str.getBytes("gbk");
            if (bytes.length > 20) {
                return false;
            }
        } catch (UnsupportedEncodingException e) {
        }
        // 把1个汉字看成字符编码 (不小于1个  (汉字和包括下划线的单词字符))
        final String regex = "[\\u4e00-\\u9fa5\\w]{1,}";
        return match(regex, str);
    }

    /**
     * 邮箱判断
     *
     * @param str
     * @return
     */
    public static boolean isEmail(final String str) {
        final String regex = "^[a-zA-Z0-9]{1,}[a-zA-Z0-9\\_\\.\\-]{0,}" +
                "@(([a-zA-Z0-9]){1,}\\.){1,3}[a-zA-Z0-9]{0,}[a-zA-Z]{1,}$";
        return match(regex, str);
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        final String regex = "^[1][3,4,5,7,8][0-9]{9}$";
        return match(regex, str);
    }

    /**
     * 校验银行卡卡号 这里只校验16位的银行卡
     *
     * @param cardId
     * @return
     */
    public static boolean isBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N' || cardId.length() != 16) {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * @param str
     * @return
     */
    public static boolean isRegUserName(final String str) {
        final String regex = "[0-9a-zA-Z\\_]{5,20}";
        return match(regex, str);
    }

    public static boolean isLoginUserName(final String str) {
        final String regex = "[0-9a-zA-Z\\_]{3,20}";
        return match(regex, str);
    }

    public static boolean isPassword(final String str) {
        final String regex = "[\\S]{6,16}";
        return match(regex, str);
    }


    /**
     * 解析 url 把每部分放到 HashSet中
     *
     * @param s
     * @return
     */
    public static Set<String> handleUrl(String s) {
        Set<String> result = new HashSet<String>();
        if (isNullOrEmpty(s)) {
            return result;
        }

        Matcher matcher = URL_PATTERN.matcher(s);
        boolean find = matcher.find();
        StringBuffer sb = new StringBuffer();
        while (find) {
            result.add(matcher.group());
            find = matcher.find();
        }
        matcher.appendTail(sb);

        return result;
    }


    /**
     * 处理空字符串  去掉前后空格 如果是null的话直接返回
     *
     * @param str
     * @return String
     */
    public static String handleEmpty(String str) {
        return handleEmpty(str, "");
    }

    /**
     * 处理手机号码  把手机号的中间4位变成
     *
     * @param phonenum
     * @return
     */
    public static String handlePhoneNumber(String phonenum) {
        String ret = null;
        if (isPhone(phonenum)) {

            ret = phonenum.substring(0, phonenum.length() - (phonenum.substring(3)).length())
                    + "****" + phonenum.substring(7);
        }
        return ret;

    }


    /////////////////////////////////////////////////////////////////////////


    public static final Pattern URL_PATTERN =
            Pattern.compile("((?:https|http)://)" + "(?:[0-9a-z_!~*'()-]+\\.)*" // 域名-(www.)
                    + "(?:[0-9a-z][0-9a-z!~*#&'.^:@+$%-]{0,61})?[0-9a-z]\\." //二级域名(,可以含有符号)
                    + "[a-z]{0,6}" // .com
                    + "(?::[0-9]{1,4})?" // 端口
                    + "(?:/[0-9A-Za-z_!~*'().?:@&=+,$%#-]*)*" // 除了域名外中间参数允许的字符
                    + "[0-9A-Za-z-/?~#%*&()$+=^]", Pattern.CASE_INSENSITIVE); // 指定可以作为结尾的字符


    /**
     * 用于判断字符串和正则表达式是否匹配
     *
     * @param regex
     * @param str
     * @return
     */
    private static boolean match(final String regex, final String str) {
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isNullOrEmpty(String str) {
        boolean flag = true;
        if (str != null && !"".equals(str.trim()) && !"null".equalsIgnoreCase(str.trim())) {
            flag = false;
        }
        return flag;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    public static String handleEmpty(String str, String defaultValue) {
        if (str == null || str.equalsIgnoreCase("null")
                || str.trim().equals("")) {
            str = defaultValue;
        }
        return str.trim();
    }

}