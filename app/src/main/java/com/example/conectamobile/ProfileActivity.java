package com.example.conectamobile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        usersRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());

        editTextName = findViewById(R.id.editTextName);

        // Leer el nombre del usuario desde Firebase
        usersRef.child("name").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                String name = snapshot.getValue(String.class);
                if (name != null) {
                    editTextName.setText(name);
                }
            } else {
                Toast.makeText(ProfileActivity.this, "Error al cargar el nombre", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnSave = findViewById(R.id.btnSaveProfile);
        btnSave.setOnClickListener(v -> saveName());

    }

    private void saveName() {
        String updatedName = editTextName.getText().toString();
        if (!updatedName.isEmpty()) {
            usersRef.child("name").setValue(updatedName).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Nombre actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Error al actualizar el nombre", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ProfileActivity.this, "Por favor ingresa un nombre", Toast.LENGTH_SHORT).show();
        }
    }
}
