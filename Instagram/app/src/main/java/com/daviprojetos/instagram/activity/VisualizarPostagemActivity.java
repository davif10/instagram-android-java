package com.daviprojetos.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daviprojetos.instagram.R;
import com.daviprojetos.instagram.helper.ConfiguracaoFirebase;
import com.daviprojetos.instagram.helper.UsuarioFirebase;
import com.daviprojetos.instagram.model.Postagem;
import com.daviprojetos.instagram.model.PostagemCurtida;
import com.daviprojetos.instagram.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisualizarPostagemActivity extends AppCompatActivity {
    private TextView textPerfilPostagem, textQtdCurtidasPostagem,
            textDescricaoPostagem;
    private ImageView imagePostagemSelecionada;
    private CircleImageView imagePerfilPostagem;
    private ImageView imageComentarios;
    private LikeButton likeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_postagem);

        //Inicializar componentes
        inicializarComponentes();

        //Configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Visualizar Postagem");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_fechar_preto_24);

        //Recuperar dados da Activity
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Postagem postagem = (Postagem) bundle.getSerializable("postagem");
            Usuario usuario = (Usuario) bundle.getSerializable("usuario");

            //Exibe dados de usuario
            Uri uri = Uri.parse(usuario.getCaminhoFoto());
            Glide.with(VisualizarPostagemActivity.this)
                    .load(uri)
                    .into(imagePerfilPostagem);

            textPerfilPostagem.setText(usuario.getNome());
            //Exibe dados da postagem
            Uri uriPostagem = Uri.parse(postagem.getCaminhoFoto());
            Glide.with(VisualizarPostagemActivity.this)
                    .load(uriPostagem)
                    .into(imagePostagemSelecionada);

            textDescricaoPostagem.setText(postagem.getDescricao());
            imageComentarios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), ComentariosActivity.class);
                    i.putExtra("idPostagem",postagem.getId());
                    startActivity(i);
                }
            });

            //Recuperar dados da postagem curtidas
            DatabaseReference curtidasRef = ConfiguracaoFirebase.getFirebase()
                    .child("postagens-curtidas")
                    .child(postagem.getId());

            curtidasRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int qtdCurtidas = 0;
                    Usuario usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
                    if(dataSnapshot.hasChild("qtdCurtidas")){
                        PostagemCurtida postagemCurtida = dataSnapshot.getValue(PostagemCurtida.class);
                        qtdCurtidas = postagemCurtida.getQtdCurtidas();
                    }
                    //Verifica se já foi clicado
                    if(dataSnapshot.hasChild(usuarioLogado.getId())){
                        likeButton.setLiked(true);
                    }else{
                        likeButton.setLiked(false);
                    }

                    //Monta objeto postagem curtida
                    PostagemCurtida curtida = new PostagemCurtida();
                    curtida.setPostagem(postagem);
                    curtida.setUsuario(usuarioLogado);
                    curtida.setQtdCurtidas(qtdCurtidas);

                    //Adiciona eventos para curtir uma foto
                    likeButton.setOnLikeListener(new OnLikeListener() {
                        @Override
                        public void liked(LikeButton likeButton) {
                            curtida.salvarCurtidaNoVisualizarPostagem();
                            textQtdCurtidasPostagem.setText(curtida.getQtdCurtidas() + " curtidas");
                        }

                        @Override
                        public void unLiked(LikeButton likeButton) {
                            curtida.removerCurtidaNoVisualizarPostagem();
                            textQtdCurtidasPostagem.setText(curtida.getQtdCurtidas() + " curtidas");
                        }
                    });

                    textQtdCurtidasPostagem.setText(curtida.getQtdCurtidas() + " curtidas");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    private void inicializarComponentes(){
        textPerfilPostagem = findViewById(R.id.textPerfilPostagem);
        textQtdCurtidasPostagem = findViewById(R.id.textQtdCurtidasPostagem);
        textDescricaoPostagem = findViewById(R.id.textDescricaoPostagem);
        imagePerfilPostagem = findViewById(R.id.imagePerfilPostagem);
        imagePostagemSelecionada = findViewById(R.id.imagePostagemSelecionada);
        imageComentarios = findViewById(R.id.imageComentarioFeed);
        likeButton = findViewById(R.id.likeButtonFeed);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}