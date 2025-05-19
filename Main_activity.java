package com.example.notes_mad;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    EditText noteInput;
    Button saveButton;
    ListView notesList;

    ArrayList<String> notes;
    ArrayAdapter<String> adapter;
    SharedPreferences sharedPreferences;
    static final String PREFS_NAME = "NotesPrefs";
    static final String NOTES_KEY = "Notes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // ensure XML name matches

        noteInput = findViewById(R.id.noteInput);
        saveButton = findViewById(R.id.saveButton);
        notesList = findViewById(R.id.notesList);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load notes from SharedPreferences
        Set<String> notesSet = sharedPreferences.getStringSet(NOTES_KEY, new HashSet<>());
        notes = new ArrayList<>(notesSet);

        // Display trimmed notes in the list
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notes) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                String fullNote = getItem(position);
                if (fullNote != null && fullNote.length() > 50) {
                    ((android.widget.TextView) view).setText(fullNote.substring(0, 47) + "...");
                }
                return view;
            }
        };

        notesList.setAdapter(adapter);

        saveButton.setOnClickListener(v -> {
            String newNote = noteInput.getText().toString().trim();
            if (!newNote.isEmpty()) {
                notes.add(0, newNote); // add latest note to top
                adapter.notifyDataSetChanged();
                saveNotes();
                noteInput.setText(""); // clear input
            }
        });
    }

    private void saveNotes() {
        Set<String> notesSet = new HashSet<>(notes);
        sharedPreferences.edit().putStringSet(NOTES_KEY, notesSet).apply();
    }
}
