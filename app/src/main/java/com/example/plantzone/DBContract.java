// DBContract.java
package com.example.plantzone;

import android.provider.BaseColumns;

public final class DBContract {

    private DBContract() {
    }

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
    }

    public static class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = "posts";
        public static final String COLUMN_POST_ID = "post_id";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE_URI = "image_uri";

    }

    public static class FeedbackEntry implements BaseColumns {
        public static final String TABLE_NAME = "feedback";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_FEEDBACK = "feedback";
        public static final String COLUMN_USER_EMAIL = "user_email"; // Foreign key column referencing UserEntry.COLUMN_EMAIL


    }

    public static class CommentEntry implements BaseColumns {
        public static final String TABLE_NAME = "comments";
        public static final String COLUMN_POST_ID = "post_id";
        public static final String COLUMN_COMMENT = "comment";
    }
    public static class ImagesEntry implements BaseColumns {
        public static final String TABLE_IMAGES = "images";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_PREDICTION = "prediction";

    }
}