package com.example.cantina.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {Usuario.class, Favorito.class, Comentario.class}, version = 2, exportSchema = false)
public abstract class AppBaseDeDatos extends RoomDatabase {

    static Executor executor = Executors.newSingleThreadExecutor();

    public abstract CantinaDao dao();

    private static volatile AppBaseDeDatos INSTANCE;

    public static AppBaseDeDatos getInstance(final Context context){
        if (INSTANCE == null){
            synchronized (AppBaseDeDatos.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, AppBaseDeDatos.class, "app.db")
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                }

                                @Override
                                public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
                                    super.onDestructiveMigration(db);
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
        @Dao
    public interface CantinaDao {
        @Insert
        void insertarUsuario(Usuario usuario);

        @Query("SELECT * FROM Usuario WHERE username = :nombre AND password = :contrasenya")
        Usuario autenticar(String nombre, String contrasenya);

        @Query("SELECT * FROM Usuario WHERE username = :nombre")
        Usuario comprobarNombreDisponible(String nombre);
/*
        @Query("SELECT * FROM Producto")
        LiveData<List<Producto>> obtenerProductos();

        @Query("SELECT * FROM Producto WHERE tipo ='S'")
        LiveData<List<Producto>> obtenerBocatas();

        @Query("SELECT * FROM Producto WHERE tipo ='C'")
        LiveData<List<Producto>> obtenerCafes();

        @Query("SELECT * FROM Producto WHERE tipo ='D'")
        LiveData<List<Producto>> obtenerBebidas();

        @Query("SELECT * FROM Producto ORDER BY nombre ASC")
        LiveData<List<Producto>> alfabeticamente();

        @Query("SELECT * FROM Producto ORDER BY precio ASC")
        LiveData<List<Producto>> caro();

        @Query("SELECT * FROM Producto WHERE nombre LIKE '%' || :d || '%'")
        LiveData<List<Producto>> buscar(String d);

        @Insert
        void insertarProducto(Producto producto);

        @Insert
        void insertarProductos(List<Producto> allProducts);

        //----------------
        //    FAVORITOS
        //----------------

        @Query("SELECT * FROM Producto JOIN Favorito USING(productoId) WHERE favorito.userId = :userId")
        LiveData<List<ProductoFavorito>> obtenerFavorito(int userId);

        //comprobar si el producto ya existe en favoritos
//        @Query("SELECT EXISTS (SELECT 1 FROM Favorito WHERE userId=:userId AND productoId = :productId)")
//        LiveData<Integer> esFavorito(int userId, int productId);

        @Query("SELECT *, CASE WHEN userId IS NOT NULL THEN 1 ELSE 0 END as esFavorito FROM Producto AS p LEFT JOIN FAVORITO AS Fav ON p.productoId = Fav.productoId WHERE Fav.userId = :userId")
        LiveData<List<ProductoFavorito>> productosFavoritos(int userId);

        //comprobar si un producto es favorito en carrito
        @Query("SELECT EXISTS (SELECT 1 FROM Favorito WHERE userId=:userId AND productoId = :productId)")
        LiveData<Integer> isFavorite(int userId, int productId);

        //buscar
        @Query("SELECT Producto.productoId, Producto.nombre, Producto.precio, Producto.tipo, Producto.img, CASE WHEN userId IS NOT NULL THEN 1 ELSE 0 END as esFavorito FROM Producto Producto LEFT JOIN (SELECT * FROM Favorito WHERE userId = :userId) AS Fav ON Producto.productoId = Fav.productoId WHERE tipo LIKE '%' || :d || '%' OR nombre LIKE '%' || :d ")
        LiveData<List<ProductoFavorito>> buscarFavorito(String d, int userId);

        @Insert
        void anadirFavorito(Favorito favorito);

        @Query("DELETE FROM Favorito WHERE userId = :userId AND productoId = :productoId")
        void eliminarProductoFavorito(int userId, int productoId);
*/
        //----------------
        //    CARRITO
        //----------------
        /*@Insert
        void anadirAlCarrito(LineaCarrito lineaCarrito);

        @Query("DELETE FROM LineaCarrito WHERE userId = :userId AND productoId = :productoId")
        void eliminarProductoDelCarrito(int userId, int productoId);

        //join
        @Query("SELECT * FROM Producto JOIN LineaCarrito USING(productoId)  WHERE lineacarrito.userId = :userId")
        LiveData<List<ProductoEnCarrito>> obtenerCarrito(int userId);

        //detele
        @Query("DELETE FROM LineaCarrito WHERE userId = :userId")
        void eliminarCarrito(int userId);
        */
        //----------------
        //    COMUNIDAD
        //----------------
        @Insert
        void insertarComentario(Comentario comentario);

        @Insert
        void comentariosIniciales(List<Comentario> comentario);

        @Query("SELECT * FROM Comentario")
        LiveData<List<Comentario>> obtenerComentarios();
    }
}
