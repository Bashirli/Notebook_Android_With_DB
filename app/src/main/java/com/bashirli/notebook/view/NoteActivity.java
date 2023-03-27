package com.bashirli.notebook.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bashirli.notebook.R;
import com.google.firebase.auth.FirebaseAuth;

public class NoteActivity extends AppCompatActivity {
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
    auth=FirebaseAuth.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.optionsmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(R.id.signout==item.getItemId()){
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            auth.signOut();
            finish();

        }else if(R.id.adddata==item.getItemId()){
            FirstFragmentDirections.ActionFirstFragmentToPublishFragment navi=FirstFragmentDirections.actionFirstFragmentToPublishFragment("new");
            navi.setInfo("new");
            Navigation.findNavController(this,R.id.fragmentContainerView).navigate(navi);

        }

        return super.onOptionsItemSelected(item);
    }

}