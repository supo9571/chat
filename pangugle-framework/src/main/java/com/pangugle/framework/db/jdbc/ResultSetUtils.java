/*
 * Copyright (C) 2019  即时通讯网(www.pangugle.com) & Jack Pangugle.
 * The pangugle project. All rights reserved.
 * 
 * 【本产品为著作权产品，合法授权后请放心使用，禁止外传！】
 * 【本次授权给：<xxx网络科技有限公司>，授权编号：<授权编号-xxx>】
 * 
 * 本系列产品在国家版权局的著作权登记信息如下：
 * 1）国家版权局登记名（简称）和证书号：Project_name（软著登字第xxxxx号）
 * 著作权所有人：厦门盘古网络科技有限公司
 * 
 * 违法或违规使用投诉和举报方式：
 * 联系邮件：2624342267@qq.com
 * 联系微信：pangugle
 * 联系QQ：2624342267
 * 官方社区：http://www.pangugle.com
 */
package com.pangugle.framework.db.jdbc;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.springframework.stereotype.Component;

import com.pangugle.framework.reflect.JavaBeanField;
import com.pangugle.framework.reflect.MyFieldFactory;

/**
 * Resultset 自动转成model, model必须实现getTablePrefix静态方法
 * 原则是约定优于配置
 *
 * @author Administrator
 */
@Component
public class ResultSetUtils {
	
	public static String BOOLEAN = "boolean";

    public static <T> T convertJavaBean(Class<T> cls, ResultSet rs) {
        try {
            // 实例化对象
            //T obj = cls.newInstance();
            T obj = null;

            JavaBeanField beanField = MyFieldFactory.getField(cls);
            ResultSetMetaData meta = rs.getMetaData();
            int count = meta.getColumnCount();
            if (rs.next()) {
                obj = cls.newInstance();
                for (int i = 1; i <= count; i++) {
                    String column = meta.getColumnLabel(i);
//                    Object value = rs.getObject(column);
                    Field field = beanField.getField(column);
					if(field != null) {
						safeSetFieldValue(obj, field, rs, column);
//						String fieldType = field.getGenericType().toString();
//						 if(BOOLEAN.equalsIgnoreCase(fieldType))
//						 {
//	                    	 field.set(obj, rs.getBoolean(column));
//	                    	// System.out.println(column +  " = " + rs.getBoolean(column));
//						 }
//						 else
//						 {
//							 field.set(obj, value);
//						 }
					}
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static <T> T resultSetToJavaBean(Class<T> cls, ResultSet rs) {
        try {
            T obj = null;

            JavaBeanField beanField = MyFieldFactory.getField(cls);
            ResultSetMetaData meta = rs.getMetaData();
            int count = meta.getColumnCount();
            obj = cls.newInstance();
            for (int i = 1; i <= count; i++) {
                String column = meta.getColumnLabel(i);
                Object value = rs.getObject(column);
                Field field = beanField.getField(column);
                if (value == null){
                    Class<?> fieldType = field.getType();
                    if (fieldType != null){
                        if (fieldType == BigDecimal.class){
                            value = BigDecimal.ZERO;
                        }
                    }
                }


                if (field != null){
                	safeSetFieldValue(obj, field, rs, column);
                    //beanField.getField(column).set(obj, value);
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static <T> void safeSetFieldValue(T model, Field field, ResultSet rs, String column) throws Exception
    {
    	String fieldType = field.getGenericType().toString();
		 if(ResultSetUtils.BOOLEAN.equalsIgnoreCase(fieldType))
		 {
			 field.set(model, rs.getBoolean(column));
		 }
		 else
		 {
			 field.set(model, rs.getObject(column));
		 }
    }
}
