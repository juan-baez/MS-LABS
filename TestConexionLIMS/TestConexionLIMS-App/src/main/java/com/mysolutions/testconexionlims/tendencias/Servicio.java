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
public class Servicio {
    private String nombre;
    private ArrayList<Comuna> comunas;

    public Servicio(String nombre, ArrayList<Comuna> comunas) {
        this.nombre = nombre;
        this.comunas = comunas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Comuna> getComunas() {
        if(comunas == null) {
            comunas = new ArrayList();
        }
        return comunas;
    }

    @Override
    public String toString() {
        String datos = "  Nombre: " + nombre + "\n  Comunas: {\n\n";
        
        for(Comuna aux : comunas) {
            datos += aux.toString();
        }
        
        datos += "  }\n\n";
        
        return datos;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.nombre);
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
        
        return Objects.equals(this.nombre, ((Servicio) obj).nombre);
    }
}
