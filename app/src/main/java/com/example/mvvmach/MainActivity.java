package com.example.mvvmach;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteViewModel noteViewModel;
    private static final int REQUEST=1;
    private static final int EDIT_REQUEST=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton button=findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,NoteAddActivity.class);
                startActivityForResult(intent,REQUEST);
            }
        });
        RecyclerView recyclerView=findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final NoteAdapter adapter=new NoteAdapter();
        recyclerView.setAdapter(adapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));

            }
        });
        adapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent=new Intent(MainActivity.this,NoteAddActivity.class);
                intent.putExtra(NoteAddActivity.ID,note.getId());
                intent.putExtra(NoteAddActivity.TITLE,note.getTitle());
                intent.putExtra(NoteAddActivity.DESCRIPTION,note.getDescription());
                intent.putExtra(NoteAddActivity.PRIORITY,note.getPriority());
                startActivityForResult(intent,EDIT_REQUEST);
            }
        });
        noteViewModel=ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
                Toast.makeText(MainActivity.this,"on changed",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST&&resultCode==RESULT_OK){
            String title=data.getStringExtra(NoteAddActivity.TITLE);
            String description=data.getStringExtra(NoteAddActivity.DESCRIPTION);
           int priority=data.getIntExtra(NoteAddActivity.PRIORITY,1);
            Note note=new Note(title,description,priority);
            noteViewModel.insert(note);
        }
        else if(requestCode==EDIT_REQUEST&&resultCode==RESULT_OK){
            int id=data.getIntExtra(NoteAddActivity.ID,-1);
            if(id==-1){
                Toast.makeText(MainActivity.this,"Note Can't be updated",Toast.LENGTH_SHORT).show();
                return;
            }
            String title=data.getStringExtra(NoteAddActivity.TITLE);
            String description=data.getStringExtra(NoteAddActivity.DESCRIPTION);
            int priority=data.getIntExtra(NoteAddActivity.PRIORITY,1);
            Note note=new Note(title,description,priority);
            note.setId(id);
            noteViewModel.update(note);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_menu:
                noteViewModel.deleteAllNotes();
                Toast.makeText(MainActivity.this,"All Notes Deleted",Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

