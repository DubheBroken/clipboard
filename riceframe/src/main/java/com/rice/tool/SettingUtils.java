package com.rice.tool;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static android.content.Context.MODE_PRIVATE;

/**
 * APP设置管理类
 */
public class SettingUtils {

    private static Application mApplication;

    /**
     * 初始化传入Application对象
     */
    public static void init(Application mApplication) {
        SettingUtils.mApplication = mApplication;
    }

    /**
     * 取Editor对象
     *
     * @param name 存储文件名
     */
    public static SharedPreferences.Editor getEditor(String name) {
        if (mApplication == null) {
            throw new IllegalArgumentException("请先在Application中初始化");
        }
        SharedPreferences pref = mApplication.getSharedPreferences(name, MODE_PRIVATE);
        return pref.edit();
    }

    /**
     * 取SharedPreferences对象
     *
     * @param name 存储文件名
     */
    public static SharedPreferences getPrefer(String name) {
        if (mApplication == null) {
            throw new IllegalArgumentException("请先在Application中初始化");
        }
        return mApplication.getSharedPreferences(name, MODE_PRIVATE);
    }

    /**
     * 存字符串数据
     *
     * @param name     存储文件名
     * @param valueKey 键
     * @param value    值
     */
    public static void setStringPrefer(String name, String valueKey, String value) {
        if (mApplication == null) {
            throw new IllegalArgumentException("请先在Application中初始化");
        }
        SharedPreferences pref = mApplication.getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(valueKey, value);
        editor.apply();
    }

    /**
     * 取字符串数据
     *
     * @param name 存储文件名
     * @param key  键
     */
    public static String getStringPrefer(String name, String key) {
        if (mApplication == null) {
            throw new IllegalArgumentException("请先在Application中初始化");
        }
        SharedPreferences pref = mApplication.getSharedPreferences(name, MODE_PRIVATE);
        return pref.getString(key, "");
    }

    /**
     * 保存对象数据
     *
     * @param name  存储文件名
     * @param key   键
     * @param model Model对象
     * @param <T>   Model类
     */
    public static <T extends Serializable> void saveModel(String name, String key, T model) {
        SharedPreferences.Editor editor = getEditor(name);
        if (model instanceof Serializable) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(model);//把对象写到流里
                String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
                editor.putString(key, temp);
                editor.apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                throw new IllegalArgumentException("Model必须implements Serializable");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取对象数据
     *
     * @param name 存储文件名
     * @param key  键
     * @param <T>  Model类型
     * @return 存储的数据转化为的对象
     */
    private static <T> T getmodel(String name, String key) {
        String temp = getStringPrefer(name, key);
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        T model = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            model = (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

}
