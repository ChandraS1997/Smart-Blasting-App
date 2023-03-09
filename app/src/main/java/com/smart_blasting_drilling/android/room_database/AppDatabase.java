package com.smart_blasting_drilling.android.room_database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.smart_blasting_drilling.android.room_database.dao_interfaces.AllMineInfoSurfaceInitiatorDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.AllProjectBladesModelDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.BenchTableDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.BlastCodeDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.BlastPerformanceDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.DrillAccessoriesInfoAllDataDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ExplosiveDataDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.FileTypeTableDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.InitiatingDataDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.InitiatingDeviceDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.MineTableDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.PitTableDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.Project2DBladesDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.Project3DBladesDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ResponseDrillMaterialDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.UpdatedProjectDataDao;
import com.smart_blasting_drilling.android.room_database.entities.AllProjectBladesModelEntity;
import com.smart_blasting_drilling.android.room_database.entities.BlastCodeEntity;
import com.smart_blasting_drilling.android.room_database.entities.BlastPerformanceEntity;
import com.smart_blasting_drilling.android.room_database.entities.InitiatingDeviceDataEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseDrillMaterialEntity;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ResponseDrillMethodDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.RockDataDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.TldDataDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.TypeTableDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.UpdateProjectBladesDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ZoneTableDao;
import com.smart_blasting_drilling.android.room_database.entities.AllMineInfoSurfaceInitiatorEntity;
import com.smart_blasting_drilling.android.room_database.entities.ExplosiveDataEntity;
import com.smart_blasting_drilling.android.room_database.entities.InitiatingDataEntity;
import com.smart_blasting_drilling.android.room_database.entities.Project2DBladesEntity;
import com.smart_blasting_drilling.android.room_database.entities.Project3DBladesEntity;
import com.smart_blasting_drilling.android.room_database.entities.ProjectHoleDetailRowColEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseBenchTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseDrillAccessoriesInfoAllDataEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseDrillMethodEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseFileDetailsTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseMineTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponsePitTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseTypeTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseZoneTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.RockDataEntity;
import com.smart_blasting_drilling.android.room_database.entities.TldDataEntity;
import com.smart_blasting_drilling.android.room_database.entities.UpdateProjectBladesEntity;
import com.smart_blasting_drilling.android.room_database.entities.UpdatedProjectDetailEntity;

@Database(version = 1, entities = {Project2DBladesEntity.class, Project3DBladesEntity.class, ProjectHoleDetailRowColEntity.class, UpdateProjectBladesEntity.class
            , ResponseBenchTableEntity.class, ResponseFileDetailsTableEntity.class, ResponseMineTableEntity.class, ResponsePitTableEntity.class
            , ResponseTypeTableEntity.class, ResponseZoneTableEntity.class, ExplosiveDataEntity.class
            , TldDataEntity.class, InitiatingDataEntity.class, RockDataEntity.class
            , AllMineInfoSurfaceInitiatorEntity.class, ResponseDrillAccessoriesInfoAllDataEntity.class
            , ResponseDrillMethodEntity.class, ResponseDrillMaterialEntity.class, UpdatedProjectDetailEntity.class
            , InitiatingDeviceDataEntity.class, AllProjectBladesModelEntity.class, BlastPerformanceEntity.class
            , BlastCodeEntity.class}, exportSchema = true,
    autoMigrations = {})
public abstract class AppDatabase extends RoomDatabase {

    public abstract Project2DBladesDao project2DBladesDao();
    public abstract Project3DBladesDao project3DBladesDao();
    public abstract ProjectHoleDetailRowColDao projectHoleDetailRowColDao();
    public abstract UpdateProjectBladesDao updateProjectBladesDao();
    public abstract BenchTableDao benchTableDao();
    public abstract FileTypeTableDao fileDetailsTableDao();
    public abstract MineTableDao mineTableDao();
    public abstract PitTableDao pitTableDao();
    public abstract TypeTableDao typeTableDao();
    public abstract ZoneTableDao zoneTableDao();
    public abstract ExplosiveDataDao explosiveDataDao();
    public abstract TldDataDao tldDataEntity();
    public abstract RockDataDao rockDataDao();
    public abstract InitiatingDataDao initiatingDataDao();
    public abstract AllMineInfoSurfaceInitiatorDao allMineInfoSurfaceInitiatorDao();
    public abstract DrillAccessoriesInfoAllDataDao drillAccessoriesInfoAllDataDao();
    public abstract ResponseDrillMethodDao drillMethodDao();
    public abstract ResponseDrillMaterialDao drillMaterialDao();
    public abstract UpdatedProjectDataDao updatedProjectDataDao();
    public abstract InitiatingDeviceDao initiatingDeviceDao();
    public abstract AllProjectBladesModelDao allProjectBladesModelDao();
    public abstract BlastPerformanceDao blastPerformanceDao();
    public abstract BlastCodeDao blastCodeDao();

    public static class DatabaseMigrations {
        public Migration MIGRATION_2_3 = new Migration(2, 3) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("CREATE TABLE  IF NOT EXISTS ProjectHoleDetailRowColEntity (`id` LONG,`designId` TEXT, `project_hole` TEXT NOT NULL, PRIMARY KEY(`id`))");
            }
        };

        public Migration MIGRATION_3_4 = new Migration(3, 4) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("CREATE TABLE  IF NOT EXISTS UpdateProjectBladesEntity (id LONG PRIMARY KEY, PitName TEXT, ZoneName TEXT, " +
                        "DesignId TEXT, DesignCode TEXT, DesignName TEXT, DesignDateTime TEXT, MineName TEXT, BenchName TEXT, RockName TEXT, PcntUnderSize TEXT, " +
                        "BlastPlan TEXT, AirVibration TEXT, BI TEXT, PcntOverSize TEXT, UniformExp TEXT, MatSize TEXT, GroundVibration TEXT, " +
                        "BoosterCharge TEXT, PcntOptSize TEXT, BackThrow TEXT, BottomCharge TEXT, ColCharge TEXT, FrontThrow TEXT, ChrctSize TEXT)");
            }
        };
    }

}
