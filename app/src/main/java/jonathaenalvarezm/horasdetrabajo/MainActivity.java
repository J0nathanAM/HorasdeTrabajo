package jonathaenalvarezm.horasdetrabajo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.Principal;

public class MainActivity extends AppCompatActivity {

    private EditText nombre, paterno,materno,email;
    private Button btnGuardar;
    private SQLiteDatabase db;
    private ContentValues cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre=(EditText)findViewById(R.id.nombrepersona);
        paterno=(EditText)findViewById(R.id.paterno);
        materno=(EditText)findViewById(R.id.materno);
        email=(EditText)findViewById(R.id.email);
        btnGuardar=(Button)findViewById(R.id.btnguardar);

        MyPreference myPreference=new MyPreference(MainActivity.this);

        if (!myPreference.isFirstTime()){
            Intent intent=new Intent(getApplicationContext(),PrincipalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }else {
            db = new DBHelper(this).getWritableDatabase();

            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name= null,pat=null,mat=null,mail=null;
                    name=nombre.getText().toString().trim();
                    pat=paterno.getText().toString().trim();
                    mat=materno.getText().toString().trim();
                    mail=email.getText().toString().trim();
                    if(!name.isEmpty() && !pat.isEmpty() && !mat.isEmpty() && !mail.isEmpty()) {
                        cv = new ContentValues();
                        cv.put("nombre",name);
                        cv.put("apPaterno",pat);
                        cv.put("apMaterno",mat);
                        cv.put("email",mail);
                        db.insert("Recurso", "nombre", cv);

                        Intent intent = new Intent(MainActivity.this, NuevaSemana.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(MainActivity.this, "Favor de llenar todos los campos, gracias.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
