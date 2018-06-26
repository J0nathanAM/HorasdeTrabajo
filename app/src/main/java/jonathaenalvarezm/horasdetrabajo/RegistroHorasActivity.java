package jonathaenalvarezm.horasdetrabajo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Jonathan EAM on 02/10/2017.
 */

public class RegistroHorasActivity extends AppCompatActivity {

    TextView dia,fechareg,horasactuales,comentario;
    Button btnGuardar;
    Spinner hrs,min;
    Spinner actividad, proyecto;
    private SQLiteDatabase db;
    private ContentValues cv;
    private int idSemana,idProyecto,idActividad;
    private String cadhora;
    private float calcHoras;
    private float hactuales;
    private ArrayList<String> datos;
    private final int MIN_X_HORA=60;
    private final double HORAS_MINIMO=0.30;
    private final int HORAS_MAXIMO=8;
    private int hora_a_minutos;
    private int minutos;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.registro_horas_activity);
        super.onCreate(savedInstanceState);

        dia=(TextView)findViewById(R.id.diaregistro);
        fechareg=(TextView)findViewById(R.id.fecharegistro);
        horasactuales=(TextView)findViewById(R.id.horasactual);
        comentario=(EditText)findViewById(R.id.comentario);
        hrs=(Spinner) findViewById(R.id.hr);
        min=(Spinner) findViewById(R.id.min);
        actividad=(Spinner) findViewById(R.id.tipoactividad);
        proyecto=(Spinner) findViewById(R.id.proyecto);
        btnGuardar=(Button)findViewById(R.id.btnguardarnuevo);
        db = new DBHelper(this).getWritableDatabase();

        final Registro r=(Registro)getIntent().getExtras().getSerializable("registro");

        dia.setText(r.getDia());
        fechareg.setText(r.getFecha());
        hactuales=Float.parseFloat(r.getHoras());
        horasactuales.setText(String.valueOf(hactuales));
        comentario.setText(r.getComentario());
        idSemana=r.getIdSemana();

        llenarSpinner(consulta("SELECT * FROM Actividad"),actividad);
        llenarSpinner(consulta("SELECT * FROM Proyecto"),proyecto);

        actividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!actividad.getSelectedItem().toString().equals("Normal")){
                    datos=new ArrayList<String>();
                    datos.add("04");
                    datos.add("08");
                    llenarSpinner(datos,hrs);
                    datos=new ArrayList<String>();
                    datos.add("00");
                    llenarSpinner(datos,min);
                }else{
                    datos=new ArrayList<String>();
                    datos.add("00");
                    datos.add("01");
                    datos.add("02");
                    datos.add("03");
                    datos.add("04");
                    datos.add("05");
                    datos.add("06");
                    datos.add("07");
                    datos.add("08");
                    llenarSpinner(datos,hrs);
                    datos=new ArrayList<String>();
                    datos.add("00");
                    datos.add("10");
                    datos.add("20");
                    datos.add("30");
                    datos.add("40");
                    datos.add("50");
                    llenarSpinner(datos,min);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hora_a_minutos=Integer.parseInt(hrs.getSelectedItem().toString())*MIN_X_HORA;
                minutos=Integer.parseInt(min.getSelectedItem().toString());

                cadhora=hrs.getSelectedItem().toString()+"."+min.getSelectedItem().toString();
                calcHoras=Float.parseFloat(cadhora);


                if (calcHoras>=HORAS_MINIMO && calcHoras <=HORAS_MAXIMO && (hactuales+calcHoras) <= HORAS_MAXIMO) {
                    registrar();
                    Intent service = new Intent(RegistroHorasActivity.this, MyService.class);
                    startService(service);
                }else{
                    Toast.makeText(RegistroHorasActivity.this, "Solo puede registrar entre 30 min a 8 hrs.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void registrar(){
        if (!comentario.getText().toString().equals("") && !comentario.getText().toString().isEmpty()) {
            idProyecto= (int) proyecto.getSelectedItemId()+1;
            idActividad= (int) actividad.getSelectedItemId()+1;
            int total_minutos= hora_a_minutos+minutos;
            //Toast.makeText(this, String.valueOf(idProyecto)+"-"+String.valueOf(idActividad), Toast.LENGTH_SHORT).show();
            cv = new ContentValues();
            cv.put("dia", fechareg.getText().toString());
            cv.put("horas", total_minutos);
            cv.put("comentario", comentario.getText().toString());
            cv.put("idSemana", idSemana);
            cv.put("idActividad", idActividad);
            cv.put("idProyecto", idProyecto);
            db.insert("RegistroDiaSemana", null, cv);
            db.close();

            Intent intent = new Intent(RegistroHorasActivity.this, PrincipalActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Escribe un Comentario", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList consulta(String sql){
        ArrayList<String> d=new ArrayList<String>();
        Cursor cur=db.rawQuery(sql,null);
        cur.moveToFirst();
        for (int i=0;i<cur.getCount();i++){
            d.add(cur.getString(1));
            cur.moveToNext();
        }
        return d;
    }

    public void llenarSpinner(ArrayList datos, Spinner spinner){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, datos);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
}
