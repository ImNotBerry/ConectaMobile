package com.example.conectamobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddContactoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_conacto);

        EditText nombreContacto = findViewById(R.id.nombreContacto);
        EditText correoContacto = findViewById(R.id.correoContacto);
        Button btnCrearContacto = findViewById(R.id.btnCrearContacto);

        btnCrearContacto.setOnClickListener(v -> {
            String nombre = nombreContacto.getText().toString().trim();
            String correo = correoContacto.getText().toString().trim();

            if (nombre.isEmpty() || correo.isEmpty()) {
                Toast.makeText(AddContactoActivity.this, "Rellene todos los campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();

            if (currentUser == null) {
                Toast.makeText(AddContactoActivity.this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference contactsRef = FirebaseDatabase.getInstance()
                    .getReference("contacts")
                    .child(currentUser.getUid());

            String contactId = contactsRef.push().getKey();
            Contact contact = new Contact(contactId, nombre, correo);

            contactsRef.child(contactId).setValue(contact)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddContactoActivity.this, "Contacto agregado.", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddContactoActivity.this, "Error al agregar contacto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
