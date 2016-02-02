/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysolutions.testconexionlims.tendencias;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author mysolutions
 */
public class Analisis {
    private Componente componente;
    private Date fechaMonitoreo;
    private String numeroInforme;
    private double valorResultado;
    private String operadorResultado;

    public Analisis(Componente componente, Date fechaMonitoreo,
            String numeroInforme, double valorResultado,
            String operatoriaResultado) {
        this.componente = componente;
        this.fechaMonitoreo = fechaMonitoreo;
        this.numeroInforme = numeroInforme;
        this.valorResultado = valorResultado;
        this.operadorResultado = operatoriaResultado;
    }

    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    public Date getFechaMonitoreo() {
        return fechaMonitoreo;
    }

    public void setFechaMonitoreo(Date fechaMonitoreo) {
        this.fechaMonitoreo = fechaMonitoreo;
    }

    public String getNumeroInforme() {
        return numeroInforme;
    }

    public void setNumeroInforme(String numeroInforme) {
        this.numeroInforme = numeroInforme;
    }

    public double getValorResultado() {
        return valorResultado;
    }

    public void setValorResultado(double valorResultado) {
        this.valorResultado = valorResultado;
    }

    public String getOperadorResultado() {
        return operadorResultado;
    }

    public void setOperadorResultado(String operatoriaResultado) {
        this.operadorResultado = operatoriaResultado;
    }

    @Override
    public String toString() {
        return "        Nombre componente: " + componente.getNombre() +
                "\n        Fecha de Monitoreo: " + fechaMonitoreo +
                "\n        Numero de Informe: " + numeroInforme +
                "\n        Resultado: " + operadorResultado + valorResultado +
                "\n\n";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.componente);
        hash = 73 * hash + Objects.hashCode(this.fechaMonitoreo);
        hash = 73 * hash + Objects.hashCode(this.numeroInforme);
        hash = 73 * hash + (int) (Double.doubleToLongBits(this.valorResultado) ^ (Double.doubleToLongBits(this.valorResultado) >>> 32));
        hash = 73 * hash + Objects.hashCode(this.operadorResultado);
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
        final Analisis other = (Analisis) obj;
        if (Double.doubleToLongBits(this.valorResultado) != Double.doubleToLongBits(other.valorResultado)) {
            return false;
        }
        if (!Objects.equals(this.numeroInforme, other.numeroInforme)) {
            return false;
        }
        if (!Objects.equals(this.operadorResultado, other.operadorResultado)) {
            return false;
        }
        if (!Objects.equals(this.componente, other.componente)) {
            return false;
        }
        return Objects.equals(this.fechaMonitoreo, other.fechaMonitoreo);
    }
}
