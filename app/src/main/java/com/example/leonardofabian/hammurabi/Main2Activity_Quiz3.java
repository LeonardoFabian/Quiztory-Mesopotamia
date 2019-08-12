package com.example.leonardofabian.hammurabi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity_Quiz3 extends AppCompatActivity {

    private TextView tvJugador, tvScore, tvTitle;
    String string_jugador, string_score, string_vidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2__quiz3);

        tvJugador = (TextView)findViewById(R.id.tv_jugador);
        tvScore = (TextView)findViewById(R.id.tv_score);
        tvTitle = (TextView)findViewById(R.id.tv_titulo);

        tvTitle.setText(R.string.title_puntuacion);
        string_jugador = getIntent().getStringExtra("jugador");
        tvJugador.setText(string_jugador);
        string_score = getIntent().getStringExtra("score");
        tvScore.setText(string_score);
    }
}
