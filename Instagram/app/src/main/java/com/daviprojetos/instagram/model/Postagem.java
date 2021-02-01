package com.daviprojetos.instagram.model;

import com.daviprojetos.instagram.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Postagem implements Serializable {
    /*
    *Modelo de postagem
    * postagens
    * <id_usuario>
    *   <id_postagem_firebase>
    *       descricao
    *       caminhoFoto
    *       idUsuario
     */
    private String id;
    private String idUsuario;
    private String descricao;
    private String caminhoFoto;

    public Postagem() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference postagemref = firebaseRef.child("postagens");
        String idPostagem = postagemref.push().getKey();
        setId(idPostagem);
    }

    public boolean salvar(){
        DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebase();
        DatabaseReference postagensRef = firebaseref.child("postagens")
                .child(getIdUsuario())
                .child(getId());
        postagensRef.setValue(this);
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
