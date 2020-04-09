package com.csu.xgum.greendaof.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.csu.xgum.entity.Student;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * 作者：sucheng on 2020/4/1 0001 16:28
 */
public class ListConverter implements PropertyConverter<List<Student>, String> {
    @Override
    public List<Student> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        // 先得获得这个，然后再typeToken.getType()，否则会异常
//        meizis= gson.fromJson(jsonData, new TypeToken<List<Meizi>>() {}.getType());
        TypeToken<List<Student>> typeToken = new TypeToken<List<Student>>() {
        };
        return new Gson().fromJson(databaseValue, typeToken.getType());
    }

    @Override
    public String convertToDatabaseValue(List<Student> entityProperty) {
        if (entityProperty == null || entityProperty.size() == 0) {
            return null;
        } else {
            String sb = new Gson().toJson(entityProperty);
            return sb;
        }
    }
}
