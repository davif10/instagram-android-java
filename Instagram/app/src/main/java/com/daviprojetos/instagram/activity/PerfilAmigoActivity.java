package com.daviprojetos.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daviprojetos.instagram.R;
import com.daviprojetos.instagram.helper.ConfiguracaoFirebase;
import com.daviprojetos.instagram.helper.UsuarioFirebase;
import com.daviprojetos.instagram.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAmigoActivity extends AppCompatActivity {
    private Usuario usuarioSelecionado;
    private Button buttonAcaoPerfil;
    private CircleImageView imagePerfil;
    private DatabaseReference firebaseRef;
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioAmigoRef;
    private DatabaseReference seguidoresRef;
    private ValueEventListener valueEventListenerPerfilAmigo;
    private TextView textPublicacoes, textSeguidores, textSeguindo;
    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigo);

        //Configurações iniciais
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        usuariosRef = firebaseRef.child("usuarios");
        seguidoresRef = firebaseRef.child("seguidores");
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        //Inicializar componentes
        inicializarComponentes();

        //Configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_fechar_preto_24);

        //Recupera usuario selecionado
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");
            //Configura nome do usuário na Toolbar
            getSupportActionBar().setTitle(usuarioSelecionado.getNome());

            //Recuperar foto do usuário
            String caminhoFoto = usuarioSelecionado.getCaminhoFoto();
            if(caminhoFoto !=null){
                Uri url = Uri.parse(caminhoFoto);
                Glide.with(PerfilAmigoActivity.this)
                        .load(url)
                        .into(imagePerfil);
            }
        }

        verificaSegueUsuarioAmigo();
    }

    private void verificaSegueUsuarioAmigo(){
        DatabaseReference seguidorRef = seguidoresRef
                .child(idUsuarioLogado)
                .child(usuarioSelecionado.getId());
        seguidorRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            //Já está seguindo
                            habilitarBotaoSeguir(true);

                        }else{
                            //Ainda não está seguindo
                            habilitarBotaoSeguir(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );

    }

    private void habilitarBotaoSeguir(boolean segueUsuario){

        if(segueUsuario){
            buttonAcaoPerfil.setText("Seguindo");
        }else{
            buttonAcaoPerfil.setText("Seguir");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarDadosPerfilAmigo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioAmigoRef.removeEventListener(valueEventListenerPerfilAmigo);
    }

    private void recuperarDadosPerfilAmigo(){
        usuarioAmigoRef = usuariosRef.child(usuarioSelecionado.getId());
        valueEventListenerPerfilAmigo = usuarioAmigoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    String postagens = String.valueOf(usuario.getPostagens());
                    String seguindo = String.valueOf(usuario.getSeguindo());
                    String seguidores = String.valueOf(usuario.getSeguidores());

                    //Configurar valores recuperados
                    textPublicacoes.setText(postagens);
                    textSeguidores.setText(seguidores);
                    textSeguindo.setText(seguindo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inicializarComponentes(){
        imagePerfil = findViewById(R.id.imagePerfil);
        buttonAcaoPerfil = findViewById(R.id.buttonAcaoPerfil);
        buttonAcaoPerfil.setText("Seguir");
        textPublicacoes = findViewById(R.id.textPublicacoes);
        textSeguidores = findViewById(R.id.textSeguidores);
        textSeguindo = findViewById(R.id.textSeguindo);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}