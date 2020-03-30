package com.rice.tool;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 作者：张雪涛
 * 时间：2019/3/7 0007:17:43
 * 邮箱：1574507001@qq.com
 * 说明：时间转换工具
 */
public class TimeUtils {

    /**
     * 秒转毫秒
     *
     * @param str_s
     * @return
     */
    public static String s_to_m_s(String str_s) {
        double db_s = Double.parseDouble(str_s);
        int s = (int) db_s % 60;
        int m = (int) db_s / 60;
        if (m > 0) {
            return "" + m + "′" + s + "″";
        } else {
            return "" + s + "″";
        }
    }

    /**
     * 根据日期取得周几
     *
     * @param date
     * @return
     */
    public static String getWeek(Date date) {
        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    /**
     * @param dateSta 起始时间
     * @param dateEnd 结束时间
     * @param date    要判断的时间
     * @return 时间是否在起止时间内
     */
    public static boolean isInside(Date dateSta, Date dateEnd, Date date) {
        if (date.getHours() > dateSta.getHours() && date.getHours() < dateEnd.getHours()) {
            //小时在范围内，时间合法
            return true;
        } else if (date.getHours() == dateSta.getHours()) {
            //小时=开始时间
            //分钟＞开始时间，时间合法
            return date.getMinutes() >= dateSta.getMinutes();
        } else if (date.getHours() == dateEnd.getHours()) {
            //小时=结束时间
            //分钟＜结束时间，时间合法
            return date.getMinutes() <= dateEnd.getMinutes();
        } else {
            //小时超出范围，时间合法
            return false;
        }
    }

    /**
     * 将10 or 13 位时间戳转为时间字符串
     * convert the number 1407449951 1407499055617 to date/time format timestamp
     */
    public static String timestamp2Time(String str_num) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (str_num.length() == 13) {
            String date = sdf.format(new Date(Long.parseLong(str_num)));
//            LogUtil.d(Constant.TAG + "将13位时间戳:" + str_num + "转化为字符串:", date);
            return date;
        } else {
            String date = sdf.format(new Date(Integer.parseInt(str_num) * 1000L));
//            LogUtil.d(Constant.TAG + "将10位时间戳:" + str_num + "转化为字符串:", date);
            return date;
        }
    }

    /**
     * 将10 or 13 位时间戳转为时间字符串
     * convert the number 1407449951 1407499055617 to date/time format timestamp
     */
    public static String timestamp2Date(String str_num, SimpleDateFormat sdf) {
        if (str_num.length() == 13) {
            String date = sdf.format(new Date(Long.parseLong(str_num)));
//            LogUtil.d(Constant.TAG + "将13位时间戳:" + str_num + "转化为字符串:", date);
            return date;
        } else {
            String date = sdf.format(new Date(Integer.parseInt(str_num) * 1000L));
//            LogUtil.d(Constant.TAG + "将10位时间戳:" + str_num + "转化为字符串:", date);
            return date;
        }
    }

    /**
     * 获得年份差距
     *
     * @param strDate
     * @throws ParseException
     */
    public static String getYearDistance(SimpleDateFormat sdf, String strDate) throws ParseException {
        Date date = sdf.parse(strDate);

        Calendar c1 = Calendar.getInstance();   //当前日期
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);   //设置为另一个时间

        int year = c1.get(Calendar.YEAR);
        int oldYear = c2.get(Calendar.YEAR);

