/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aa.toolbox.jar.enums;

/**
 *
 * @author pastorduran
 */
public enum FileExtensions {
    XLS(".xls"), 
    CSV(".csv");
    
    private final String nombre;
    
    FileExtensions(String nombre){
        this.nombre = nombre;
    }

    public String getNombre() {
        return this.nombre;
    }    
    
}
