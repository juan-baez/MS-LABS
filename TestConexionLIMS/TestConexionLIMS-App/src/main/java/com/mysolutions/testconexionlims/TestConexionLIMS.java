/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysolutions.testconexionlims;

import com.mysolutions.testconexionlims.datos.Conexion;
import com.mysolutions.testconexionlims.busquedaresultados.ListaBusquedaResultados;
import com.mysolutions.testconexionlims.busquedaresultados.ListaBusquedaResultadosExcel;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author mysolutions
 */
public class TestConexionLIMS {
    public static Conexion con = new Conexion();
    public static Scanner scan = new Scanner(System.in);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean ciclo = true;
        int opcion = 0;
        do {
            System.out.println("Ingrese la consulta que se quiere hacer:");
            System.out.println("1. Búsqueda de Resultados");
            System.out.println("2. Búsqueda de Resultados (Excel)");
            System.out.println("3. Tendencias por Zona");
            System.out.println("4. Tendencias por Grupo de Análisis");
            System.out.println("5. Salir");
            System.out.print("Opción: ");
            
            try {
                opcion = scan.nextInt();
            }
            catch(Exception ex) {
                opcion = 0;
            }
            
            scan.nextLine();
            
            switch(opcion) {
                case 1:
                    consultarBusquedaResultados(false);
                    break;
                case 2:
                    consultarBusquedaResultados(true);
                    break;
                case 3:
                    consultarTendencias(true);
                    break;
                case 4:
                    consultarTendencias(false);
                    break;
                case 5:
                    ciclo = false;
                    break;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        }
        while(ciclo);
    }
    
    public static void consultarBusquedaResultados(boolean excel) {
        boolean ciclo = true;
        Date fechaMonitoreoDesde = null;
        Date fechaMonitoreoHasta = null;
        Date fechaRecepcionDesde = null;
        Date fechaRecepcionHasta = null;
        Date fechaEmisionDesde = null;
        Date fechaEmisionHasta = null;
        String programaMonitoreo = null;
        String idPuntoMonitoreo = null;
        String grupoAnalisis = null;
        String fueraNorma = null;
        String cliente = null;
        
        String temp;
        
        do {
            System.out.println("Ingrese la fecha de inicio de monitoreo: (Formato: AAAA-MM-DD)");
            
            temp = scan.nextLine();
            
            String[] verificarFormato = temp.split("-");
            
            if(verificarFormato.length == 3) {
                try {
                    fechaMonitoreoDesde = Date.valueOf(temp);
                    
                    ciclo = false;
                }
                catch(Exception ex) {
                    System.out.println("Fecha invalida");
                }
            }
            else {
                System.out.println("Formato invalido.");
            }
        }
        while(ciclo);
        
        ciclo = true;
        
        do {
            System.out.println("Ingrese la fecha de término de monitoreo: (Formato: AAAA-MM-DD)");
            
            temp = scan.nextLine();
            
            String[] verificarFormato = temp.split("-");
            
            if(verificarFormato.length == 3) {
                try {
                    fechaMonitoreoHasta = Date.valueOf(temp);
                    
                    ciclo = false;
                }
                catch(Exception ex) {
                    System.out.println("Fecha invalida");
                }
            }
            else {
                System.out.println("Formato invalido.");
            }
        }
        while(ciclo);
        
        ciclo = true;
        
        do {
            System.out.println("Ingrese la fecha de inicio de recepción: (Formato: AAAA-MM-DD)");
            
            temp = scan.nextLine();
            
            String[] verificarFormato = temp.split("-");
            
            if(verificarFormato.length == 3) {
                try {
                    fechaRecepcionDesde = Date.valueOf(temp);
                    
                    ciclo = false;
                }
                catch(Exception ex) {
                    System.out.println("Fecha invalida");
                }
            }
            else {
                System.out.println("Formato invalido.");
            }
        }
        while(ciclo);
        
        ciclo = true;
        
        do {
            System.out.println("Ingrese la fecha de término de recepción: (Formato: AAAA-MM-DD)");
            
            temp = scan.nextLine();
            
            String[] verificarFormato = temp.split("-");
            
            if(verificarFormato.length == 3) {
                try {
                    fechaRecepcionHasta = Date.valueOf(temp);
                    
                    ciclo = false;
                }
                catch(Exception ex) {
                    System.out.println("Fecha invalida");
                }
            }
            else {
                System.out.println("Formato invalido.");
            }
        }
        while(ciclo);
        
        ciclo = true;
        
        do {
            System.out.println("Ingrese la fecha de inicio de emisión de informe: (Formato: AAAA-MM-DD)");
            
            temp = scan.nextLine();
            
            String[] verificarFormato = temp.split("-");
            
            if(verificarFormato.length == 3) {
                try {
                fechaEmisionDesde = Date.valueOf(temp);
                    
                    ciclo = false;
                }
                catch(Exception ex) {
                    System.out.println("Fecha invalida");
                }
            }
            else {
                System.out.println("Formato invalido.");
            }
        }
        while(ciclo);
        
        ciclo = true;
        
        do {
            System.out.println("Ingrese la fecha de término de emisión de informe: (Formato: AAAA-MM-DD)");
            
            temp = scan.nextLine();
            
            String[] verificarFormato = temp.split("-");
            
            if(verificarFormato.length == 3) {
                try {
                    fechaEmisionHasta = Date.valueOf(temp);
                    
                    ciclo = false;
                }
                catch(Exception ex) {
                    System.out.println("Fecha invalida");
                }
            }
            else {
                System.out.println("Formato invalido.");
            }
        }
        while(ciclo);
        
        ciclo = true;
        
        do {
            System.out.println("Ingrese programa de monitoreo: ");
            
            temp = scan.nextLine();
            
            if(!temp.isEmpty()) {
                programaMonitoreo = temp;
                ciclo = false;
            }
        }
        while(ciclo);
        
        ciclo = true;
        
        do {
            System.out.println("Ingrese ID de punto de monitoreo: ");
            
            temp = scan.nextLine();
            
            if(!temp.isEmpty()) {
                idPuntoMonitoreo = temp;
                ciclo = false;
            }
        }
        while(ciclo);
        
        ciclo = true;
        
        do {
            System.out.println("Ingrese grupo de análisis: ");
            
            temp = scan.nextLine();
            
            if(!temp.isEmpty()) {
                grupoAnalisis = temp;
                ciclo = false;
            }
        }
        while(ciclo);
        
        ciclo = true;
        
        do {
            System.out.println("Indicar si está fuera de norma: (S/N)");
            
            temp = scan.nextLine().toUpperCase();
            
            if(!temp.isEmpty()) {
                switch(temp) {
                    case "S":
                    case "SI":
                        fueraNorma = "T";
                        ciclo = false;
                        break;
                    case "N":
                    case "NO":
                        fueraNorma = "F";
                        ciclo = false;
                        break;
                    default:
                        System.out.println("Opción inválida");
                        break;
                }
            }
        }
        while(ciclo);
        
        ciclo = true;
        
        do {
            System.out.println("Ingrese cliente: ");
            
            temp = scan.nextLine();
            
            if(!temp.isEmpty()) {
                cliente = temp;
                ciclo = false;
            }
        }
        while(ciclo);
        
        try {
            if(!excel) {
                ListaBusquedaResultados lista = con.queryBusquedaResultados(
                        fechaMonitoreoDesde, fechaMonitoreoHasta,
                        fechaRecepcionDesde, fechaRecepcionHasta,
                        fechaEmisionDesde, fechaEmisionHasta, programaMonitoreo,
                        idPuntoMonitoreo, grupoAnalisis, fueraNorma, cliente);

                if(lista.obtenerCantidad() > 0) {
                    System.out.println(lista.toString());
                }
                else {
                    System.out.println("No se han encontrado resultados.");
                }
            }
            else {
                ListaBusquedaResultadosExcel lista =
                        con.queryBusquedaResultadosExcel(fechaMonitoreoDesde,
                                fechaMonitoreoHasta, fechaRecepcionDesde,
                                fechaRecepcionHasta, fechaEmisionDesde,
                                fechaEmisionHasta, programaMonitoreo,
                                idPuntoMonitoreo, grupoAnalisis, fueraNorma,
                                cliente);

                if(lista.obtenerCantidad() > 0) {
                    System.out.println(lista.toString());
                }
                else {
                    System.out.println("No se han encontrado resultados.");
                }
            }
            
        } catch (SQLException ex) {
            System.out.println("Ha ocurrido un error: " + ex.toString());
        }
    }
    
