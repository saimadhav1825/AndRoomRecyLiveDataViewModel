package com.example.mvvmach;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private MyDao myDao;
    LiveData<List<Note>> allNotes;
    public NoteRepository(Application application){
        NoteRoomDatabase database=NoteRoomDatabase.getInstance(application);
        myDao=database.myDao();
        allNotes=myDao.getAllNotes();
    }
    public void insert(Note note){
        new NoteAsyncTask(myDao).execute(note);

    }
    public void update(Note note){
        new UpdateAsyncTask(myDao).execute(note);

    }
    public void delete(Note note){
        new DeleteAsyncTask(myDao).execute(note);

    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(myDao).execute();

    }
    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }
    private static class NoteAsyncTask extends AsyncTask<Note,Void,Void>{
        private MyDao myDao;
        private  NoteAsyncTask(MyDao myDao){
            this.myDao=myDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            myDao.insert(notes[0]);
            return null;
        }
    }
    private static class UpdateAsyncTask extends AsyncTask<Note,Void,Void>{
        private MyDao myDao;
        private  UpdateAsyncTask(MyDao myDao){
            this.myDao=myDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            myDao.update(notes[0]);
            return null;
        }
    }
    private static class DeleteAsyncTask extends AsyncTask<Note,Void,Void>{
        private MyDao myDao;
        private  DeleteAsyncTask(MyDao myDao){
            this.myDao=myDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            myDao.delete(notes[0]);
            return null;
        }
    }
    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void,Void,Void>{
        private MyDao myDao;
        private  DeleteAllNotesAsyncTask(MyDao myDao){
            this.myDao=myDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            myDao.deleteAllNotes();
            return null;
        }
    }
}
