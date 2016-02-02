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
public class Comuna {
    private String nombre;
    private ArrayList<PuntoMonitoreo> puntosMonitoreo;

    public Comuna(String nombre, ArrayList<PuntoMonitoreo> puntosMonitoreoParam) {
        this.nombre = nombre;
        puntosMonitoreo = puntosMonitoreoParam;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<PuntoMonitoreo> getPuntosMonitoreo() {
        if (puntosMonitoreo == null) {
            puntosMonitoreo = new ArrayList();
        }
        return puntosMonitoreo;
    }

    @Override
    public String toString() {
        String datos = "    Nombre: " + nombre +
                "\n    Puntos de Monitoreo: {\n\n";
        
        for(PuntoMonitoreo aux : puntosMonitoreo) {
            datos += aux.toString();
        }
        
        datos += "    }\n\n";
        
        return datos;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.nombre);
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
        
        return Objects.equals(this.nombre, ((Comuna) obj).nombre);
    }
}
