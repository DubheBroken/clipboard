package com.rice.tool;

import android.util.Log;

public class NumberUtils {

    /**
     * 以千、万、亿为单位缩写数字
     *
     * @param num 要缩写的数字
     * @return 缩写后的结果
     */
    public static String 缩写数字(String num) {
        return 缩写数字(num, 1000.0);
    }

    /**
     * 以千、万、亿为单位缩写数字
     *
     * @param 待缩写数字 要缩写的数字
     * @param 阈值    ≥此值才会执行
     * @return 缩写后的结果
     */
    public static String 缩写数字(String 待缩写数字, double 阈值) {
        String abb = "";
        if (TextUtils.isEmpty(待缩写数字)) {
            return abb;
        }
        try {
            double dnum = Double.parseDouble(待缩写数字);
            if (dnum < 阈值) {
                abb = 待缩写数字;
                return abb;
            }
            if (dnum > Arith.mul(1000.0, 10000.0)) {
                abb = ((int) Arith.div(dnum, Arith.mul(1000.0, 10000.0), 2)) + "亿";
            } else if (dnum > 10000.0) {
                abb = ((int) Arith.div(dnum, 10000.0, 2)) + "万";
            } else if (dnum > 1000.0) {
                abb = (Arith.div(dnum, 1000.0, 2)) + "千";
            } else {
                abb = 待缩写数字;
            }
        } catch (NumberFormatException e) {
            Log.e("缩写数字", "入参" + 待缩写数字 + "不是合法的数字");
            abb = 待缩写数字;
            e.printStackTrace();
        }
        return abb;
    }

}
