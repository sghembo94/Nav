package com.example.andrea.nav;

import android.provider.BaseColumns;

/**
 * Created by andrea on 21/03/17.
 */

public class TableData {
    public TableData(){

    }

    public static abstract class TableInfo implements BaseColumns{

        public static final String DATABASE_NAME = "base_db";
        public static final String TABLE_NAME = "table_name";
        public static final String PRODUCT_ID = "product_id";
        public static final String PRODUCT_NAME = "product_name";
        public static final String PRODUCT_PRICE = "product_price";
        public static final String PRODUCT_Qta = "product_qta";
        public static final String PRODUCT_IMG = "product_img";


    }
}
