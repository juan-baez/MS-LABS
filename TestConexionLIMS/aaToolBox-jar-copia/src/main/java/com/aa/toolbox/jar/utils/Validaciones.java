/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aa.toolbox.jar.utils;

import java.io.Serializable;

/**
 *
 * @author Pastor
 */
public class Validaciones implements Serializable {

    /**
     * Este método se encarga de validar que un rut ingresado tenga la
     * estructura establecida por la legislación chilena (18560575-k)
     *
     * @param rut
     * @return
     */
    public static boolean validateRut(String rut) {
        int splitedRut;
        char validatorDigit;
        if (rut.length() > 0) {
            // Creamos un arreglo con el rut y el digito verificador
            String[] rut_vd = rut.split("-");
            // Las partes del rut (numero y dv) deben tener una longitud positiva
            if (rut_vd.length == 2) {
                // Capturamos error (al convertir un string a entero)
                try {
                    splitedRut = Integer.parseInt(rut_vd[0]);
                    validatorDigit = rut_vd[1].charAt(0);
                    // Validamos que sea un rut valido segÃºn la norma
                    int m = 0;
                    int s = 1;
                    for (; splitedRut != 0; splitedRut /= 10) {
                        s = (s + splitedRut % 10 * (9 - m++ % 6)) % 11;
                    }
                    return validatorDigit == (char) (s != 0 ? s + 47 : 75);
                } catch (Exception ex) {
                    System.out.println(" Error " + ex.getMessage());
                }
            }
        }
        return false;
    }
}
