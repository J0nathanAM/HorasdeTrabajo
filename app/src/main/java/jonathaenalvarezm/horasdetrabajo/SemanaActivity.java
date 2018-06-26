package jonathaenalvarezm.horasdetrabajo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jonathan EAM on 02/10/2017.
 */

public class SemanaActivity extends AppCompatActivity {

    private ArrayList<Registro> registros;
    private ListView lista;
    private TextView semana, totalhr;
    private String[] dias={"Domingo","Lunes","Martes", "Miércoles","Jueves","Viernes","Sábado"};
    private Date d=new Date();
    private SQLiteDatabase db;
    private SimpleDateFormat fecc;
    private int idsemana;
    private static final int VER_COMENTARIOS= Menu.FIRST;
    public static final int BORRAR_ID=Menu.FIRST+1;
    private float totalhoras;
    private final int MIN_X_HORA=60;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.semana_activity);
        super.onCreate(savedInstanceState);

        lista=(ListView)findViewById(R.id.listadias);
        registerForContextMenu(lista);

        semana=(TextView)findViewById(R.id.cajasemana);
        totalhr=(TextView)findViewById(R.id.totalhr);

        db = new DBHelper(this).getWritableDatabase();
        Date d=new Date();
        fecc=new SimpleDateFormat("yyyy-MM-dd");

        String clave=getIntent().getStringExtra("llave");

        if (clave.equals("actual")){
            semana.setText("Semana Actual");
            consultarSemana(getCursorActual());
        }
        
        if (clave.equals("anterior")){
            semana.setText("Semana Anterior");
            consultarSemana(getCursorAnterior());
        }

        lista.setAdapter(new AdapterDias());

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentn=new Intent(SemanaActivity.this,RegistroHorasActivity.class);
                intentn.putExtra("registro",registros.get(position));
                float hr=Float.parseFloat(registros.get(position).getHoras());
                if (hr<8) {
                    startActivity(intentn);
                    finish();
                }else {
                    Toast.makeText(SemanaActivity.this, "No puedes registrar más horas, maximo 8", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public Cursor getCursorActual(){
        Cursor cur=db.rawQuery("SELECT * FROM Semana",null);
        cur.moveToLast();
        return cur;
    }

    public Cursor getCursorAnterior(){
        Cursor cur=db.rawQuery("SELECT * FROM Semana",null);
        cur.moveToLast();
        cur.moveToPrevious();
        return cur;
    }

    public void consultarSemana(Cursor cursor){
        registros=new ArrayList<Registro>();

        Date inicio=new Date();
        Date fin=new Date();
        Date actual;

        try {
            inicio = fecc.parse(cursor.getString(1));
            fin = fecc.parse(cursor.getString(2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        idsemana = cursor.getInt(0);
        int dias=diferencia(inicio,fin);
        for (int i=0;i<=dias;i++){
            actual=sumarDias(inicio,i);
            String fecha=fecc.format(actual);
            Cursor h=db.rawQuery("SELECT SUM(horas) FROM RegistroDiaSemana WHERE dia='"+fecha+"' ",null);
            h.moveToFirst();
            float minu=h.getFloat(0);
            registros.add(new Registro(i,getDay(actual),fecha,minutosAhoras(h.getInt(0)),"",idsemana,0,0));
            totalhoras+=minu;
        }
        totalhr.setText("Total de Horas: "+minutosAhoras((int) totalhoras));
    }

    public int diferencia(Date inicio, Date fin){
        long diferenciaEn_ms = fin.getTime()-inicio.getTime();
        long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
        return (int) dias;
    }

    public Date sumarDias(Date fecha, int dias){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar.getTime();
    }

    public String getDay(Date d){
        int numeroDia=0;
        Calendar cal= Calendar.getInstance();
        cal.setTime(d);
        numeroDia=cal.get(Calendar.DAY_OF_WEEK);
        return dias[numeroDia-1];
    }

    public String minutosAhoras(int minutos) {
        String formato = "%02d.%02d";
        long horasReales = TimeUnit.MINUTES.toHours(minutos);
        long minutosReales = TimeUnit.MINUTES.toMinutes(minutos) - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(minutos));
        return String.format(formato, horasReales, minutosReales);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE,VER_COMENTARIOS,Menu.NONE,"Ver Detalles");
        menu.add(Menu.NONE,BORRAR_ID,Menu.NONE,"Eliminar");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case VER_COMENTARIOS:
                AdapterView.AdapterContextMenuInfo registroComentarios=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                comentarios(registroComentarios.id);
                break;

            case BORRAR_ID:
                AdapterView.AdapterContextMenuInfo registroBorrar=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                borrar(registroBorrar.id);
                break;
        }
        return true;
    }

    private void comentarios(final long rowid) {
        Intent intentn = new Intent(SemanaActivity.this, HistorialActivity.class);
        intentn.putExtra("idregistro", registros.get((int) rowid).getFecha());
        startActivity(intentn);
    }

    private void borrar (final long rowid){
        if(rowid>=0){
            final String fechaborrar=registros.get((int) rowid).getFecha();
            new AlertDialog.Builder(this)
                    .setTitle("¿Estas seguro de eliminar?")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.execSQL("DELETE FROM RegistroDiaSemana WHERE dia='"+fechaborrar+"'");
                            Toast.makeText(SemanaActivity.this, "Registro Eliminado", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
        }
    }

    class AdapterDias extends ArrayAdapter<Registro> {
        AdapterDias(){
            super(SemanaActivity.this,R.layout.row_dia,registros);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row = getLayoutInflater().inflate(R.layout.row_dia,parent,false);

            TextView dia = (TextView) row.findViewById(R.id.dia);
            TextView fecha = (TextView) row.findViewById(R.id.fecha);
            TextView horas = (TextView) row.findViewById(R.id.horas);

            Registro reg = registros.get(position);

            dia.setText(reg.getDia());
            fecha.setText(reg.getFecha());
            horas.setText(reg.getHoras());

            return row;
        }
    }
}
