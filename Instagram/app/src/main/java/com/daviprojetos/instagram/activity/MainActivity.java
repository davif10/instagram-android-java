package com.daviprojetos.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.daviprojetos.instagram.R;
import com.daviprojetos.instagram.helper.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Instagram");
        setSupportActionBar(toolbar);

        //Configurações de objetos
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_sair:
                deslogarUsuario();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario(){
        try {
            autenticacao.signOut();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}