        return "" + (year - oldYear);
    }

    /**
     * 获取当前月最后一天
     *
     * @return
     */
    public static int getToMonthLastDay() {
        SimpleDateFormat format = new SimpleDateFormat("dd");
//        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, 1);
        ca.set(Calendar.DAY_OF_MONTH, 0);
        String last = format.format(ca.getTime());
        return Integer.parseInt(last);
    }

    /**
     * 获得月份差距
     *
     * @param strDate
     * @throws ParseException
     */
    public static String getMonthDistance(SimpleDateFormat sdf, String strDate) throws ParseException {
        Date date = sdf.parse(strDate);

        Calendar c1 = Calendar.getInstance();   //当前日期
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);   //设置为另一个时间

        int month = c1.get(Calendar.MONTH);
        int oldMonth = c2.get(Calendar.MONTH);

        return "" + (month - oldMonth);
    }

    /**
     * 获得天数差距
     *
     * @param strDate
     * @throws ParseException
     */
    public static String getDayDistance(SimpleDateFormat sdf, String strDate) throws ParseException {
        Date date = sdf.parse(strDate);

        Calendar c1 = Calendar.getInstance();   //当前日期
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);   //设置为另一个时间

        int day = c1.get(Calendar.DAY_OF_MONTH);
        int oldDay = c2.get(Calendar.DAY_OF_MONTH);

        return "" + (day - oldDay);
    }

    /**
     * 获得小时差距
     *
     * @param strDate
     * @throws ParseException
     */
    public static String getHourDistance(SimpleDateFormat sdf, String strDate) throws ParseException {
        Date date = sdf.parse(strDate);

        Calendar c1 = Calendar.getInstance();   //当前日期
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);   //设置为另一个时间

        int hour = c1.get(Calendar.HOUR_OF_DAY);
        int oldHour = c2.get(Calendar.HOUR_OF_DAY);

        return "" + (hour - oldHour);
    }

    /**
     * 获得分钟差距
     *
     * @param strDate
     * @throws ParseException
     */
    public static String getMinDistance(SimpleDateFormat sdf, String strDate) throws ParseException {
        Date date = sdf.parse(strDate);

        Calendar c1 = Calendar.getInstance();   //当前日期
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);   //设置为另一个时间

        int min = c1.get(Calendar.MINUTE);
        int oldMin = c2.get(Calendar.MINUTE);

        return "" + (min - oldMin);
    }

    /**
     * 获得秒钟差距
     *
     * @param strDate
     * @throws ParseException
     */
    public static String getSecondDistance(SimpleDateFormat sdf, String strDate) throws ParseException {
        Date date = sdf.parse(strDate);

        Calendar c1 = Calendar.getInstance();   //当前日期
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);   //设置为另一个时间

        int second = c1.get(Calendar.SECOND);
        int oldSecond = c2.get(Calendar.SECOND);

        return "" + Math.abs(second - oldSecond);
    }

    /**
     * 获得秒
     *
     * @param strDate
     * @throws ParseException
     */
    public static String getSecond(SimpleDateFormat sdf, String strDate) throws ParseException {
        Date date = sdf.parse(strDate);
        SimpleDateFormat df = new SimpleDateFormat("ss");
        return df.format(date);
    }

    /**
     * 获得分
     *
     * @param strDate
     * @throws ParseException
     */
    public static String getMin(SimpleDateFormat sdf, String strDate) throws ParseException {
        Date date = sdf.parse(strDate);
        SimpleDateFormat df = new SimpleDateFormat("mm");
        return df.format(date);
    }

    /**
     * 获得小时
     *
     * @param strDate
     * @throws ParseException
     */
    public static String getHour(SimpleDateFormat sdf, String strDate) throws ParseException {
        Date date = sdf.parse(strDate);
        SimpleDateFormat df = new SimpleDateFormat("HH");
        return df.format(date);
    }

    /**
     * 是否是过去的时间
     *
     * @param sdf     时间格式
     * @param strDate 要计算距离的时间
     * @return
     */
    public static boolean isPast(SimpleDateFormat sdf, String strDate) throws ParseException {
        int yearDis = Integer.parseInt(getYearDistance(sdf, strDate));
        int monthDis = Integer.parseInt(getMonthDistance(sdf, strDate));
        int dayDis = Integer.parseInt(getDayDistance(sdf, strDate));
        int hourDis = Integer.parseInt(getHourDistance(sdf, strDate));
        int minDis = Integer.parseInt(getMinDistance(sdf, strDate));
        int secDis = Integer.parseInt(getSecondDistance(sdf, strDate));
        return !(yearDis <= 0 || monthDis <= 0 || dayDis <= 0 || hourDis <= 0 || minDis <= 0 || secDis <= 0);
    }

    /**
     * 计算指定时间和当前时间的距离
     *
     * @param sdf     时间格式
     * @param strDate 要计算距离的时间
     * @param hasHM   是否要带时分
     * @return 时间差距
     * @throws ParseException
     */
    public static String getDistance(SimpleDateFormat sdf, String strDate, boolean hasHM) throws ParseException {
        int yearDis = Integer.parseInt(getYearDistance(sdf, strDate));
        int monthDis = Integer.parseInt(getMonthDistance(sdf, strDate));
        int dayDis = Integer.parseInt(getDayDistance(sdf, strDate));
        int hourDis = Integer.parseInt(getHourDistance(sdf, strDate));
        int minDis = Integer.parseInt(getMinDistance(sdf, strDate));
        int secDis = Integer.parseInt(getSecondDistance(sdf, strDate));
        String hour = getHour(sdf, strDate);
        String min = getMin(sdf, strDate);
        if (yearDis > 0) {
            return "" + yearDis + "年前";
        } else if (monthDis > 0) {
            return "" + monthDis + "月前";
        } else if (dayDis > 0) {
            if (dayDis == 1) {
                if (hasHM) {
                    return "昨天 " + hour + ":" + min;
                } else {
                    return "昨天";
                }
            } else {
                if (hasHM) {
                    return "" + dayDis + "天前 " + hour + ":" + min;
                } else {
                    return "" + dayDis + "天前";
                }
            }
        } else if (hourDis > 0) {
            return "" + hourDis + "小时前";
        } else if (minDis > 0) {
            return "" + minDis + "分钟前";
        } else {
            if (hasHM) {
                return "" + secDis + "秒前";
            } else {
                return "刚刚";
            }
        }
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static long getNowTimestamp() {
        return Calendar.getInstance().getTimeInMillis();
    }

    /**
     * 将时间转换为时间戳
     */
    @SuppressLint("SimpleDateFormat")
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 将时间转换为时间戳
     */
    @SuppressLint("SimpleDateFormat")
    public static String dateToStamp(String s, SimpleDateFormat sdf) throws ParseException {
        String res;
        Date date = sdf.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

}
