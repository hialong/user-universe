package com.decade.usercenter.utils;

import org.apache.commons.lang3.StringUtils;

public class SqlUtils {

    /**
     * 校验排序字段是否合法,防止SQL注入
     *
     * @param sortField
     * @return
     */
    public static Boolean validateSortField(String sortField) {
        if (sortField == null || sortField.length() == 0) {
            return false;
        }
        return !StringUtils.containsAny(sortField, "=", "(", ")", " ");
    }
}
