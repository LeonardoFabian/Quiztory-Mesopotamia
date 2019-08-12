package com.example.leonardofabian.hammurabi;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity_Quiz3 extends AppCompatActivity {

    private TextView tvJugador, tvScore, tvTitle, tvCorrectas, tvIncorrectas;
    String string_jugador, string_score, string_vidas, string_correctas, string_incorrectas;
    private MediaPlayer mp;
    private Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2__quiz3);

        tvJugador = (TextView) findViewById(R.id.tv_jugador);
        tvScore = (TextView) findViewById(R.id.tv_score);
        tvTitle = (TextView) findViewById(R.id.tv_titulo);
        tvCorrectas = (TextView)findViewById(R.id.tv_correctas);
        tvIncorrectas = (TextView)findViewById(R.id.tv_incorrectas);

        tvTitle.setText(R.string.title_puntuacion);
        string_jugador = getIntent().getStringExtra("jugador");
        tvJugador.setText(string_jugador);
        string_score = getIntent().getStringExtra("score");
        tvScore.setText(string_score);
        string_correctas = getIntent().getStringExtra("correctas");
        tvCorrectas.setText("Correctas: " + string_correctas);
        string_incorrectas = getIntent().getStringExtra("incorrectas");
        tvCorrectas.setText("Incorrectas: " + string_incorrectas);

        //objeto media player
        mp = MediaPlayer.create(this, R.raw.goats); //pista
        mp.start();//iniciar
        mp.setLooping(true);//repetir pista

        btnHome = (Button)findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Main2Activity_Quiz3.this, MainActivity.class);
                startActivity(intent);
                finish();
                mp.stop();
                mp.release();
            }
        });
    }
}
