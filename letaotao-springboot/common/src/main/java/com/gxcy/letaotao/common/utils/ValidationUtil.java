package com.gxcy.letaotao.common.utils;

import java.math.BigDecimal;

public class ValidationUtil {

    public static void requireNonNullAndNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + "不能为空");
        }
    }

    // 泛型方法，适用于所有数值类型（包括Integer, Long, BigDecimal等）
    public static <T extends Number> void requireNonNullAndPositive(T value, String fieldName) {
        if (value == null || value.doubleValue() <= 0) {
            throw new IllegalArgumentException(fieldName + "必须大于零");
        }
    }

    // 特化版本，仅针对Long类型，提高代码可读性并避免不必要的类型转换
    public static void requireNonNullAndPositive(Long value, String fieldName) {
        if (value == null || value <= 0L) {
            throw new IllegalArgumentException(fieldName + "必须大于零");
        }
    }

    public static void requireNonNullAndPositive(Integer value, String fieldName) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(fieldName + "必须大于零");
        }
    }

    // 特殊针对BigDecimal的非空判断
    public static void requireNonNullAndGreaterThanZero(BigDecimal value, String fieldName) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(fieldName + "必须大于零");
        }
    }

    // 对于非String类型对象的非空判断
    public static <T> void requireNonNull(T obj, String fieldName) {
        if (obj == null) {
            throw new IllegalArgumentException(fieldName + "不能为空");
        }
    }
}

