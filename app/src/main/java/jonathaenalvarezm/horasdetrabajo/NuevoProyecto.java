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

public class NuevoProyecto extends AppCompatActivity {

    private SQLiteDatabase db;
    private ArrayList<Proyecto> proyectos;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHelper(this).getWritableDatabase();
        proyectos=new ArrayList<Proyecto>();

        proyectos.add(new Proyecto(1,"Beca Móvil"));
        proyectos.add(new Proyecto(2,"Banamex"));

        for (int i=0;i<proyectos.size();i++){
            insertar(proyectos.get(i));
        }
    }

    public void insertar(Proyecto proyecto) {
        int id=proyecto.getIdPreyecto();
        String act=proyecto.getNombreProyecto();
            if(db != null) {
                ContentValues valores = new ContentValues();
                valores.put("idProyecto", id);
                valores.put("nombreProyecto", act);
                db.insert("Proyecto", null, valores);
                db.close();
            }
        }

    }
