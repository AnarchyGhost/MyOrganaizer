package com.anarchyghost.myapplication;

public class Birthday {
        private final int id;
        private final String name;
        private final long date;
        public Birthday(int id,String name, long date) {
            this.id=id;
            this.name = name;
            this.date=date;
        }
        public int getId(){return id;}
        public String getName(){
            return name;
        }
        public long getDate(){return date;}


    }
