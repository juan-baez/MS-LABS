/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysolutions.testconexionlims.busquedaresultados;

/**
 * Clase que hereda de BusquedaResultados y contiene los datos de la Búsqueda de
 * Resultados que se mostrarán en un archivo Excel.
 * 
 * Esta clase se creó solo con fines de mostrar los datos de una forma mas
 * ordenada y sencilla.
 * 
 * @author Juan Báez
 * @version 1.0
 */
public class BusquedaResultadosExcel extends BusquedaResultados{
    private String fechaAnalisis, componente, resultado, unidad, fueraNorma, 
            limiteDeteccion;
    
    /**
     * 
     * 
     * @param fechaAnalisis Fecha de analisis
     * @param componente Nombre del componente
     * @param resultado Resultado
     * @param unidad Unidad de medida
     * @param fueraNorma Indica si está fuera de norma ("T" o "F")
     * @param limiteDeteccion Limite de detección
     * @param numeroInforme Número del informe
     * @param descripcionMuestra Descripción de la muestra
     * @param fechaMonitoreo Fecha de monitoreo
     * @param fechaRecepcion Fecha de recepción
     * @param fechaEmisionInforme Fecha de emisión de informe
     * @param puntoMonitoreo Punto de monitoreo
     * @param cadenaCustodia Cadena de custodia
     * @param tipoAgua Tipo de agua
     * @param grupoAnalisis Grupo de análisis
     * @param programa Programa
     */
    public BusquedaResultadosExcel(String fechaAnalisis, String componente,
            String resultado, String unidad, String fueraNorma,
            String limiteDeteccion, String numeroInforme,
            String descripcionMuestra, String fechaMonitoreo,
            String fechaRecepcion, String fechaEmisionInforme,
            String puntoMonitoreo, String cadenaCustodia, String tipoAgua,
            String grupoAnalisis, String programa) {
        super(numeroInforme, descripcionMuestra, fechaMonitoreo, fechaRecepcion,
                fechaEmisionInforme, puntoMonitoreo, cadenaCustodia, tipoAgua,
                grupoAnalisis, programa);
        this.fechaAnalisis = fechaAnalisis;
        this.componente = componente;
        this.resultado = resultado;
        this.unidad = unidad;
        this.fueraNorma = fueraNorma;
        this.limiteDeteccion = limiteDeteccion;
    }

    public String getFechaAnalisis() {
        return fechaAnalisis;
    }

    public void setFechaAnalisis(String fechaAnalisis) {
        this.fechaAnalisis = fechaAnalisis;
    }

    public String getComponente() {
        return componente;
    }

    public void setComponente(String componente) {
        this.componente = componente;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getFueraNorma() {
        return fueraNorma;
    }

    public void setFueraNorma(String fueraNorma) {
        this.fueraNorma = fueraNorma;
    }

    public String getLimiteDeteccion() {
        return limiteDeteccion;
    }

    public void setLimiteDeteccion(String limiteDeteccion) {
        this.limiteDeteccion = limiteDeteccion;
    }

    @Override
    public String toString() {
        return "BusquedaResultadosExcel{" + "numeroInforme=" +
                super.numeroInforme + ", descripcionMuestra=" +
                super.descripcionMuestra + ", fechaMonitoreo=" +
                super.fechaMonitoreo + ", fechaRecepcion=" +
                super.fechaRecepcion + ", fechaEmisionInforme=" +
                super.fechaEmisionInforme + ", puntoMonitoreo=" +
                super.puntoMonitoreo + ", cadenaCustodia=" +
                super.cadenaCustodia + ", tipoAgua=" + super.tipoAgua +
                ", grupoAnalisis=" + super.grupoAnalisis + ", programa=" +
                super.programa + ", fechaAnalisis=" + fechaAnalisis +
                ", componente=" + componente + ", resultado=" + resultado +
                ", unidad=" + unidad + ", fueraNorma=" + fueraNorma +
                ", limiteDeteccion=" + limiteDeteccion + '}';
    }
}
