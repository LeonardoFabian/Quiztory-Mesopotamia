package com.example.leonardofabian.hammurabi;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity_Login extends AppCompatActivity {

    private EditText et_nombre;
    private TextView tv_bestScore;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2__login);

        et_nombre = (EditText)findViewById(R.id.txt_nombre);
        tv_bestScore = (TextView)findViewById(R.id.textView_record);

        //icono action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //conexion a Base de Datos
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        // Consultar puntaje en Base de Datos para luego mostrar en textview record
        Cursor consulta = BD.rawQuery(
                "select * from puntaje where score = (select max(score) from puntaje)", null
        );

        //Si encontro datos mostrar mejor puntaje
        if(consulta.moveToFirst()){
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);
            tv_bestScore.setText("Record: " + temp_score + " " + temp_nombre);
            BD.close();
        } else {
            BD.close();
        }

        //objeto media player
        mp = MediaPlayer.create(this, R.raw.goats); //pista
        mp.start();//iniciar
        mp.setLooping(true);//repetir pista
    }

    public void Jugar(View view){
        String nombre = et_nombre.getText().toString();

        // validar que el nombre ha sido escrito
        if(!nombre.equals("")){
            mp.stop();
            mp.release();

            Intent intent = new Intent(this, Main2Activity_Quiz1.class); //pasar al proximo activity

            // Pasar el nombre de usuario
            intent.putExtra("jugador", nombre);

            startActivity(intent); //iniciar proximo activity
            finish();//finalizar este activity
        } else {
            Toast.makeText(this, "Debes escribir tu nombre", Toast.LENGTH_SHORT).show();

            //metodo para abrir teclado si no ha escrito nombre
            et_nombre.requestFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_nombre, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onBackPressed(){

    }
}
