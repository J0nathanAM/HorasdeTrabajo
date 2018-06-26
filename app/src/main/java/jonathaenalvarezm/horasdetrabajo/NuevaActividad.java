package jonathaenalvarezm.horasdetrabajo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by IDS Comercial on 04/10/2017.
 */

public class NuevaActividad extends AppCompatActivity {

    private SQLiteDatabase db;
    private ArrayList<Actividad> actividads;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DBHelper(this).getWritableDatabase();
        actividads=new ArrayList<Actividad>();

        actividads.add(new Actividad(1,"Normal"));
        actividads.add(new Actividad(2,"Vacaciones"));
        actividads.add(new Actividad(3,"Falta por enfermedad"));
        actividads.add(new Actividad(4,"Falta por Permiso"));

        for (int i=0;i<actividads.size();i++){
            insertar(actividads.get(i));
        }
    }

    public void insertar(Actividad actividad) {
        int id=actividad.getIdActividad();
        String act=actividad.getNombreActividad();
        if(db != null) {
            ContentValues valores = new ContentValues();
            valores.put("idActividad", id);
            valores.put("nombreActividad", act);
            db.insert("Actividad", null, valores);
            db.close();
        }
    }
}
