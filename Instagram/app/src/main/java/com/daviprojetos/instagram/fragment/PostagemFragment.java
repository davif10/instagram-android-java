package com.daviprojetos.instagram.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.daviprojetos.instagram.R;
import com.daviprojetos.instagram.helper.Permissao;


public class PostagemFragment extends Fragment {
    private Button buttonAbrirCamera;
    private Button buttonAbrirGaleria;
    private static  final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public PostagemFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_postagem, container, false);

        //Validar permissões
        Permissao.validarPermissoes(permissoesNecessarias,getActivity(),1);

        //Inicializar componentes
        buttonAbrirCamera = view.findViewById(R.id.buttonAbrirCamera);
        buttonAbrirGaleria = view.findViewById(R.id.buttonAbrirGaleria);

        //Adicionar evento de clique no botão câmera
        buttonAbrirCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getActivity().getPackageManager())!=null){
                    startActivityForResult(i, SELECAO_CAMERA);
                }

            }
        });

        //Adiciona evento de clique no botao galeria

        buttonAbrirGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getActivity().getPackageManager())!=null){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });
        return view;
    }
}