package jonathaenalvarezm.horasdetrabajo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Jonathan EAM on 03/10/2017.
 */

public class PrincipalActivity extends AppCompatActivity {

    private TextView nombreusu, apellidos, emailusu;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Button btnactual, btnanterior;
    private static final int INSERTAR_ID= Menu.FIRST;
    public static final int SALIR=Menu.FIRST+1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.principal_activity);
        super.onCreate(savedInstanceState);

        nombreusu=(TextView)findViewById(R.id.nombreusu);
        apellidos=(TextView)findViewById(R.id.apellidos);
        emailusu=(TextView)findViewById(R.id.emailusu);
        btnactual=(Button)findViewById(R.id.semanaactual);
        btnanterior=(Button)findViewById(R.id.semanaanterior);

        MyPreference preference=new MyPreference(PrincipalActivity.this);
        if (preference.isFirstTime()){
            preference.setOld(true);
        }

        db = new DBHelper(this).getWritableDatabase();
        query("SELECT nombre, apPaterno, apMaterno, email FROM Recurso ORDER BY idRecurso");

        btnactual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PrincipalActivity.this,SemanaActivity.class);
                intent.putExtra("llave","actual");
                startActivity(intent);
            }
        });

        btnanterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PrincipalActivity.this,SemanaActivity.class);
                intent.putExtra("llave","anterior");
                startActivity(intent);
            }
        });

    }

    public void query(String sql){
        cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        nombreusu.setText(cursor.getString(0));
        apellidos.setText(cursor.getString(1)+" "+cursor.getString(2));
        emailusu.setText(cursor.getString(3));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,INSERTAR_ID,Menu.NONE,"Nueva Semana");
        menu.add(Menu.NONE,SALIR,Menu.NONE,"Salir");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case INSERTAR_ID:
                btnanterior.setEnabled(true);
                Intent intent=new Intent(PrincipalActivity.this,NuevaSemana.class);
                startActivity(intent);
                break;
            case  SALIR:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
        }
        return false;
    }

}
