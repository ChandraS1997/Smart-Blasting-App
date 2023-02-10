package com.smart_blasting_drilling.android.room_database;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseMigrations {

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE  IF NOT EXISTS `ProjectHoleDetailRowColEntity` (`id` LONG,`designId` TEXT, `project_hole` TEXT NOT NULL, PRIMARY KEY(`id`))");
        }
    };

}
