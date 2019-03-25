package spaceInvaders;

public class Comprobaciones {

    private String nombre;
    private int intentoActual = 0;
    private static final int MAX_NOMBRE_INTENTOS = 3;


    public Comprobaciones(String nombre, int intentos){
        this.nombre = nombre;
        this.intentoActual = intentos;
    }

    public int nuevoIntentoNombre(){
        return ++intentoActual;
    }

    public boolean excedidoNumeroIntentos(){
        return intentoActual == MAX_NOMBRE_INTENTOS;
    }

    public boolean esNombreCorrecto(){
        boolean correcto = true;
        for(char c : this.nombre.toCharArray()){
            if(Character.isDigit(c)){
                correcto = false;
            }
        }
        return correcto;
    }




}
