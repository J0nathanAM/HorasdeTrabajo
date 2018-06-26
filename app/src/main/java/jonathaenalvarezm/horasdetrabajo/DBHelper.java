package jonathaenalvarezm.horasdetrabajo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jonathan EAM on 02/10/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "registro.db";
    private static final int DATABASE_VERSION=1;

    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tabla = "CREATE TABLE Recurso(idRecurso INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "nombre VARCHAR(100) NOT NULL,"+
                "apPaterno VARCHAR(50) NOT NULL," +
                "apMaterno VARCHAR(50) NOT NULL," +
                "email VARCHAR(50) NOT NULL" +
                ");";
        db.execSQL(tabla);

        String tabla2 = "CREATE TABLE Semana(idSemana INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "fechaInicio DATETIME NOT NULL,"+
                "fechaFin DATETIME NOT NULL," +
                "status INTEGER NOT NULL," +
                "idRecurso INTEGER NOT NULL," +
                "FOREIGN KEY(idRecurso) REFERENCES Recurso(idRecurso)" +
                ");";
        db.execSQL(tabla2);

        String tabla3 = "CREATE TABLE RegistroDiaSemana(idRegistroDiaSemana INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "dia DATETIME NOT NULL,"+
                "horas INTEGER NOT NULL," +
                "comentario VARCHAR(500) NOT NULL," +
                "idSemana INTEGER NOT NULL," +
                "idActividad INTEGER NOT NULL," +
                "idProyecto INTEGER NOT NULL," +
                "FOREIGN KEY(idSemana) REFERENCES Semana(idSemana)," +
                "FOREIGN KEY(idActividad) REFERENCES Actividad(idActividad)" +
                ");";
        db.execSQL(tabla3);

        String tabla4 = "CREATE TABLE Proyecto(idProyecto INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "nombreProyecto VARCHAR(50) NOT NULL"+
                ");";
        db.execSQL(tabla4);

        String tabla5 = "CREATE TABLE Actividad(idActividad INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "nombreActividad VARCHAR(30) NOT NULL"+
                ");";
        db.execSQL(tabla5);

        ContentValues valores = new ContentValues();
        valores.put("nombreActividad", "Normal");
        db.insert("Actividad", null, valores);

        valores.put("nombreActividad", "Vacaciones");
        db.insert("Actividad", null, valores);

        valores.put("nombreActividad", "Falta por enfermedad");
        db.insert("Actividad", null, valores);

        valores.put("nombreActividad", "Falta por permiso");
        db.insert("Actividad", null, valores);

        ContentValues valores2 = new ContentValues();
        valores2.put("nombreProyecto", "Beca Móvil");
        db.insert("Proyecto", null, valores2);

        valores2.put("nombreProyecto", "Banamex");
        db.insert("Proyecto", null, valores2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Recurso");
        onCreate(db);
    }


}
