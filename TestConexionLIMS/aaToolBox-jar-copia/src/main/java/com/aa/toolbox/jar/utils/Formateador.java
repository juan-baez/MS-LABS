/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aa.toolbox.jar.utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Conamerica01
 */
public class Formateador implements Serializable {

    private static final long serialVersionUID = -6242437333528018564L;

    private static Formateador instancia;

    private Formateador() {
        super();
    }

    public static Formateador getInstancia() {
        if (instancia == null) {
            instancia = new Formateador();
        }
        return instancia;
    }

    public String obtenerFechaConFormato(Date fechaPorFormatear) {
        DateFormat formateadorFecha = DateFormat.getInstance();
        return formateadorFecha.format(fechaPorFormatear);
    }

    public String obtenerFechaConFormato2(Date fechaformatear) {
        String fechaFormato;
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, new Locale("es", "VE"));
        if (fechaformatear == null) {
            fechaFormato = df.format(new Date());
        } else {
            fechaFormato = df.format(fechaformatear);
        }
        return fechaFormato;
    }

    /**
     *
     * @param fechaObjetivo es la fecha que queremos transformar a un formato
     * facil de leer
     * @return String que contiene la fecha ya formateada en formato (FULL)
     * ejemplo: "lunes 6 de agosto de 2012"
     */
    public String formatearFechaEstiloCompleto(Date fechaObjetivo) {
        DateFormat formatoFechaFull =  DateFormat.getDateInstance(DateFormat.FULL, new Locale("es", "VE"));
        return formatoFechaFull.format(fechaObjetivo);
    }

    /**
     *
     * @param fechaObjetivo es la fecha que queremos transformar a un formato
     * facil de leer
     * @return String que contiene la fecha ya formateada en formato (SHORT)
     * ejemplo: "06/08/12"
     */
    public String formatearFechaEstiloCorto(Date fechaObjetivo) {
        DateFormat formatoFechaShort = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("es", "VE"));
        return formatoFechaShort.format(fechaObjetivo);
    }

    /**
     *
     * @param fechaObjetivo es la fecha que queremos transformar a un formato
     * facil de leer
     * @return String que contiene la fecha ya formateada en formato (MEDIUM)
     * ejemplo: "06/08/2012"
     */
    public String formatearFechaEstiloMedio(Date fechaObjetivo) {
        DateFormat formatoFechaMedium = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("es", "VE"));
        return formatoFechaMedium.format(fechaObjetivo);
    }

    /**
     *
     * @param fechaObjetivo es la fecha que queremos transformar a un formato
     * facil de leer
     * @return String que contiene la fecha ya formateada en formato (LONG)
     * ejemplo: "6 de agosto de 2012"
     */
    public String formatearFechaEstiloLargo(Date fechaObjetivo) {
        DateFormat formatoFechaLong = DateFormat.getDateInstance(DateFormat.LONG, new Locale("es", "VE"));
        return formatoFechaLong.format(fechaObjetivo);
    }

    /**
     * Este metodo permite eliminar caracteres con tilde de una cadena de caracteres, sin importar si la cadena esta
     * en mayusculas o minusculas
     * @param input - cadena que se quiere modificar
     * @return cadena sin tildes
     */
    public String removeCharacter(String input) {
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        try {
            for (int i = 0; i < original.length(); i++) {
                output = output.replace(original.charAt(i), ascii.charAt(i));
            }
        } catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
        }
        return output;
    }
    
    /**
     * Este método se encarga de darle formato a un valor tipo Double, de tal forma que 
     * el valor entrante sufra la siguiente transformación: valorEntrante = 9000,99923141
     * valoRetorno = 9.000,99
     * 
     * @param valorParaFormatear, valor que se desea formatear
     * @return valorRetorno, valor ya formateado
     */
    public String formatearDecimalDouble(Double valorParaFormatear) {
        return String.format("%,.2f", valorParaFormatear);
    }
    
    /**
     * Este método se encarga de darle formato a un valor tipo Double para que haga uso 
     * de los separadores de miles según a notación venezolana, de tal forma que el valor entrante sufra la siguiente 
     * transformación: valorEntrante = 9000,99923141
     * valoRetorno = 9.000
     * 
     * @param valorParaFormatear, valor que se desea formatear
     * @param localidadParaFormato, ejemplo: new Locale("es", "VE") para Venezuela
     * @return valorRetorno, valor ya formateado
     */
    public String formatearEnteroMiles(Double valorParaFormatear, Locale localidadParaFormato) {
        return NumberFormat.getNumberInstance(localidadParaFormato).format(valorParaFormatear.intValue());
    }
    
    // Este metodo recibe una fecha incluyendo su hora y devuleve solamente la hora.
    public String formatearHora(Date fecha)
    {
        //Formateando la fecha:
        DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        
        return formatoHora.format(fecha);
    }

}
