package com.bashirli.notebook.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bashirli.notebook.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();


    }

    public void login(View view){

    }

    public int problem_tapma(){
        if(binding.editTextTextPersonName2.getText().toString().equals("")||
                binding.editTextTextPassword2.getText().toString().equals("")||
                binding.editTextTextPassword3.getText().toString().equals("")){
            Toast.makeText(this, "Xana boş buraxılıb", Toast.LENGTH_LONG).show();
            return 0;
        }

        if(binding.editTextTextPassword2.getText().toString().length()<6){
            Toast.makeText(this, "Parol minimum 6 simvol olmalıdır", Toast.LENGTH_SHORT).show();
            return 0;
        }

        if(!binding.editTextTextPassword3.getText().toString().equals(binding.editTextTextPassword2.getText().toString())){
            Toast.makeText(this, "Parollar uyğunlaşmır", Toast.LENGTH_SHORT).show();
            return 0;
        }

        if(!binding.checkBox2.isChecked()){
            Toast.makeText(this, "Robot olmadığınızı təsdiq edin", Toast.LENGTH_SHORT).show();
            return 0;
        }

        return 1;
    }

    public void reg(View view){
        if(problem_tapma()==0){
            return;
        }
        String email=binding.editTextTextPersonName2.getText().toString();
        String password=binding.editTextTextPassword3.getText().toString();
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(RegisterActivity.this, "Hesab uğurla yaradıldı!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}