package com.example.moviesapp.utilities;

import android.provider.SyncStateContract;

import androidx.room.TypeConverter;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampConverter {
    @TypeConverter
    public static Long fromTimestamp(DateTime value) {
        if (value != null) {
            return value.getMillis();
        } else {
            return null;
        }
    }
}
