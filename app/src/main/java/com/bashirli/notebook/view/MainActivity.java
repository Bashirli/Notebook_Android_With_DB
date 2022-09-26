package com.bashirli.notebook.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bashirli.notebook.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        if(user!=null){
            Intent intent=new Intent(this,NoteActivity.class);
            startActivity(intent);
            finish();
        }

    }


    public int problem_tapma(){
        if(binding.editTextTextPersonName.getText().toString().equals("")||
                binding.editTextTextPassword.getText().toString().equals("")){
            Toast.makeText(this, "Xana boş buraxılıb", Toast.LENGTH_LONG).show();
            return 0;
        }

        if(binding.editTextTextPassword.getText().toString().length()<6){
            Toast.makeText(this, "Parol minimum 6 simvol olmalıdır", Toast.LENGTH_SHORT).show();
            return 0;
        }



        if(!binding.checkBox.isChecked()){
            Toast.makeText(this, "Robot olmadığınızı təsdiq edin", Toast.LENGTH_SHORT).show();
            return 0;
        }

        return 1;
    }


    public void login(View view){
if(problem_tapma()==0){
    return;

}

String email=binding.editTextTextPersonName.getText().toString();
String password=binding.editTextTextPassword.getText().toString();

auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
    @Override
    public void onSuccess(AuthResult authResult) {
        Intent intent=new Intent(getApplicationContext(),NoteActivity.class);
        startActivity(intent);
        finish();
    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        System.out.println(e.getLocalizedMessage());

    }
});


    }

    public void register(View view){
        Intent intent=new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

}