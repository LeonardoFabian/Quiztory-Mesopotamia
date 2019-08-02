package com.example.leonardofabian.hammurabi;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //musica para el activity
    private MediaPlayer mp;
    private TextView tv_bestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            tv_bestScore.setText("Record: " + temp_nombre + " (" + temp_score + " PUNTOS)");
            BD.close();
        } else {
            BD.close();
        }

        //objeto media player
        mp = MediaPlayer.create(this, R.raw.goats); //pista
        mp.start();//iniciar
        mp.setLooping(true);//repetir pista
    }

    public void Entrar(View view){
        mp.stop();
        mp.release();//destruye el objeto media player y libera recursos

        Intent intent = new Intent(this, Main2Activity_Login.class); //pasar al proximo activity
        startActivity(intent); //iniciar proximo activity
        finish();//finalizar este activity
    }

    @Override
    public void onBackPressed(){

    }

}
