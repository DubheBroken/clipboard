package com.rice.tool;

import java.lang.reflect.Field;

/**
 * 作者：DubheBroken
 * 时间：2018/12/7 16:38:20
 * 邮箱：z1574507001@gmail.com
 * 说明：
 */
public class ResHelper {

    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
