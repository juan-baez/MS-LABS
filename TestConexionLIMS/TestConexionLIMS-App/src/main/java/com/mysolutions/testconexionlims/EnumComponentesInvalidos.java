/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mysolutions.testconexionlims;

/**
 * Enumeración que contiene las descripciones de los componentes que no se
 * pueden graficar.
 * 
 * Estas por lo general contienen fechas o solo palabras en sus resultados, por
 * lo que no se pueden generar un gráfico con esos resultados..
 * 
 * @author Juan Báez
 * @version 1.0
 */
public enum EnumComponentesInvalidos {
    /* Enumerados de los componentes que no se pueden graficar. Si se necesita
    agregar otro componente, se debe de hacer aqui. */
    FECHAENSAYO("Fecha de ensayo"), HOLDINGTIME("Holding Time"), OLOR("OLOR"),
    SABOR("Sabor"), QUITA("QUITA%"),
    METALTOTALDISUELTO("Metal Total<Metal Disuelto"),
    FECHATERMINODBO("Fecha termino DBO"), REQUIERESUBLATOR("Requiere Sublator");
    
    // Descripción del componente
    private final String descripcion;
    
    /**
     * Constructor privado de la enumeración EnumComponentesInvalidos.
     * 
     * @param descripcion Descripción del componente.
     */
    private EnumComponentesInvalidos(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
