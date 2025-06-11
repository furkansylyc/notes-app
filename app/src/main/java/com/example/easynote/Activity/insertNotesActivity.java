package com.example.easynote.Activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.text.format.DateFormat;

import com.example.easynote.BroadcastReceiver.ReminderReceiver;
import com.example.easynote.Model.Notes;
import com.example.easynote.R;
import com.example.easynote.ViewModel.NotesViewModel;
import com.example.easynote.databinding.ActivityInsertNotesBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

public class insertNotesActivity extends AppCompatActivity {

    ActivityInsertNotesBinding binding;
    String title, subtitle, notes;
    NotesViewModel notesViewModel;
    String priority = "3";
    FloatingActionButton menuButton, alarmButton, doneNotesButton;
    LinearLayout fabMenu;

    boolean isMenuOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        menuButton = findViewById(R.id.menuButton);
        alarmButton = findViewById(R.id.alarmButton);
        doneNotesButton = findViewById(R.id.doneNotesButton);
        fabMenu = findViewById(R.id.fabMenu);

        menuButton.setOnClickListener(v -> {
            if (isMenuOpen) {
                fabMenu.animate().alpha(0f).translationY(100).setDuration(300).withEndAction(() -> fabMenu.setVisibility(View.GONE)).start();
                isMenuOpen = false;
            } else {
                fabMenu.setVisibility(View.VISIBLE);
                fabMenu.setAlpha(0f);
                fabMenu.setTranslationY(100);
                fabMenu.animate().alpha(1f).translationY(0).setDuration(300).start();
                isMenuOpen = true;
            }
        });

        alarmButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (!alarmManager.canScheduleExactAlarms()) {
                    Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(intent);
                }
            }
            showDateTimePicker();
        });

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        binding.greenTag.setOnClickListener(v -> {
            binding.greenTag.setImageResource(R.drawable.done);
            binding.yellowTag.setImageResource(0);
            binding.redTag.setImageResource(0);
            priority = "1";
        });

        binding.yellowTag.setOnClickListener(v -> {
            binding.greenTag.setImageResource(0);
            binding.yellowTag.setImageResource(R.drawable.done);
            binding.redTag.setImageResource(0);
            priority = "2";
        });

        binding.redTag.setOnClickListener(v -> {
            binding.greenTag.setImageResource(0);
            binding.yellowTag.setImageResource(0);
            binding.redTag.setImageResource(R.drawable.done);
            priority = "3";
        });


        binding.doneNotesButton.setOnClickListener(view -> {
            title = binding.notesBaslik.getText().toString();
            subtitle = binding.notesAltBaslik.getText().toString();
            notes = binding.notesText.getText().toString();


            if (title.isEmpty() || notes.isEmpty()  ) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_warning, findViewById(R.id.toast_layout_root));
                TextView text = layout.findViewById(R.id.toast_text);
                text.setText("Başlık ve not alanı boş olamaz");

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            } else {
                CreateNotes(title, subtitle, notes);
            }

        });
    }

    private void CreateNotes(String title, String subtitle, String notes) {
        Date date = new Date();
        CharSequence sequence = DateFormat.format("dd/MM/yyyy", date.getTime());

        Notes note = new Notes();
        note.setNotesTitle(title);
        note.setNotesSubtitle(subtitle);
        note.setNotes(notes);
        note.setNotesPriority(priority);
        note.setNotesDate(sequence.toString());
        note.setIsFavorite(false);

        notesViewModel.insertNote(note);

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_custom, (ViewGroup) findViewById(R.id.toast_layout_root));
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        finish();
    }

    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();

        new DatePickerDialog(insertNotesActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);

            new TimePickerDialog(insertNotesActivity.this, (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                date.set(Calendar.SECOND, 0);

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container));
                TextView toastText = layout.findViewById(R.id.custom_toast_message);
                toastText.setText("Alarm Ayarlandı!");

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

                String title = binding.notesBaslik.getText().toString();
                String notes = binding.notesText.getText().toString();

                Intent intent = new Intent(getApplicationContext(), ReminderReceiver.class);
                intent.putExtra("title", title);
                intent.putExtra("message", notes);

                int requestCode = (int) System.currentTimeMillis();

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(),
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            date.getTimeInMillis(),
                            pendingIntent
                    );
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            date.getTimeInMillis(),
                            pendingIntent
                    );
                } else {
                    alarmManager.set(
                            AlarmManager.RTC_WAKEUP,
                            date.getTimeInMillis(),
                            pendingIntent
                    );
                }

            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();

        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }


}
