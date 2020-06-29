package com.example.mvvmach;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyDao {
    @Insert
     void insert(Note note);
    @Update
     void update(Note note);
    @Delete
    void delete(Note note);
    @Query("delete from sai")
    void deleteAllNotes();
    @Query("select * from sai order by priority desc")
    LiveData<List<Note>> getAllNotes();


}
