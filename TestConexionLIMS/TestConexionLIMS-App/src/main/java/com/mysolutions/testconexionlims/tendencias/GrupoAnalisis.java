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
public class GrupoAnalisis {
    private String descripcion;
    private ArrayList<PuntoMonitoreo> puntosMonitoreo;

    public GrupoAnalisis(String descripcion,
            ArrayList<PuntoMonitoreo> puntosMonitoreo) {
        this.descripcion = descripcion;
        this.puntosMonitoreo = puntosMonitoreo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<PuntoMonitoreo> getPuntosMonitoreo() {
        if(puntosMonitoreo == null) {
            puntosMonitoreo = new ArrayList();
        }
        return puntosMonitoreo;
    }

    @Override
    public String toString() {
        String datos = "    Descripción: " + descripcion +
                "\n    Puntos de Monitoreo: {\n\n";
        
        for(PuntoMonitoreo aux : puntosMonitoreo) {
            datos += aux.toString();
        }
        
        datos += "    }\n\n";
        
        return datos;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.descripcion);
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
        
        return Objects.equals(this.descripcion,
                ((GrupoAnalisis) obj).descripcion);
    }
}
