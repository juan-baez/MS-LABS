/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysolutions.testconexionlims.tendencias;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author mysolutions
 */
public class PuntoMonitoreo {
    private String nombre;
    private ArrayList<Analisis> listaAnalisis;

    public PuntoMonitoreo(String nombre, ArrayList<Analisis> componentes) {
        this.nombre = nombre;
        this.listaAnalisis = componentes;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Analisis> getListaAnalisis() {
        if(listaAnalisis == null) {
            listaAnalisis = new ArrayList();
        }
        return listaAnalisis;
    }

    @Override
    public String toString() {
        String datos = "      Nombre: " + nombre +
                "\n      Lista Análisis: {\n\n";
        
        for(Analisis aux : listaAnalisis) {
            datos += aux.toString();
        }
        
        datos += "      }\n\n";
        
        return datos;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        return Objects.equals(this.nombre, ((PuntoMonitoreo) obj).nombre);
    }
}
