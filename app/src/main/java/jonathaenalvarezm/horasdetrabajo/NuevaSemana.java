package jonathaenalvarezm.horasdetrabajo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by IDS Comercial on 03/10/2017.
 */

public class NuevaSemana extends AppCompatActivity {

    private SQLiteDatabase db;
    private Button guardar;

    CalendarView simpleCalendarView;
    private SimpleDateFormat fecc;
    private Date date;
    private String[] dias={"Domingo","Lunes","Martes", "Miércoles","Jueves","Viernes","Sábado"};
    private String fecha1, fecha2;
    private String isLunes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.nueva_semana);
        super.onCreate(savedInstanceState);
        simpleCalendarView = (CalendarView) findViewById(R.id.simpleCalendarView);
        guardar=(Button)findViewById(R.id.guardarsemana);

        db = new DBHelper(this).getWritableDatabase();
        fecc=new SimpleDateFormat("yyyy-MM-dd");

        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                fecha1=year+"-"+(month+1)+"-"+dayOfMonth;
                try {
                    date=fecc.parse(fecha1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fecha2=getFechaFin(date);
                isLunes=getDay(date);
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(NuevaSemana.this);
                View row = inflater.inflate(R.layout.fechas, null);
                final TextView f1 = (TextView) row.findViewById(R.id.finicio);
                final TextView f2 = (TextView) row.findViewById(R.id.ffin);

                f1.setText("De: "+fecha1);
                f2.setText("Hasta: "+fecha2);

                if (fecha1!=null && isLunes.equals("Lunes")) {
                    new AlertDialog.Builder(NuevaSemana.this)
                            .setTitle("Semana Selecionada")
                            .setView(row)
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ContentValues cv = new ContentValues();

                                    cv.put("fechaInicio", fecha1);
                                    cv.put("fechaFin", fecha2);
                                    cv.put("status", 0);
                                    cv.put("idRecurso", 1);
                                    db.insert("Semana", null, cv);

                                    Intent intent = new Intent(NuevaSemana.this, PrincipalActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .show();
                }else{
                    Toast.makeText(NuevaSemana.this, "Selecciona fecha correcta en Lunes.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public String getFechaFin(Date inicio){
        return fecc.format(sumarDias(inicio,4));
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

}
