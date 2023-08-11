package com.example.musicdownload;

import android.provider.BaseColumns;

public final class DbContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DbContract() {}


    /* Inner class that defines the table contents */
    public static class SongEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String ALBUM = "album";
        public static final String NAME = "name";
        public static final String URL = "url";
    }
}
