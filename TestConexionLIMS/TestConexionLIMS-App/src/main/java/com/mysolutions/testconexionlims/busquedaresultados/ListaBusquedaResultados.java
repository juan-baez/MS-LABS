/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysolutions.testconexionlims.busquedaresultados;

import java.util.ArrayList;

/**
 * Clase que contiene una lista con los datos de la Búsqueda de Resultados.
 * 
 * Esta clase se creó solo con fines de mostrar los datos de una forma mas
 * ordenada y sencilla.
 * 
 * @author Juan Báez
 * @version 1.0
 */
public class ListaBusquedaResultados {
    // Lista de Busqueda de Resultados.
    private ArrayList<BusquedaResultados> registro;
    
    /**
     * Constructor de la clase ListaBusquedaResultados.
     * 
     * Instancia la lista para empezar a utilizarla
     */
    public ListaBusquedaResultados() {
        this.registro = new ArrayList();
    }
    
    /**
     * Método que permite agregar una nueva búsqueda de resultados a la lista.
     * 
     * @param nuevo Elemento a agregar.
     */
    public void agregar(BusquedaResultados nuevo) {
        registro.add(nuevo);
    }
    
    /**
     * Obtiene la cantidad de elementos que contiene la lista.
     * 
     * @return Cantidad de elementos en la lista.
     */
    public int obtenerCantidad() {
        return registro.size();
    }
    
    /**
     * Método sobreescrito que muestra los datos ingresados de la lista.
     * 
     * Recorre la lista, extrae los datos de cada elemento, los concatena y
     * retorna.
     * 
     * @return Datos que contiene la lista.
     */
    @Override
    public String toString() {
        String datos = "";
        
        for(BusquedaResultados aux : registro) {
            datos += aux.toString() + '\n';
        }
        
        return datos;
    }
}
