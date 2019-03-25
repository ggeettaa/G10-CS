package spaceInvaders;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComprobacionesTest {

    String nombre = "pepito";
    int intentos = 0;

    @Test
    public void compruebaNumeroIntentos(){
        Comprobaciones comprobaciones = new Comprobaciones(nombre,intentos);
        Assert.assertEquals(1,comprobaciones.nuevoIntentoNombre());
        Assert.assertEquals(2,comprobaciones.nuevoIntentoNombre());
        Assert.assertEquals(3,comprobaciones.nuevoIntentoNombre());
        Assert.assertTrue(comprobaciones.excedidoNumeroIntentos());
    }


    @Test
    public void comprobarNombreSinNumero(){
        Comprobaciones comprobaciones = new Comprobaciones(nombre,intentos);
        Assert.assertTrue(comprobaciones.esNombreCorrecto());
    }

}