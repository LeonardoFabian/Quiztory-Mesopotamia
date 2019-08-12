package com.example.leonardofabian.hammurabi;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity_Quiz1 extends AppCompatActivity {

    int int_score,  int_vidas = 3;
    String string_jugador, string_score, string_vidas;
    private TextView tv_nombre, tv_score;
    private ImageView iv_vidas;
    private TextView tv_question;
    private MediaPlayer mp, mp_great, mp_bad;
    private int correct_answer;
    private int current_question;
    private int correctas = 0;
    private int incorrectas = 0;
    private String[] all_questions;
    private boolean[] answers_correct;
    private RadioGroup group;
    private Button btn_check, btn_skip;

    private int ids_answers[] = {
            R.id.answer1, R.id.answer2, R.id.answer3
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2__quiz1);

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

        // REFERENCIAS
        tv_nombre = (TextView)findViewById(R.id.player);
        tv_score = (TextView)findViewById(R.id.score);
        iv_vidas = (ImageView)findViewById(R.id.vidas);
        tv_question = (TextView)findViewById(R.id.text_question);
        group = (RadioGroup)findViewById(R.id.respuestas);
        btn_check = (Button)findViewById(R.id.btn_check);

        string_jugador = getIntent().getStringExtra("jugador");
        tv_nombre.setText(string_jugador);

        all_questions = getResources().getStringArray(R.array.all_questions);
        answers_correct = new boolean[all_questions.length];
        current_question = 0;
        showQuestion();

        //numero respuesta correcta desde string
        //final int correct_answer1 = getResources().getStringArray(R.integer.correct_answer1);

        //final Intent intent = new Intent(this, Main2Activity_Quiz2.class);

        // TODO: Me quede aqui, configurando que paa si es la ultima pregunta video youtube 49:00

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

                answers_correct[current_question] = (respuesta == correct_answer);

                if(respuesta == correct_answer){
                    mp_great.start();
                    int_score++;
                    String str_score = String.valueOf(int_score);
                    tv_score.setText(str_score);
                    //Cada ves que tiene una respuesta correcta incrementa la base de datos
                    BaseDeDatos();
                    Toast.makeText(Main2Activity_Quiz1.this, R.string.correct, Toast.LENGTH_SHORT).show();

                    /*
                    String string_score = String.valueOf(int_score);
                    String string_vidas = String.valueOf(int_vidas);

                    intent.putExtra("jugador", string_jugador);
                    intent.putExtra("score", string_score);
                    intent.putExtra("vidas", string_vidas);

                    startActivity(intent);
                    finish();
                    mp.stop();
                    mp.release();
                    */
                }else {
                    mp_bad.start();
                    int_vidas--;
                    BaseDeDatos();
                    Toast.makeText(Main2Activity_Quiz1.this, R.string.incorrect, Toast.LENGTH_SHORT).show();

                    switch(int_vidas){
                        case 3:
                            iv_vidas.setImageResource(R.drawable.tresvidas);
                            break;
                        case 2:
                            Toast.makeText(Main2Activity_Quiz1.this, R.string.dos_vidas, Toast.LENGTH_SHORT).show();
                            iv_vidas.setImageResource(R.drawable.dosvidas);
                            break;
                        case 1:
                            Toast.makeText(Main2Activity_Quiz1.this, R.string.una_vida, Toast.LENGTH_SHORT).show();
                            iv_vidas.setImageResource(R.drawable.unavida);
                            break;
                        case 0:
                            Toast.makeText(Main2Activity_Quiz1.this, R.string.cero_vidas, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Main2Activity_Quiz1.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            mp.stop();
                            mp.release();
                            break;
                    }
                }

                // TODO: Hacer que al finalizar muestre una tabla de calificaciones
                if(current_question < all_questions.length - 1){
                    current_question++;
                    showQuestion();
                } else {

                    for (boolean b : answers_correct){
                        if (b) correctas++;
                        else incorrectas++;
                    }
                    /*
                    String resultado = String.format("Correctas: %d -- Incorrectas: %d", correctas, incorrectas);
*/
                    String string_score = String.valueOf(int_score);
                    String string_vidas = String.valueOf(int_vidas);
                    String string_correctas = String.valueOf(correctas);
                    String string_incorrectas = String.valueOf(incorrectas);

                    Intent intent = new Intent(Main2Activity_Quiz1.this, Main2Activity_Quiz3.class); //pasar al proximo activity

                    intent.putExtra("jugador", string_jugador);
                    intent.putExtra("score", string_score);
                    intent.putExtra("vidas", string_vidas);
                    intent.putExtra("correctas", string_correctas);
                    intent.putExtra("incorrectas", string_incorrectas);

                    startActivity(intent);
                    finish();

                    /*
                    Toast.makeText(Main2Activity_Quiz1.this, resultado, Toast.LENGTH_LONG).show();
                    finish();
                    */
                }
                /*
                for(int i = 0; i < answers_correct.length; i++){
                    Log.i("Caso", String.format("Respuesta $d: %b", i, answers_correct[i]));
                }*/
            }
        });

        btn_skip = (Button)findViewById(R.id.btn_skip);
        btn_skip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(current_question < all_questions.length - 1){
                    showQuestion();
                }
                /*mp.stop();
                mp.release();
                intent.putExtra("jugador", tv_nombre.getText().toString());
                startActivity(intent);
                finish();
                */
            }
        });
    }

    private void showQuestion() {

        // START PREGUNTA Y RESPUESTA
        String question = all_questions[current_question];
        String[] part_question = question.split(";");

        group.clearCheck();

        //question.setText(R.string.question_content1);
        tv_question.setText(part_question[0]);

        //String[] answers = getResources().getStringArray(R.array.answers_question1);

        //mostrando las respuestas
        for(int i = 0; i < ids_answers.length; i++){
            RadioButton rb = (RadioButton)findViewById(ids_answers[i]);
            String answer = part_question[i+1];
            if(answer.charAt(0) == '*'){
                correct_answer = i;
                answer = answer.substring(1);
            }
            //rb.setText(answers[i]);
            rb.setText(answer);
        }
        if(current_question == all_questions.length - 1){
            btn_skip.setVisibility(View.GONE);
        }
        /*else {
            btn_skip.setVisibility(View.VISIBLE);
        }*/

        if(current_question == all_questions.length - 1){
            btn_check.setText(R.string.btn_finish_attempt);
        } else {
            btn_check.setText(R.string.btn_check);
        }
        // END PREGUNTA Y RESPUESTA
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
                modificacion.put("score", int_score);

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
