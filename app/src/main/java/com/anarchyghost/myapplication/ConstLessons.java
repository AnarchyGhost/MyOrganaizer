package com.anarchyghost.myapplication;

public class ConstLessons {
    private final int id;
    private final String name;
    private final int date;
    private final long start;
    private final long end;

    public ConstLessons(int id, String name, int date, long start, long end) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    public int getDate() {
        return date;
    }

    public long getEnd() {
        return end;
    }

    public int getId() {
        return id;
    }

    public long getStart() {
        return start;
    }

    public String getName() {
        return name;
    }
}
