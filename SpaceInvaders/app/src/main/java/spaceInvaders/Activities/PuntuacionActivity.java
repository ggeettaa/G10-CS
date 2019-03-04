package spaceInvaders.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spaceInvaders.android.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

public class PuntuacionActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO_REQUEST = 13;

    private TextView mensajeFin;
    private TextView puntuaciones;
    private TextView punt500;
    private Button reinicio;
    private int puntos;
    private String nombre;
    private String myPhotoPath;
    private ImageView myPhoto;
    private ImageView myPhoto2;
    private ImageView myPhoto3;
    private ImageView myPhoto4;
    private ImageView myPhoto5;
    private ImageView myPhoto6;
    private ImageView myPhoto7;
    private ImageView myPhoto8;
    private ImageView myPhoto9;
    private ImageView myPhoto10;
    private ImageView myPhoto11;;

    MediaPlayer musica;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fin_del_juego);
        nombre = getIntent().getExtras().getString("nombre");
        puntos = Integer.parseInt(getIntent().getExtras().getString("puntos"));
        puntuaciones = (TextView) findViewById(R.id.puntuaciones);
        mensajeFin = (TextView) findViewById(R.id.puntuacion);
        punt500 = (TextView)findViewById(R.id.textPunt500);
        reinicio= (Button)findViewById(R.id.reinicio);
        mensajeFin.setText(Integer.toString(puntos));
        myPhoto = (ImageView) findViewById(R.id.myPhoto);
        myPhoto2 = (ImageView) findViewById(R.id.myPhoto2);
        myPhoto3 = (ImageView) findViewById(R.id.myPhoto3);
        myPhoto4 = (ImageView) findViewById(R.id.myPhoto4);
        myPhoto5 = (ImageView) findViewById(R.id.myPhoto5);
        myPhoto6 = (ImageView) findViewById(R.id.myPhoto6);
        myPhoto7 = (ImageView) findViewById(R.id.myPhoto7);
        myPhoto8 = (ImageView) findViewById(R.id.myPhoto8);
        myPhoto9 = (ImageView) findViewById(R.id.myPhoto9);
        myPhoto10 = (ImageView) findViewById(R.id.myPhoto10);
        myPhoto11 = (ImageView) findViewById(R.id.myPhoto11);

        takePhoto();


    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {

            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.spaceInvaders.android.fileProvider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, TAKE_PHOTO_REQUEST);
            }
        }
    }

    private File createImageFile() throws  IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        myPhotoPath = image.getAbsolutePath();
        return image;

    }

    public void reiniciar(View view){
        while (true) {
                try {
                    if ((getIntent().getExtras().getString("tipoJuego")).equals("mayorRebotes")) {
                        musica.stop();
                        Intent mayorRebotes = new Intent(this, MayorActivityRebotes.class);
                        mayorRebotes.putExtra("nombre", nombre);
                        startActivity(mayorRebotes);
                        finish();
                        break;
                    } else if ((getIntent().getExtras().getString("tipoJuego")).equals("mayor")) {
                        musica.stop();
                        Intent mayor = new Intent(this, MayorActivity.class);
                        mayor.putExtra("nombre", nombre);
                        startActivity(mayor);
                        finish();
                        break;
                    } else {
                        musica.stop();
                        Intent menor = new Intent(this, MenorActivity.class);
                        menor.putExtra("nombre", nombre);
                        startActivity(menor);
                        finish();
                        break;
                    }
                } catch (Exception e) {
                }
        }
    }

    public void salir(View view){
        finish();
        System.exit(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == TAKE_PHOTO_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                int targetW = myPhoto.getWidth();
                int targetH = myPhoto.getHeight();

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(myPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                int scaleFactor = Math.min(photoH/110, photoW/110);

                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;

                Bitmap bitmap = BitmapFactory.decodeFile(myPhotoPath, bmOptions);

                myPhoto.setImageBitmap(bitmap);

                SharedPreferences preferencias = getSharedPreferences("Ranking", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                //editor.clear(); //reinicia el ranking cada partida
                if (puntos < 10) {
                    editor.putString("0000" + " - " + nombre  +"<" +myPhotoPath, "");
                } else if (puntos < 100) {
                    editor.putString("00" + String.valueOf(puntos) + " - " + nombre  +"<" +myPhotoPath, "");

                } else if (puntos < 1000){
                    editor.putString("0" + String.valueOf(puntos) + " - " + nombre+  "<" +myPhotoPath, "");

                } else {
                    editor.putString(String.valueOf(puntos) + " - " + nombre+"<" +myPhotoPath, "");
                }
                editor.apply();

                if (puntos<500){
                    reinicio.setVisibility(View.INVISIBLE);
                }

                Iterator it = preferencias.getAll().keySet().iterator();
                ArrayList ord = new ArrayList();
                while (it.hasNext()){
                    String res = (String)it.next();
                    ord.add(res);
                }
                Collections.sort(ord, new Comparator<String>() {
                    public int compare(String o1, String o2) {
                        return extractInt(o2)- extractInt(o1);
                    }

                    int extractInt(String s) {
                        String [] splitted = new String[2];
                        splitted=s.split("<");
                        String num = splitted[0].replaceAll("\\D", "");
                        return num.isEmpty() ? 0 : Integer.parseInt(num);
                    }
                });
                String s = "";
                int cont = 0;
                for (Object i : ord) {
                    String nuevo = String.valueOf(i);
                    String [] splitted = new String[2];
                    splitted=nuevo.split("<");
                    if (splitted[0].equals(String.valueOf(puntos) + " - " + nombre)) {
                        s += ("-->  " + splitted[0] + "\n\n");
                    } else {
                        s += ("      " + splitted[0] + "\n\n");
                    }
                    BitmapFactory.Options bmOptions2 = new BitmapFactory.Options();
                    bmOptions2.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(splitted[1], bmOptions2);
                    int photoW2 = bmOptions2.outWidth;
                    int photoH2 = bmOptions2.outHeight;
                    int scaleFactor2 = Math.min(photoH2/30, photoW2/30);
                    bmOptions2.inJustDecodeBounds = false;
                    bmOptions2.inSampleSize = scaleFactor2;
                    Bitmap bitmap2 = BitmapFactory.decodeFile(splitted[1], bmOptions2);

                    if(cont==0) {
                        myPhoto2.setImageBitmap(bitmap2);
                    }else if(cont==1) {
                        myPhoto3.setImageBitmap(bitmap2);
                    }else if(cont==2) {
                        myPhoto4.setImageBitmap(bitmap2);
                    }else if(cont==3) {
                        myPhoto5.setImageBitmap(bitmap2);
                    }else if(cont==4) {
                        myPhoto6.setImageBitmap(bitmap2);
                    }else if(cont==5) {
                        myPhoto7.setImageBitmap(bitmap2);
                    }else if(cont==6) {
                        myPhoto8.setImageBitmap(bitmap2);
                    }else if(cont==7) {
                        myPhoto9.setImageBitmap(bitmap2);
                    }else if(cont==8) {
                        myPhoto10.setImageBitmap(bitmap2);
                    }else if(cont==9) {
                        myPhoto11.setImageBitmap(bitmap2);
                    }
                    cont++;
                    if (cont > 9) {
                        break;
                    }
                }
                puntuaciones.setText(s);

                musica = MediaPlayer.create(this, R.raw.supermariobros3);
                musica.start();
                musica.setLooping(true);

            }
        }
    }

}
