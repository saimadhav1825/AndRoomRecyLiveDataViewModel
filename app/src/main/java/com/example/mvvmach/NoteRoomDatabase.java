package com.example.mvvmach;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class},version = 1)
public abstract class NoteRoomDatabase extends RoomDatabase {
    private static NoteRoomDatabase instance;
    public  abstract MyDao myDao();
    public static synchronized NoteRoomDatabase getInstance(Context context) {
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),NoteRoomDatabase.class,"database")
                    .fallbackToDestructiveMigration().
                            addCallback(roomCallBack).build();
            }
             return instance;
    }
    private static RoomDatabase.Callback roomCallBack=new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            new PopularAsyncTask(instance).execute();
            super.onCreate(db);
        }
    };
    private static class PopularAsyncTask extends AsyncTask<Void,Void,Void>{
        private MyDao myDao;
        private PopularAsyncTask(NoteRoomDatabase database){
            myDao=database.myDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            myDao.insert(new Note("Title1","bewjfbbfjeffj0",1));
            myDao.insert(new Note("Title12","bewjfbbfjeffj0",2));
            return null;
        }
    }
}
