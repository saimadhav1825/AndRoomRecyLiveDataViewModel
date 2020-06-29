package com.example.mvvmach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class NoteAddActivity extends AppCompatActivity {
    public static final String ID = "Id";
    public static final String TITLE = "Title";
    public static final String DESCRIPTION = "Description";
    public static final String PRIORITY = "Priority";
    private EditText editTitle;
    private EditText editDescription;
    private NumberPicker numPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);
        editTitle = findViewById(R.id.edit_title);
        editDescription = findViewById(R.id.edit_description);
        numPriority = findViewById(R.id.pic_priority);
        numPriority.setMinValue(1);
        numPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_);
        Intent intent = new Intent();
        if (intent.hasExtra(ID)) {
            setTitle("Edit Notes");
            editTitle.setText(intent.getStringExtra(TITLE));
            editDescription.setText(intent.getStringExtra(DESCRIPTION));
            numPriority.setValue(intent.getIntExtra(PRIORITY, 1));
        }
        setTitle("Add notes");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        String title = editTitle.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        int priority = numPriority.getValue();
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "ADD notes", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(TITLE, title);
        data.putExtra(DESCRIPTION, description);
        data.putExtra(PRIORITY, priority);
        int id = getIntent().getIntExtra(ID, -1);
        if (id != -1) {
            data.putExtra(ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }
}
