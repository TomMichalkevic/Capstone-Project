package com.tomasmichalkevic.seevilnius.data.db;

import android.arch.persistence.room.TypeConverter;

public class BooleanConverter {
    @TypeConverter
    public static Boolean toBoolean(int value) {
        return value == 1 ? true : false;
    }

    @TypeConverter
    public static int toInteger(Boolean value) {
        return value == true ? 1 : 0;
    }
}