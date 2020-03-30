package com.rice.tool;

import android.util.Log;
import android.util.Patterns;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本操作工具类
 */
public class TextUtils {

    /**
     * 判断字符串是否为空
     *
     * @param str 要判断的字符串
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() < 1;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str 要判断的字符串
     */
    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }

    /**
     * 手机号隐藏中间四位
     *
     * @param str 传入的手机号
     * @return 隐藏后的手机号
     */
    public static String hindPhoneNumber(String str) {
        if (str.isEmpty() || str.length() < 11) {
//            throw new IllegalArgumentException("手机号无效" + str);
            Log.e("", "手机号无效:   " + str);
            return str;
        }
        String phone;
        phone = str.substring(0, 3) + "****" + str.substring(7);
        return phone;
    }

    /**
     * 取末尾X位
     */
    public static String getEndNum(String str, int x) {
        if (str.isEmpty() || str.length() < x) {
//            throw new IllegalArgumentException("手机号无效" + str);
            return str;
        }
        String phone;
        phone = str.substring(str.length() - x);
        return phone;
    }


    public static String getImgUrl(String base, String str) {
        if (!str.startsWith("http")) {
            str = base + str;
        }
        str = str.replace("\\", "/");
        str = str.replace("///", "//");
        return str;
    }

    /**
     * 判断字符串是否为Email
     *
     * @param str 传入的Email
     */
    public static boolean isEmail(String str) {
        Pattern pattern = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 判断电话号码是否有效
     * 移动：134、135、136、137、138、139、147、150、151、152、157、158、159、182、183、187、188
     * 联通：130、131、132、145、155、156、185、186
     * 电信：133、153、180、181、189
     * 虚拟运营商：17x
     */
    public static boolean isMobileNO(String number) {
        if (number.startsWith("+86")) {
            number = number.substring(3);
        }

        if (number.startsWith("+") || number.startsWith("0")) {
            number = number.substring(1);
        }
        number = number.replace(" ", "").replace("-", "");
        Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(18[0-9])|(17[0-1,3,5-8]))\\d{8}$");
        Matcher m = p.matcher(number);

        return m.matches();
    }

    /**
     * 判断全部是否为汉字
     */
    public static boolean isChinese(String str) {
        String regexIsHanZi = "[\\u4e00-\\u9fa5]+";
        Pattern pattern = Pattern.compile(regexIsHanZi);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();//true为全部是汉字，否则是false
    }

    /**
     * 判断是否是json结构
     */
    public static boolean isJson(String value) {
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否是url结构
     */
    public static boolean isUrl(String value) {
        return Patterns.WEB_URL.matcher(value).matches();
    }

    /**
     * 拼接地址字符串
     */
    public static String getAddress(String province, String city, String area) {
        String result = "";
        if (province.equals(city)) {//直辖市
            result = province + "-" + area;
        } else {//其余省市区
            if (isEmpty(area)) {
                result = province + "-" + city;
            } else {
                result = province + "-" + city + "-" + area;
            }
        }
        return result;
    }

    /**
     * 拼接地址字符串
     */
    public static String getAddress(String province, String city, String area, String addr) {
        String result = "";
        if (province.equals(city)) {//直辖市
            result = province + "  " + area + "  " + addr;
        } else {//其余省市区
            if (isEmpty(area)) {
                result = province + "  " + city + "  " + addr;
            } else {
                result = province + "  " + city + "  " + area + "  " + addr;
            }
        }
        return result;
    }

    /**
     * 判断是否正整数
     */
    public static boolean isPositiveInt(String str) {
        Pattern pattern = Pattern.compile("^[1-9]\\d*$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否整数
     */
    public static boolean isInt(String str) {
        Pattern pattern = Pattern.compile("^[0-9]\\d*$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 根据URL取文件名
     *
     * @param url
     * @return 取得的文件名，如果URL无效会返回Null
     */
    public static String getFileName(String url) {
        try {
            return new File(url.trim()).getName();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}
