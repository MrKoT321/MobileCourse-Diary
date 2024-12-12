package com.example.dictionary

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecordDao {
    @Query("SELECT * FROM Record")
    suspend fun getAll(): List<Record>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg records: Record)

    @Delete
    suspend fun delete(record: Record)

    @Update
    suspend fun updateAll(vararg records: Record)

    @Query("SELECT * FROM Record WHERE uid = :uid")
    suspend fun getByUid(uid: String): Record
}