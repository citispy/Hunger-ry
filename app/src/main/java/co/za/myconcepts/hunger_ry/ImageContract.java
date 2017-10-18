package co.za.myconcepts.hunger_ry;

import android.provider.BaseColumns;

public final class ImageContract {
    private ImageContract() {
    }

    public static class ImageEntry implements BaseColumns {
        public static final String TABLE_NAME = "image";
        public static final String COLUMN_NAME_IMAGE_URL = "image_url";
    }
}