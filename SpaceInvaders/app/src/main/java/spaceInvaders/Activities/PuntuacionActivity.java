package spaceInvaders.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.spaceInvaders.android.R;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PuntuacionActivity extends Activity {
    public static final int TAKE_PHOTO_REQUEST = 13;
    private static final String NOMBRE_STRING = "nombre";
    private TextView puntuaciones;
    private Button reinicio;
    private int puntos;
    private String nombre;
    private String stringDisplayRanking = "";
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
    private ImageView myPhoto11;
    MediaPlayer musica;
    
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        TextView mensajeFin;
        setContentView(R.layout.activity_fin_del_juego);
        nombre = getIntent().getExtras().getString(NOMBRE_STRING);
        puntos = Integer.parseInt(getIntent().getExtras().getString("puntos"));
        puntuaciones = findViewById(R.id.puntuaciones);
        mensajeFin = findViewById(R.id.puntuacion);
        reinicio= findViewById(R.id.reinicio);
        mensajeFin.setText(Integer.toString(puntos));
        myPhoto = findViewById(R.id.myPhoto);
        myPhoto2 = findViewById(R.id.myPhoto2);
        myPhoto3 = findViewById(R.id.myPhoto3);
        myPhoto4 = findViewById(R.id.myPhoto4);
        myPhoto5 = findViewById(R.id.myPhoto5);
        myPhoto6 = findViewById(R.id.myPhoto6);
        myPhoto7 = findViewById(R.id.myPhoto7);
        myPhoto8 = findViewById(R.id.myPhoto8);
        myPhoto9 = findViewById(R.id.myPhoto9);
        myPhoto10 = findViewById(R.id.myPhoto10);
        myPhoto11 = findViewById(R.id.myPhoto11);
        takePhoto();
    }

    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.e("takephoto","Some error with the photo");
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
        boolean finished =false;
        while (!finished) {
                try {
                    if ((getIntent().getExtras().getString("tipoJuego")).equals("mayorRebotes")) {
                        finished = activarReinicioMayorRebotes();
                    } else if ((getIntent().getExtras().getString("tipoJuego")).equals("mayor")) {
                        finished = activarReinicioMayor();
                    } else {
                        finished = activarReinicioMenor();
                    }
                } catch (Exception e) {
                    Log.e("reiniciar","Some error with the restart");
                }
        }
    }

    public boolean activarReinicioMayorRebotes() {
            musica.stop();
            Intent mayorRebotes = new Intent(this, MayorActivityRebotes.class);
            mayorRebotes.putExtra(NOMBRE_STRING, nombre);
            startActivity(mayorRebotes);
            finish();
            return true;
    }

    public boolean activarReinicioMayor() {
        musica.stop();
        Intent mayor = new Intent(this, MayorActivity.class);
        mayor.putExtra(NOMBRE_STRING, nombre);
        startActivity(mayor);
        finish();
        return true;
    }

    public boolean activarReinicioMenor() {
        musica.stop();
        Intent menor = new Intent(this, MenorActivity.class);
        menor.putExtra(NOMBRE_STRING, nombre);
        startActivity(menor);
        finish();
        return true;
    }

    public void salir(View view){
        finish();
        System.exit(0);
    }

    public void disableReplaying(){
        if (puntos<500){
            reinicio.setVisibility(View.INVISIBLE);
        }
    }

    public SharedPreferences.Editor cadenaNombreRanking(SharedPreferences.Editor editor){
        if (puntos < 10) {
            editor.putString("0000" + " - " + nombre  +"<" +myPhotoPath, "");
        } else if (puntos < 100) {
            editor.putString(String.valueOf(puntos) + " - " + nombre  +"<" +myPhotoPath, "");

        } else if (puntos < 1000){
            editor.putString(String.valueOf(puntos) + " - " + nombre+  "<" +myPhotoPath, "");

        } else {
            editor.putString(String.valueOf(puntos) + " - " + nombre+"<" +myPhotoPath, "");
        }
        return editor;
    }

    public ArrayList addPreferencesToList(SharedPreferences preferencias){
        Iterator it = preferencias.getAll().keySet().iterator();
        ArrayList ord = new ArrayList();
        while (it.hasNext()){
            String res = (String)it.next();
            ord.add(res);
        }
        return ord;
    }
    public void setMusicAtEnd(){
        musica = MediaPlayer.create(this, R.raw.supermariobros3);
        musica.start();
        musica.setLooping(true);
    }

    public void stringConfig(List ord){
        Collections.sort(ord, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return extractInt(o2)- extractInt(o1);
            }

            int extractInt(String s) {
                String [] splitted;
                splitted=s.split("<");
                String num = splitted[0].replaceAll("\\D", "");
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });
    }

    public Bitmap bitmapConfigurePhotoRanking(String [] splitted){
        BitmapFactory.Options bmOptions2 = new BitmapFactory.Options();
        bmOptions2.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(splitted[1], bmOptions2);
        int photoW2 = bmOptions2.outWidth;
        int photoH2 = bmOptions2.outHeight;
        int scaleFactor2 = Math.min(photoH2/30, photoW2/30);
        bmOptions2.inJustDecodeBounds = false;
        bmOptions2.inSampleSize = scaleFactor2;
        return BitmapFactory.decodeFile(splitted[1], bmOptions2);
    }

    public String [] stringBuildRanking(Object obj){
        StringBuilder bld = new StringBuilder(stringDisplayRanking);
        String nuevo = String.valueOf(obj);
        String [] splitted;
        splitted=nuevo.split("<");
        if (splitted[0].equals(String.valueOf(puntos) + " - " + nombre)) {
            bld.append("-->  " + splitted[0] + "\n\n");
        } else {
            bld.append("      " + splitted[0] + "\n\n");
        }
        stringDisplayRanking = bld.toString();
        return splitted;
    }

    public void bitmapOptionsConfiguration(){
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
    }

  public void settingImages(ArrayList ord){
      int cont = 0;
      for (Object obj : ord) {
          Bitmap bitmap2 = bitmapConfigurePhotoRanking(stringBuildRanking(obj));
          switch(cont) {
              case 0:
                  myPhoto2.setImageBitmap(bitmap2);
                  break;
              case 1:
                  myPhoto3.setImageBitmap(bitmap2);
                  break;
              case 2:
                  myPhoto4.setImageBitmap(bitmap2);
                  break;
              case 3:
                  myPhoto5.setImageBitmap(bitmap2);
                  break;
              case 4:
                  myPhoto6.setImageBitmap(bitmap2);
                  break;
              case 5:
                  myPhoto7.setImageBitmap(bitmap2);
                  break;
              case 6:
                  myPhoto8.setImageBitmap(bitmap2);
                  break;
              case 7:
                  myPhoto9.setImageBitmap(bitmap2);
                  break;
              case 8:
                  myPhoto10.setImageBitmap(bitmap2);
                  break;
              case 9:
                  myPhoto11.setImageBitmap(bitmap2);
                  break;
              default:
                  break;
          }
          cont++;
          if (cont > 9) {
              break;
          }
      }
  }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
                bitmapOptionsConfiguration();
                SharedPreferences preferencias = getSharedPreferences("Ranking", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                cadenaNombreRanking(editor);
                editor.apply();
                disableReplaying();
                ArrayList ord = addPreferencesToList(preferencias);
                stringConfig(ord);
                settingImages(ord);
                //System.out.println(stringDisplayRanking);
                puntuaciones.setText(stringDisplayRanking);
                setMusicAtEnd();
            }
        }

    }
