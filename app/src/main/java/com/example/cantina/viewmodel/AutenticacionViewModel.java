package com.example.cantina.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.cantina.model.AutenticacionManager;
import com.example.cantina.model.CantinaRepository;
import com.example.cantina.model.Usuario;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AutenticacionViewModel extends AndroidViewModel {
    Executor executor = Executors.newSingleThreadExecutor();

    public enum EstadoDeLaAutenticacion {
        NO_AUTENTICADO,
        AUTENTICADO,
        AUTENTICACION_INVALIDA
    }

    public enum EstadoDelRegistro {
        INICIO_DEL_REGISTRO,
        NOMBRE_NO_DISPONIBLE,
        REGISTRO_COMPLETADO
    }

    public MutableLiveData<EstadoDeLaAutenticacion> estadoDeLaAutenticacion = new MutableLiveData<>(EstadoDeLaAutenticacion.NO_AUTENTICADO);
    public MutableLiveData<EstadoDelRegistro> estadoDelRegistro = new MutableLiveData<>(EstadoDelRegistro.INICIO_DEL_REGISTRO);
    public MutableLiveData<Usuario> usuarioAutenticado = new MutableLiveData<>();

    AutenticacionManager autenticacionManager;
    CantinaRepository cantinaRepository;

    public AutenticacionViewModel(@NonNull Application application) {
        super(application);
        autenticacionManager = new AutenticacionManager(application);
    }


    public void iniciarSesion(String username, String password){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                autenticacionManager.iniciarSesion(username, password, new AutenticacionManager.IniciarSesionCallback() {
                    @Override
                    public void cuandoUsuarioAutenticado(Usuario usuario) {
                        usuarioAutenticado.postValue(usuario);
                        estadoDeLaAutenticacion.postValue(EstadoDeLaAutenticacion.AUTENTICADO);
                    }

                    @Override
                    public void cuandoAutenticacionNoValida() {
                        estadoDeLaAutenticacion.postValue(EstadoDeLaAutenticacion.AUTENTICACION_INVALIDA);
                    }
                });
            }
        });
    }
    //buscar
    MutableLiveData<String> terminoBusqueda = new MutableLiveData<>();

    public void iniciarRegistro(){
        estadoDelRegistro.postValue(EstadoDelRegistro.INICIO_DEL_REGISTRO);
    }

    public void crearCuentaEIniciarSesion(String username, String password, String biography){
        autenticacionManager.crearCuenta(username, password, biography, new AutenticacionManager.RegistrarCallback() {
            @Override
            public void cuandoRegistroCompletado() {
                estadoDelRegistro.postValue(EstadoDelRegistro.REGISTRO_COMPLETADO);
                iniciarSesion(username, password);
            }

            @Override
            public void cuandoNombreNoDisponible() {
                estadoDelRegistro.postValue(EstadoDelRegistro.NOMBRE_NO_DISPONIBLE);
            }
        });
    }

    public void cerrarSesion(){
        usuarioAutenticado.postValue(null);
        estadoDeLaAutenticacion.postValue(EstadoDeLaAutenticacion.NO_AUTENTICADO);
    }
}
