package com.viajet.itubus.activity.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth referenciaAutenticacao;
    private static StorageReference storage;

    // Retorna a referência do Database
    public static DatabaseReference getFirebase() {
        if (referenciaFirebase == null) {
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    // Retorna a instância do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao() {
        if (referenciaAutenticacao == null) {
            referenciaAutenticacao = FirebaseAuth.getInstance();
        }
        return referenciaAutenticacao;
    }

 //Retorna instancia do FirebaseStorage
    public static StorageReference getFirebaseStorage(){
        if( storage == null ){
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }

}

