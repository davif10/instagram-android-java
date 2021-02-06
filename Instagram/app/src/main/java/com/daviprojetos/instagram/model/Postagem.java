package com.daviprojetos.instagram.model;

import com.daviprojetos.instagram.helper.ConfiguracaoFirebase;
import com.daviprojetos.instagram.helper.UsuarioFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

    public boolean salvar(DataSnapshot seguidoresSnapshot){
        Map objeto = new HashMap();
        Usuario usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();

        //Referência para postagem
        String combinacaoId = "/"+getIdUsuario() + "/"+getId();
        objeto.put("/postagens" + combinacaoId, this);

        //Referencia do objeto para salvar
        HashMap<String, Object> dadosSeguidor = new HashMap<>();

        //Referência para postagem
       for(DataSnapshot seguidores: seguidoresSnapshot.getChildren()){
            /*
                   +feed
                    +id_seguidor<jose renato>
                     +id_postagem<01>
                       postagem<por Jamilton>
             */

           String idSeguidor = seguidores.getKey();
           //Monta objeto para salvar
           dadosSeguidor.put("fotoPostagem",getCaminhoFoto());
           dadosSeguidor.put("descricao",getDescricao());
           dadosSeguidor.put("id",getId());

           dadosSeguidor.put("nomeUsuario",usuarioLogado.getNome());
           dadosSeguidor.put("fotoUsuario",usuarioLogado.getCaminhoFoto());

           String idsAtualizacao = "/" + idSeguidor + "/"+getId();
           objeto.put("/feed" + idsAtualizacao, dadosSeguidor);

       }
        String idAtualizacaoUsuarioLogado = "/" + getIdUsuario() + "/"+getId();
        objeto.put("/feed" + idAtualizacaoUsuarioLogado, dadosSeguidor);


        firebaseRef.updateChildren(objeto);

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
