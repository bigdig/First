package com.net263.contact.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;
import java.util.Map;

/**
 * fastJson 工具类
 *
 * @author xt
 * @version 1.0
 * @date 2017-06-23
 */
@SuppressWarnings("all")
public class JsonUtils {

    //json配置
    private static final SerializeConfig config;


    static {
        config = new SerializeConfig();
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
    }


    private static final SerializerFeature[] features = {
            SerializerFeature.WriteMapNullValue, // 输出空置字段
            SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
    };


    /**
     * 对象转化为字符串
     *
     * @param t   对象
     * @param <T>
     * @return
     */
    public static <T> String toJSONString(T t) {
        return JsonUtils.toJSONString(t, false);
    }


    /**
     * 对象转化为字符串
     *
     * @param t          对象
     * @param isFeatures 是否增加特殊配置
     * @return
     */
    public static <T> String toJSONString(T t, Boolean isFeatures) {
        if (isFeatures) {
            return JSON.toJSONString(t, config, features);
        } else {
            return JSON.toJSONString(t, config);
        }

    }


    /**
     * 字符串转对象
     *
     * @param text 待转字符串
     * @return
     */
    public static Object toBean(String text) {
        return JSON.parse(text);
    }

    /**
     * 字符串转对象
     *
     * @param text  待转字符串
     * @param clazz 待转类
     * @param <T>
     * @return
     */
    public static <T> T toBean(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    /**
     * 字符串转换为数组
     *
     * @param text
     * @param <T>
     * @return
     */
    public static <T> Object[] toArray(String text) {
        return toArray(text, null);
    }

    /**
     * 字符串转换为数组
     *
     * @param text  待转字符串
     * @param clazz 待转类对象
     * @param <T>
     * @return
     */
    public static <T> Object[] toArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz).toArray();
    }

    /**
     * 字符串转换为List
     *
     * @param text  待转字符传
     * @param clazz 待转对象
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }


    /**
     * json字符串转化为map
     *
     * @param s
     * @return
     */
    public static Map toMap(String s) {
        Map m = JSONObject.parseObject(s);
        return m;
    }

    /**
     * 将map转化为string
     *
     * @param m
     * @return
     */
    public static String mapToString(Map m) {
        String s = JSONObject.toJSONString(m);
        return s;
    }

    /**
     * 判断字符串是否是JSON对象字符串
     *
     * @param str
     * @return boolean
     * @Date:2013-6-13
     * @author wangk
     * @Description:
     */
    public static boolean isJsonObjectString(String str) {
        return str != null && str.matches("^\\{.*\\}$");
    }

    /**
     * 判断字符串是否是JSON数组字符串
     *
     * @param str
     * @return boolean
     * @Date:2013-6-13
     * @author wangk
     * @Description:
     */
    public static boolean isJsonArrayString(String str) {
        return str != null && str.matches("^\\[.*\\]$");
    }
}
