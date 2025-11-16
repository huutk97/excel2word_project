package com.handler.excel2word.core.export;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.util.Assert;

public class ReflectUtils {
    private static final Map<Class<?>, List<Field>> declaredFieldsCache = new HashMap(256);

    public ReflectUtils() {
    }

    public static Field getFieldByName(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (Exception var3) {
            throw new RuntimeException("在类" + clazz + "中找不到名为" + fieldName + "的属性!");
        }
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        if (obj != null && fieldName != null) {
            try {
                return PropertyUtils.getProperty(obj, fieldName);
            } catch (Exception var3) {
                throw new RuntimeException("在类" + obj.getClass() + "中获取不到名为" + fieldName + "的属性值!");
            }
        } else {
            throw new IllegalArgumentException("传入参数不能为空!");
        }
    }

    public static <T> T getNewInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception var2) {
            throw new RuntimeException("Create Object error which class is " + clazz, var2);
        }
    }

    public static void setProperty(Object obj, String fieldName, Object value) {
        Assert.notNull(value, "Set value can not be null!");

        try {
            Field field = getFieldByName(obj.getClass(), fieldName);
            if (field.getType() == Date.class) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date dateValue = formatter.parse(value.toString());
                field.setAccessible(true);
                field.set(obj, dateValue);
            } else {
                BeanUtils.setProperty(obj, fieldName, value);
            }

        } catch (Exception var6) {
            throw new RuntimeException("Set value to Object error,which fieldName is " + fieldName + " value is " + value, var6);
        }
    }

    public static Map<String, Object> getBeanMap(Object obj, String... properties) {
        Assert.notEmpty(properties, "properties can not be null!");
        Map<String, Object> beanMap = new HashMap();
        String[] var3 = properties;
        int var4 = properties.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String propertyName = var3[var5];
            Assert.hasText(propertyName, "propertyName can not be null!");
            beanMap.put(propertyName, getFieldValue(obj, propertyName));
        }

        return beanMap;
    }

    public static Map<String, Object> introspect(Object obj) {
        try {
            Map<String, Object> result = new HashMap();
            BeanInfo info = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] var3 = info.getPropertyDescriptors();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                PropertyDescriptor pd = var3[var5];
                Method reader = pd.getReadMethod();
                if (reader != null) {
                    result.put(pd.getName(), reader.invoke(obj));
                }
            }

            return result;
        } catch (Exception var8) {
            throw new RuntimeException("introspect object error!", var8);
        }
    }

    public static <T> T convert(Object value, Class<T> clazz) {
        return (T) ConvertUtils.convert(value, clazz);
    }

    public static Long convertToLong(Object value) {
        return (Long)convert(value, Long.class);
    }

    public static Integer convertToInt(Object value) {
        return (Integer)convert(value, Integer.class);
    }

    public static Double convertToDouble(Object value) {
        return (Double)convert(value, Double.class);
    }

    public static String convertToString(Object value) {
        return (String)convert(value, String.class);
    }

    public static void changeTimestampToDate(Map<String, Object> resultMap, String name) {
        Long timstamp = (Long)resultMap.get(name);
        if (timstamp != null) {
            resultMap.put(name, new Date(timstamp));
        }

    }

    public static List<Field> getClassAndSuperClassFields(Class<?> clazz) {
        Assert.notNull(clazz);
        List<Field> result = (List)declaredFieldsCache.get(clazz);
        if (result == null) {
            if (clazz.getSuperclass() == Object.class) {
                result = Arrays.asList(clazz.getDeclaredFields());
                declaredFieldsCache.put(clazz, result);
            } else {
                result = new ArrayList();
                ((List)result).addAll(Arrays.asList(clazz.getDeclaredFields()));
                ((List)result).addAll(getClassAndSuperClassFields(clazz.getSuperclass()));
                declaredFieldsCache.put(clazz, result);
            }
        }

        return (List)result;
    }
}

