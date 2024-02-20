package com.example.plantzone;

import android.provider.BaseColumns;

public final class DBContract {

    private DBContract() {}


    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
    }
    public static class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = "posts";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE_URI = "image_uri";
    }
}