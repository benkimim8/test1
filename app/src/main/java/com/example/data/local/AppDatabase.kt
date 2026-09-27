package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ChatMessageEntity::class,
        CounselingSessionEntity::class,
        PersonalDiagnosisEntity::class,
        DailyCheckinEntity::class,
        RoadmapStepEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun counselingSessionDao(): CounselingSessionDao
    abstract fun personalDiagnosisDao(): PersonalDiagnosisDao
    abstract fun dailyCheckinDao(): DailyCheckinDao
    abstract fun roadmapStepDao(): RoadmapStepDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "respring_counseling_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