    public static void consultarTendencias(boolean porZona) {
        boolean ciclo = true;
        Date fechaMonitoreoDesde = null;
        Date fechaMonitoreoHasta = null;
        String cliente = null;
        
        String temp;
        
        do {
            System.out.println("Ingrese la fecha de inicio de monitoreo: (Formato: AAAA-MM-DD)");
            
            temp = scan.nextLine();
            
            String[] verificarFormato = temp.split("-");
            
            if(verificarFormato.length == 3) {
                try {
                    fechaMonitoreoDesde = Date.valueOf(temp);
                    
                    ciclo = false;
                }
                catch(Exception ex) {
                    System.out.println("Fecha invalida");
                }
            }
            else {
                System.out.println("Formato invalido.");
            }
        }
        while(ciclo);
        
        ciclo = true;
        
        do {
            System.out.println("Ingrese la fecha de término de monitoreo: (Formato: AAAA-MM-DD)");
            
            temp = scan.nextLine();
            
            String[] verificarFormato = temp.split("-");
            
            if(verificarFormato.length == 3) {
                try {
                    fechaMonitoreoHasta = Date.valueOf(temp);
                    
                    ciclo = false;
                }
                catch(Exception ex) {
                    System.out.println("Fecha invalida");
                }
            }
            else {
                System.out.println("Formato invalido.");
            }
        }
        while(ciclo);
        
        ciclo = true;
        
        do {
            System.out.println("Ingrese cliente: ");
            
            temp = scan.nextLine();
            
            if(!temp.isEmpty()) {
                cliente = temp;
                ciclo = false;
            }
        }
        while(ciclo);
        
        try {
            String datos;
            if(porZona) {
                datos = con.queryTendenciaZona(fechaMonitoreoDesde,
                        fechaMonitoreoHasta, cliente);
            }
            else {
                datos = con.queryTendenciaGrupoAnalisis(fechaMonitoreoDesde,
                        fechaMonitoreoHasta, cliente);
            }
            if(!datos.isEmpty()) {
                System.out.println(datos);
            }
            else {
                System.out.println("No se han encontrado resultados.");
            }
        }
        catch(Exception ex) {
            System.out.println("Ha ocurrido un error: " + ex.toString());
        }
    }
}
