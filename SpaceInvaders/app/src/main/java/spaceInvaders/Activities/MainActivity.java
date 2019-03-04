package spaceInvaders.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.spaceInvaders.android.R;

public class MainActivity extends AppCompatActivity {

    private EditText edad;
    private EditText nombre;
    private TextView error;

    private MediaPlayer musica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edad = (EditText) findViewById(R.id.anyos);
        nombre = (EditText) findViewById(R.id.nombre);
        error = (TextView) findViewById(R.id.error);

        musica = MediaPlayer.create(this, R.raw.avengersv1);
        musica.start();
        musica.setLooping(true);
    }

    @SuppressLint("SetTextI18n")
    public void preguntarEdad(View view) {
        try {
            String anyos_s = edad.getText().toString();
            int anyos_int = Integer.parseInt(anyos_s);
            String name = nombre.getText().toString();
            if(name.equals("")){
                error.setText("Nombre no válido");
            } else {
                if (anyos_int <= 13) {
                    musica.stop();
                    Intent menor = new Intent(this, MenorActivity.class);
                    menor.putExtra("nombre", name);
                    startActivity(menor);
                    finish();
                } else if (((Switch)findViewById(R.id.REBOTES)).isChecked()) {
                    musica.stop();
                    Intent mayorRebotes = new Intent(this, MayorActivityRebotes.class);
                    mayorRebotes.putExtra("nombre", name);
                    startActivity(mayorRebotes);
                    finish();
                } else {
                    musica.stop();
                    Intent mayor = new Intent(this, MayorActivity.class);
                    mayor.putExtra("nombre", name);
                    startActivity(mayor);
                    finish();
                }
            }
        } catch (Exception e) {
            error.setText("Edad no válida");
        }
    }

}
