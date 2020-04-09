package com.csu.xgum.greendaof.converter;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者：sucheng on 2020/4/1 0001 15:37
 */
public class StringDateConverter implements PropertyConverter<Date, String> {
    private final SimpleDateFormat simUtil = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date convertToEntityProperty(String databaseValue) {
        Date date = null;
        try {
            date = simUtil.parse(databaseValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            date = new Date(0);
        }
        return date;
    }

    @Override
    public String convertToDatabaseValue(Date entityProperty) {
        return simUtil.format(entityProperty);
    }
}
