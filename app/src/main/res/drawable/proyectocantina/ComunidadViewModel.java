package com.example.proyectocantina;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ComunidadViewModel extends AndroidViewModel {

    ComunidadRepository comunidadRepository;

    MutableLiveData<List<Comunidad>> listComunidadMutableLiveData = new MutableLiveData<>();
    MutableLiveData<Comunidad> comunidadSelecionado = new MutableLiveData<>();

    public ComunidadViewModel(@NonNull Application application) {
        super(application);

        comunidadRepository = new ComunidadRepository();

        listComunidadMutableLiveData.setValue(comunidadRepository.obtener());
    }

    MutableLiveData<List<Comunidad>> obtener(){
        return listComunidadMutableLiveData;
    }

    void insertar(Comunidad comunidad){
        comunidadRepository.insertar(comunidad, new ComunidadRepository.Callback() {
            @Override
            public void cuandoFinalice(List<Comunidad> comunidadList) {
                listComunidadMutableLiveData.setValue(comunidadList);
            }
        });
    }

    void seleccionar(Comunidad comunidad){
        comunidadSelecionado.setValue(comunidad);
    }

    MutableLiveData<Comunidad> seleccionado(){
        return comunidadSelecionado;
    }
}
