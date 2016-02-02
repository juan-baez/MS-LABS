/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysolutions.testconexionlims.busquedaresultados;

/**
 * Clase que contiene los datos de la Búsqueda de Resultados.
 * 
 * Esta clase se creó solo con fines de mostrar los datos de una forma mas
 * ordenada y sencilla.
 * 
 * @author Juan Báez
 * @version 1.0
 */
public class BusquedaResultados {
    /* Atributos que guardan lo que retorna la consulta de Busqueda de
    Resultados */
    protected String numeroInforme,descripcionMuestra, fechaMonitoreo,
            fechaRecepcion, fechaEmisionInforme, puntoMonitoreo, cadenaCustodia,
            tipoAgua, grupoAnalisis, programa;
    
    /**
     * Constructor de la clase BusquedaResultados.
     * 
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
    public BusquedaResultados(String numeroInforme, String descripcionMuestra,
            String fechaMonitoreo, String fechaRecepcion,
            String fechaEmisionInforme, String puntoMonitoreo,
            String cadenaCustodia, String tipoAgua, String grupoAnalisis,
            String programa) {
        this.numeroInforme = numeroInforme;
        this.descripcionMuestra = descripcionMuestra;
        this.fechaMonitoreo = fechaMonitoreo;
        this.fechaRecepcion = fechaRecepcion;
        this.fechaEmisionInforme = fechaEmisionInforme;
        this.puntoMonitoreo = puntoMonitoreo;
        this.cadenaCustodia = cadenaCustodia;
        this.tipoAgua = tipoAgua;
        this.grupoAnalisis = grupoAnalisis;
        this.programa = programa;
    }
    
    public String getNumeroInforme() {
        return numeroInforme;
    }

    public void setNumeroInforme(String numeroInforme) {
        this.numeroInforme = numeroInforme;
    }

    public String getDescripcionMuestra() {
        return descripcionMuestra;
    }

    public void setDescripcionMuestra(String descripcionMuestra) {
        this.descripcionMuestra = descripcionMuestra;
    }

    public String getFechaMonitoreo() {
        return fechaMonitoreo;
    }

    public void setFechaMonitoreo(String fechaMonitoreo) {
        this.fechaMonitoreo = fechaMonitoreo;
    }

    public String getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(String fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public String getFechaEmisionInforme() {
        return fechaEmisionInforme;
    }

    public void setFechaEmisionInforme(String fechaEmisionInforme) {
        this.fechaEmisionInforme = fechaEmisionInforme;
    }

    public String getPuntoMonitoreo() {
        return puntoMonitoreo;
    }

    public void setPuntoMonitoreo(String puntoMonitoreo) {
        this.puntoMonitoreo = puntoMonitoreo;
    }

    public String getCadenaCustodia() {
        return cadenaCustodia;
    }

    public void setCadenaCustodia(String cadenaCustodia) {
        this.cadenaCustodia = cadenaCustodia;
    }

    public String getTipoAgua() {
        return tipoAgua;
    }

    public void setTipoAgua(String tipoAgua) {
        this.tipoAgua = tipoAgua;
    }

    public String getGrupoAnalisis() {
        return grupoAnalisis;
    }

    public void setGrupoAnalisis(String grupoAnalisis) {
        this.grupoAnalisis = grupoAnalisis;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    @Override
    public String toString() {
        return "BusquedaResultados{" + "numeroInforme=" + numeroInforme +
                ", descripcionMuestra=" + descripcionMuestra +
                ", fechaMonitoreo=" + fechaMonitoreo + ", fechaRecepcion=" +
                fechaRecepcion + ", fechaEmisionInforme=" +
                fechaEmisionInforme + ", puntoMonitoreo=" + puntoMonitoreo +
                ", cadenaCustodia=" + cadenaCustodia + ", tipoAgua=" +
                tipoAgua + ", grupoAnalisis=" + grupoAnalisis + ", programa=" +
                programa + '}';
    }
}
