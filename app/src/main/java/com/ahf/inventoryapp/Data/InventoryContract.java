package com.ahf.inventoryapp.Data;

import android.provider.BaseColumns;

/**
 * Created by Ahmed Hassan on 26/08/2018.
 */

public class InventoryContract {
    public InventoryContract() {
    }

    public static final class InventoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "inventory";
        public static final String _ID = BaseColumns._ID;
        public static final String ID = "id";
        public static final String COLUMN_PRODUCT_NAME = "product_name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_SUP_NAME = "name";
        public static final String COLUMN_SUP_EMAIL = "email";
        public static final String COLUMN_SUP_PHONE = "phone";
    }
}
