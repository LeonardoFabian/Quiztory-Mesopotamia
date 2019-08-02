package com.example.leonardofabian.hammurabi;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity_Quiz2 extends AppCompatActivity {

    int int_score,  int_vidas = 3;
    String string_jugador, string_score, string_vidas;
    private TextView tv_nombre, tv_score;
    private ImageView iv_vidas;
    private TextView question;
    private MediaPlayer mp, mp_great, mp_bad;

    private int ids_answers[] = {
            R.id.answer1, R.id.answer2, R.id.answer3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2__quiz2);

        //icono action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //objeto media player
        mp = MediaPlayer.create(this, R.raw.goats); //pista
        mp.start();//iniciar
        mp.setLooping(true);//repetir pista

        //Sonidos correcto o incorrecto
        mp_great = MediaPlayer.create(this, R.raw.wonderful);
        mp_bad = MediaPlayer.create(this, R.raw.bad);




        tv_nombre = (TextView)findViewById(R.id.player);
        tv_score = (TextView)findViewById(R.id.score);
        iv_vidas = (ImageView)findViewById(R.id.vidas);

        string_jugador = getIntent().getStringExtra("jugador");
        tv_nombre.setText(string_jugador);
        string_score = getIntent().getStringExtra("score");
        tv_score.setText(string_score);



        question = (TextView)findViewById(R.id.text_question);
        question.setText(R.string.question_content1);

        String[] answers = getResources().getStringArray(R.array.answers_question2);

        //
        for(int i = 0; i < ids_answers.length; i++){
            RadioButton rb = (RadioButton)findViewById(ids_answers[i]);
            rb.setText(answers[i]);
        }

        //numero respuesta correcta desde string
        final int correct_answer1 = getResources().getInteger(R.integer.correct_answer2);
        final RadioGroup group = (RadioGroup)findViewById(R.id.respuestas);
        final Intent intent = new Intent(this, Main2Activity_Quiz3.class);

        Button btn_check = (Button)findViewById(R.id.btn_check);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("la_accion", "btn comprobar");
                //saber cual id de radio esta clickeado
                int id = group.getCheckedRadioButtonId();
                int respuesta = -1;
                //Log.i("la_accion", String.format("Id %d", id)); //mostrar id que esta seleccionado en console log
                for(int i = 0; i < ids_answers.length; i++){
                    if(ids_answers[i] == id){
                        respuesta = i;
                    }
                }
                if(respuesta == correct_answer1){
                    mp_great.start();
                    int_score++;
                    String str_score = String.valueOf(int_score);
                    tv_score.setText(str_score);

                    //Cada ves que tiene una respuesta correcta incrementa la base de datos
                    BaseDeDatos();
                    Toast.makeText(Main2Activity_Quiz2.this, R.string.correct, Toast.LENGTH_SHORT).show();

                    String string_score = String.valueOf(int_score);
                    String string_vidas = String.valueOf(int_vidas);

                    intent.putExtra("jugador", string_jugador);
                    intent.putExtra("score", string_score);
                    intent.putExtra("vidas", string_vidas);

                    startActivity(intent);
                    finish();
                    mp.stop();
                    mp.release();
                }else {
                    mp_bad.start();
                    int_vidas--;
                    BaseDeDatos();

                    switch(int_vidas){
                        case 3:
                            iv_vidas.setImageResource(R.drawable.tresvidas);
                            break;
                        case 2:
                            Toast.makeText(Main2Activity_Quiz2.this, R.string.dos_vidas, Toast.LENGTH_SHORT).show();
                            iv_vidas.setImageResource(R.drawable.dosvidas);
                            break;
                        case 1:
                            Toast.makeText(Main2Activity_Quiz2.this, R.string.una_vida, Toast.LENGTH_SHORT).show();
                            iv_vidas.setImageResource(R.drawable.unavida);
                            break;
                        case 0:
                            Toast.makeText(Main2Activity_Quiz2.this, R.string.cero_vidas, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Main2Activity_Quiz2.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            mp.stop();
                            mp.release();
                            break;
                    }
                    Toast.makeText(Main2Activity_Quiz2.this, R.string.incorrect, Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btn_skip = (Button)findViewById(R.id.btn_skip);
        btn_skip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mp.stop();
                mp.release();
                intent.putExtra("jugador", tv_nombre.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }

    //Insertar en la base de datos
    public void BaseDeDatos(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        //obtener el maximo score de la base de datos
        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max(score) from puntaje)", null);
        if(consulta.moveToFirst()){
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);

            int best_score = Integer.parseInt(temp_score);

            if(int_score > best_score){
                ContentValues modificacion = new ContentValues();
                modificacion.put("nombre", string_jugador);
                modificacion.put("nombre", int_score);

                BD.update("puntaje", modificacion, "score=" + best_score, null);
            }

            BD.close();

        }else{
            ContentValues insertar = new ContentValues();

            insertar.put("nombre", string_jugador);
            insertar.put("score", int_score);

            BD.insert("puntaje", null, insertar);
            BD.close();
        }
    }

    @Override
    public void onBackPressed(){

    }
}
