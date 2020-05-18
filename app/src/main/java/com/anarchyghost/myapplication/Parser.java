package com.anarchyghost.myapplication;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Parser {
public String getDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
        }

        public String getDateMonth() {

                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "dd-MM", Locale.getDefault());
                Date date = new Date();
                return dateFormat.format(date);
        }

        public long parseToMillMonth(String d) throws ParseException {
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "dd-MM", Locale.getDefault());
                Date moment = dateFormat.parse(d);
                return moment.getTime();
        }
        public String parseToMonth(long d){
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "dd-MM", Locale.getDefault());
                return dateFormat.format(d).toString();
        }
        public String getCurDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
        }


public String getDateTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
        "HH:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
        }

public long parseToMillTime(String d) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
        "HH:mm", Locale.getDefault());
        Date moment=dateFormat.parse(d);
        return moment.getTime();
        }

public long parseToMillDate(String d) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd", Locale.getDefault());
        Date moment=dateFormat.parse(d);
        return moment.getTime();
        }

public String parseToTime(long d){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
        "HH:mm", Locale.getDefault());
        return dateFormat.format(d).toString();
        }

public String parseToDate(long d){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(d).toString();
        }
public String parseToDateTime(long d){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm", Locale.getDefault());
        return dateFormat.format(d).toString();
        }
    public long parseToMillDateTime(String d) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm", Locale.getDefault());
        Date moment = dateFormat.parse(d);
        return moment.getTime();
    }
    }
