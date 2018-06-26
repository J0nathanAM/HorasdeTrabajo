package jonathaenalvarezm.horasdetrabajo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by IDS Comercial on 03/10/2017.
 */

public class HistorialActivity extends AppCompatActivity {

    ListView historial;
    private SQLiteDatabase db;
    private String fecha;
    private ArrayList<Registro> datos;
    private final int MIN_X_HORA=60;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.historial_activity);
        super.onCreate(savedInstanceState);

        historial=(ListView)findViewById(R.id.listahistorial);
        datos=new ArrayList<Registro>();
        db = new DBHelper(this).getWritableDatabase();

        fecha=getIntent().getStringExtra("idregistro");

        Cursor cur=db.rawQuery("SELECT * FROM RegistroDiaSemana WHERE dia='"+fecha+"' ",null);
        cur.moveToFirst();
        for (int i=0;i<cur.getCount();i++){
            datos.add(new Registro(cur.getInt(0),null,cur.getString(1),cur.getString(2),
                    cur.getString(3),cur.getInt(4),cur.getInt(5),cur.getInt(6)));
            cur.moveToNext();
        }
        historial.setAdapter(new AdapterHistorial());

        historial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actualizar(datos.get(position).getId());
            }
        });
    }

    private void actualizar(final int id){
        Cursor cactualizar=db.rawQuery("SELECT comentario FROM RegistroDiaSemana WHERE idRegistroDiaSemana="+id+" ",null);
        cactualizar.moveToFirst();

        final String comen=cactualizar.getString(cactualizar.getColumnIndex("comentario"));

        LayoutInflater inflater =LayoutInflater.from(this);
        View row = inflater.inflate(R.layout.editar,null);
        final EditText com=(EditText)row.findViewById(R.id.etcomentario);

        com.setText(comen);

            new AlertDialog.Builder(this)
                    .setTitle("Actualizar Comentario")
                    .setView(row)
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            db.execSQL("UPDATE RegistroDiaSemana SET comentario='"+com.getText().toString().trim()
                                    +"' WHERE idRegistroDiaSemana="+id+" ");
                            finish();
                        }
                    })
                    .show();
        }

    class AdapterHistorial extends ArrayAdapter<Registro> {
        AdapterHistorial(){
            super(HistorialActivity.this,R.layout.row_comentario,datos);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row = getLayoutInflater().inflate(R.layout.row_comentario,parent,false);

            TextView proy = (TextView) row.findViewById(R.id.proye);
            TextView activ = (TextView) row.findViewById(R.id.activ);
            TextView hor = (TextView) row.findViewById(R.id.hor);
            TextView comen = (TextView) row.findViewById(R.id.comen);

            Registro reg = datos.get(position);

            int idp=reg.getIdProyecto();
            int ida=reg.getIdActividad();
            String hr=minutosAhoras(Integer.parseInt(reg.getHoras()));
            //proy.setText(String.valueOf(idp));
            proy.setText("Proyecto: "+getDato("nombreProyecto","Proyecto","idProyecto",idp));
            activ.setText("Actividad: "+getDato("nombreActividad","Actividad","idActividad",ida));
            hor.setText("Horas: "+hr);
            comen.setText("Comentario: "+reg.getComentario());

            return row;
        }
    }

    public String getDato(String dato, String tabla, String where, int iddatos){
        String sql="SELECT "+dato+" FROM "+tabla+" WHERE "+where+"="+iddatos;
        //System.out.println(">>>>>>"+sql);
        Cursor data=db.rawQuery(sql,null);
        data.moveToFirst();
        return data.getString(0);
    }

    public String minutosAhoras(int minutos) {
        String formato = "%02d.%02d";
        long horasReales = TimeUnit.MINUTES.toHours(minutos);
        long minutosReales = TimeUnit.MINUTES.toMinutes(minutos) - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(minutos));
        return String.format(formato, horasReales, minutosReales);
    }
}
