package com.tomasmichalkevic.seevilnius.data.db;

import android.arch.persistence.room.TypeConverter;

class BooleanConverter {
    @TypeConverter
    public static Boolean toBoolean(int value) {
        return value == 1;
    }

    @TypeConverter
    public static int toInteger(Boolean value) {
        return value ? 1 : 0;
    }
}