package com.example.proyectocantina;

import java.util.ArrayList;
import java.util.List;

public class ComunidadRepository {

    List<Comunidad> comunidadList = new ArrayList<>();

    interface Callback {
        void cuandoFinalice(List<Comunidad> comunidad);
    }

    ComunidadRepository(){
        comunidadList.add(new Comunidad("Juan Gabriel Barrio", "FANTÁSTICO", "Los triángulos son la especialidad de la Cantina, sin duda mi elección favorita para la hora del patio.", 4.5f));
        comunidadList.add(new Comunidad("Martina Orellana", "¡GÉNIAL!", "Buen servicio y bocatas buenísimos. Lo recomiendo 100%", 5f));
        comunidadList.add(new Comunidad("Elias Pujol", "MAL, MUY MAL", "Poco café y mucha agua,no parece express.", 1f));
        comunidadList.add(new Comunidad("Rodrigo Almagro", "Bastante bien..", "Está bien, el servicio es rápido", 4f));
        comunidadList.add(new Comunidad("Maximiliano Canales", "MUCHA COLA", "Las colas del dia jueves son eternas...", 2f));
    }

    List<Comunidad> obtener() {
        return comunidadList;
    }

    void insertar(Comunidad comunidad, Callback callback){
        comunidadList.add(comunidad);
        callback.cuandoFinalice(comunidadList);
    }
}
