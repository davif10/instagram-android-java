package com.daviprojetos.instagram.model;

import com.daviprojetos.instagram.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class PostagemCurtida {
    private int qtdCurtidas = 0;
    private Feed feed;
    private Usuario usuario;

    public PostagemCurtida() {
    }

    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        //Objeto usuario
        HashMap<String, Object> dadosUsuario = new HashMap<>();
        dadosUsuario.put("nomeUsuario",usuario.getNome());
        dadosUsuario.put("caminhoFoto",usuario.getCaminhoFoto());

        DatabaseReference pCurtidasRef = firebaseRef.child("postagens-curtidas")
                .child(feed.getId())//Id_postagem
                .child(usuario.getId());//id_usuario_logado
        pCurtidasRef.setValue(dadosUsuario);

        //Atualizar quantidade de curtidas
        atualizarQtd(1);
    }

    public void remover(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pCurtidasRef = firebaseRef.child("postagens-curtidas")
                .child(feed.getId())//Id_postagem
                .child(usuario.getId());//id_usuario_logado
        pCurtidasRef.removeValue();

        //Atualizar quantidade de curtidas
        atualizarQtd(-1);
    }
    public void atualizarQtd(int valor){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pCurtidasRef = firebaseRef.child("postagens-curtidas")
                .child(feed.getId())//Id_postagem
                .child("qtdCurtidas");
        setQtdCurtidas(getQtdCurtidas() + valor);
        pCurtidasRef.setValue(getQtdCurtidas());
    }

    public int getQtdCurtidas() {
        return qtdCurtidas;
    }

    public void setQtdCurtidas(int qtdCurtidas) {
        this.qtdCurtidas = qtdCurtidas;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
