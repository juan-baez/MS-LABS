/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysolutions.testconexionlims.datos;

import com.aa.toolbox.jar.locator.PropertiesLocator;
import com.mysolutions.testconexionlims.tendencias.Analisis;
import com.mysolutions.testconexionlims.tendencias.Componente;
import com.mysolutions.testconexionlims.tendencias.Comuna;
import com.mysolutions.testconexionlims.tendencias.PuntoMonitoreo;
import com.mysolutions.testconexionlims.tendencias.Servicio;
import com.mysolutions.testconexionlims.busquedaresultados.BusquedaResultados;
import com.mysolutions.testconexionlims.busquedaresultados.BusquedaResultadosExcel;
import com.mysolutions.testconexionlims.EnumComponentesInvalidos;
import com.mysolutions.testconexionlims.busquedaresultados.ListaBusquedaResultados;
import com.mysolutions.testconexionlims.busquedaresultados.ListaBusquedaResultadosExcel;
import com.mysolutions.testconexionlims.tendencias.GrupoAnalisis;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author mysolutions
 */
public class Conexion {

    private Connection conexion;
    private Statement sentencia;

    public Conexion() {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void conectar() throws SQLException {
        String config = "config.properties";
        conexion = DriverManager.getConnection("jdbc:oracle:thin:@//" +
                PropertiesLocator.getProperty(config, "serverLocation") +
                ":" + PropertiesLocator.getProperty(config, "port") + "/" +
                PropertiesLocator.getProperty(config, "database"),
                PropertiesLocator.getProperty(config, "dbLimsUser"),
                PropertiesLocator.getProperty(config, "dbLimsPass"));
        sentencia = conexion.createStatement();
    }

    public ListaBusquedaResultados queryBusquedaResultados(
            Date fechaMonitoreoDesde, Date fechaMonitoreoHasta,
            Date fechaRecepcionDesde, Date fechaRecepcionHasta,
            Date fechaEmisionDesde, Date fechaEmisionHasta,
            String programaMonitoreo, String idPuntoMonitoreo,
            String grupoAnalisis, String fueraNorma, String cliente) throws SQLException {
        conectar();

        ListaBusquedaResultados lista = new ListaBusquedaResultados();

        String query = "SELECT DISTINCT TRIM(t.id_numeric) "
                + "\"Número Informe\", "
                + "SUBSTR(t.description, INSTR(t.description,';') + 1) "
                + "\"Descripción de la muestra\", "
                + "TO_CHAR(t.sampled_date,'dd-mm-yyyy hh24:mi') "
                + "\"Fecha de monitoreo\", "
                + "TO_CHAR(t.recd_date,'dd-mm-yyyy hh24:mi') "
                + "\"Fecha de recepción\", "
                + "TO_CHAR(t.fecha_emision,'dd-mm-yyyy hh24:mi') "
                + "\"Fecha de emisión de informe\", "
                + "SUBSTR(t.description,1,instr(t.description,';') - 1) "
                + "\"Punto de monitoreo\", "
                + "t.id_fuente \"Cadena de custodia\", "
                + "p.phrase_text \"Tipo de agua\", "
                + "tsh.description \"Grupo de análisis\", "
                + "t.record_id \"Programa\" "
                + "FROM lims.test_sched_header tsh "
                + "JOIN lims.SAMPLE t ON (t.test_schedule = tsh.IDENTITY) "
                + "JOIN lims.phrase p ON (t.tipo_elemento = p.phrase_id) "
                + "JOIN lims.TEST c ON (t.id_numeric = c.SAMPLE) "
                + "JOIN lims.result r ON (c.test_number = r.test_number) "
                + "JOIN lims.versioned_component vc ON "
                + "(c.analysis = vc.analysis AND r.NAME = vc.NAME) "
                + "JOIN lims.versioned_analysis va ON "
                + "(vc.analysis = va.IDENTITY AND "
                + "vc.analysis_version = va.analysis_version)"
                + "WHERE t.sampled_date BETWEEN TO_DATE('"
                + fechaMonitoreoDesde.toString()
                + "','yyyy-mm-dd') AND TO_DATE('"
                + fechaMonitoreoHasta.toString()
                + " 23:59:59','yyyy-mm-dd hh24:mi:ss') AND "
                + "t.recd_date BETWEEN TO_DATE('"
                + fechaRecepcionDesde.toString()
                + "','yyyy-mm-dd') AND TO_DATE('"
                + fechaRecepcionHasta.toString()
                + " 23:59:59','yyyy-mm-dd hh24:mi:ss') AND "
                + "t.fecha_emision BETWEEN TO_DATE('"
                + fechaEmisionDesde.toString()
                + "','yyyy-mm-dd') AND TO_DATE('"
                + fechaEmisionHasta.toString()
                + " 23:59:59','yyyy-mm-dd hh24:mi:ss') AND "
                + "t.record_id = '" + programaMonitoreo + "' AND "
                + "SUBSTR(t.description,1,instr(t.description,';')-1) = '"
                + idPuntoMonitoreo + "' AND "
                + "tsh.description = '" + grupoAnalisis + "' AND "
                + "r.out_of_range = '" + fueraNorma + "' AND "
                + "t.customer_id = '" + cliente + "' "
                + "ORDER by t.id_numeric";

        ResultSet resultado = sentencia.executeQuery(query);

        while (resultado.next()) {
            String numeroInforme = resultado.getString("Número Informe");
            String descripcionMuestra =
                    resultado.getString("Descripción de la muestra");
            String fechaMonitoreo = resultado.getString("Fecha de monitoreo");
            String fechaRecepcion = resultado.getString("Fecha de recepción");
            String fechaEmisionInforme =
                    resultado.getString("Fecha de emisión de informe");
            String monitoreo = resultado.getString("Punto de monitoreo");
            String cadenaCustodia = resultado.getString("Cadena de custodia");
            String tipoAgua = resultado.getString("Tipo de agua");
            String analisis = resultado.getString("Grupo de análisis");
            String programa = resultado.getString("Programa");

            BusquedaResultados br = new BusquedaResultados(numeroInforme,
                    descripcionMuestra, fechaMonitoreo, fechaRecepcion,
                    fechaEmisionInforme, monitoreo, cadenaCustodia, tipoAgua,
                    analisis, programa);

            lista.agregar(br);
        }

        return lista;
    }

    public ListaBusquedaResultadosExcel queryBusquedaResultadosExcel(
            Date fechaMonitoreoDesde, Date fechaMonitoreoHasta,
            Date fechaRecepcionDesde, Date fechaRecepcionHasta,
            Date fechaEmisionDesde, Date fechaEmisionHasta,
            String programaMonitoreo, String idPuntoMonitoreo,
            String grupoAnalisis, String fueraNorma, String cliente) throws SQLException {
        conectar();

        ListaBusquedaResultadosExcel lista = new ListaBusquedaResultadosExcel();

        String query = "SELECT DISTINCT TRIM(t.id_numeric) \"Número Informe\", "
                + "SUBSTR(t.description, INSTR(t.description,';') + 1) "
                + "\"Descripción de la muestra\", "
                + "TO_CHAR(t.sampled_date,'dd-mm-yyyy hh24:mi') "
                + "\"Fecha de monitoreo\", "
                + "TO_CHAR(t.recd_date,'dd-mm-yyyy hh24:mi') "
                + "\"Fecha de recepción\", "
                + "TO_CHAR(t.fecha_emision,'dd-mm-yyyy hh24:mi') "
                + "\"Fecha de emisión de informe\", "
                + "SUBSTR(t.description,1,instr(t.description,';') - 1) "
                + "\"Punto de monitoreo\", "
                + "t.id_fuente \"Cadena de custodia\", "
                + "p.phrase_text \"Tipo de agua\", "
                + "tsh.description \"Grupo de análisis\", "
                + "t.record_id \"Programa\", "
                + "TO_CHAR(c.fecha_inicio_ensayo,'dd-mm-yyyy hh24:mi') "
                + "\"Fecha de análisis\", "
                + "CASE WHEN r.name LIKE 'Media%' "
                + "THEN SUBSTR(r.name,7,LENGTH(r.name)) ELSE r.name END "
                + "\"Componente\", "
                + "r.Text \"Resultado\", "
                + "r.units \"Unidad\", "
                + "decode(r.out_of_range,'F', 'No','T','Si',r.out_of_range) "
                + "\"Fuera de norma\", "
                + "vc.limite_informe \"Límite detección\" "
                + "FROM lims.test_sched_header tsh "
                + "JOIN lims.SAMPLE t ON (t.test_schedule = tsh.IDENTITY) " 
                + "JOIN lims.phrase p ON (t.tipo_elemento = p.phrase_id) " 
                + "JOIN lims.TEST c ON (t.id_numeric = c.SAMPLE) " 
                + "JOIN lims.result r ON (c.test_number = r.test_number) " 
                + "JOIN lims.versioned_component vc ON "
                + "(c.analysis = vc.analysis AND r.NAME = vc.NAME) " 
                + "JOIN lims.versioned_analysis va ON "
                + "(vc.analysis = va.IDENTITY AND "
                + "vc.analysis_version = va.analysis_version)"
                + "lims.versioned_analysis va "
                + "WHERE t.sampled_date BETWEEN TO_DATE('"
                + fechaMonitoreoDesde.toString()
                + "','yyyy-mm-dd') AND TO_DATE('"
                + fechaMonitoreoHasta.toString()
                + " 23:59:59','yyyy-mm-dd hh24:mi:ss') AND "
                + "t.recd_date BETWEEN TO_DATE('"
                + fechaRecepcionDesde.toString()
                + "','yyyy-mm-dd') AND TO_DATE('"
                + fechaRecepcionHasta.toString()
                + " 23:59:59','yyyy-mm-dd hh24:mi:ss') AND "
                + "t.fecha_emision BETWEEN TO_DATE('"
                + fechaEmisionDesde.toString()
                + "','yyyy-mm-dd') AND TO_DATE('"
                + fechaEmisionHasta.toString()
                + " 23:59:59','yyyy-mm-dd hh24:mi:ss') AND "
                + "t.record_id = '" + programaMonitoreo + "' AND "
                + "SUBSTR(t.description,1,instr(t.description,';')-1) = '"
                + idPuntoMonitoreo + "' AND "
                + "tsh.description = '" + grupoAnalisis + "' AND "
                + "r.out_of_range = '" + fueraNorma + "' AND "
                + "t.customer_id = '" + cliente + "' "
                + "ORDER by t.id_numeric";

        ResultSet resultado = sentencia.executeQuery(query);

        while (resultado.next()) {
            String numeroInforme = resultado.getString("Número Informe");
            String descripcionMuestra =
                    resultado.getString("Descripción de la muestra");
            String fechaMonitoreo = resultado.getString("Fecha de monitoreo");
            String fechaRecepcion = resultado.getString("Fecha de recepción");
            String fechaEmisionInforme =
                    resultado.getString("Fecha de emisión de informe");
            String monitoreo = resultado.getString("Punto de monitoreo");
            String cadenaCustodia = resultado.getString("Cadena de custodia");
            String tipoAgua = resultado.getString("Tipo de agua");
            String analisis = resultado.getString("Grupo de análisis");
            String programa = resultado.getString("Programa");
            String fechaAnalisis = resultado.getString("Fecha de análisis");
            String componente = resultado.getString("Componente");
            String result = resultado.getString("Resultado");
            String unidad = resultado.getString("Unidad");
            String fueraNormaAtr = resultado.getString("Fuera de norma");
            String limiteDeteccion = resultado.getString("Límite detección");

            BusquedaResultadosExcel br = new BusquedaResultadosExcel(
                    fechaAnalisis, componente, result, unidad, fueraNormaAtr,
                    limiteDeteccion, numeroInforme, descripcionMuestra,
                    fechaMonitoreo, fechaRecepcion, fechaEmisionInforme,
                    monitoreo, cadenaCustodia, tipoAgua, analisis, programa);

            lista.agregar(br);
        }

        return lista;
    }

    public String queryTendenciaZona(Date fechaMonitoreoDesde,
            Date fechaMonitoreoHasta, String cliente) throws SQLException,
            ParseException {
        conectar();

        String datos = "";

        String query = "SELECT DISTINCT se.descripcion \"Servicio\", "
                + "c.descripcion \"Comuna\", "
                + "NVL((SUBSTR(s.description,1,INSTR(s.description,';')-1)), "
                + "s.description) \"Punto de monitoreo\", "
                + "TRIM(s.id_numeric) \"Número informe\", "
                + "TO_CHAR(s.sampled_date,'dd-mm-yyyy hh24:mi') "
                + "\"Fecha de monitoreo\", "
                + "CASE WHEN r.name LIKE 'Media%' "
                + "THEN SUBSTR(r.name,7,LENGTH(r.name)) "
                + "ELSE r.name END \"Componente\", "
                + "TRIM(r.text) \"Resultado\" "
                + "FROM lims.SAMPLE s "
                + "JOIN lims.servicio se ON (s.servicio = se.IDENTITY) "
                + "JOIN lims.comuna c ON (s.comuna = c.IDENTITY) " 
                + "JOIN lims.TEST t ON (s.id_numeric = t.SAMPLE) "
                + "JOIN lims.result r ON (t.test_number = r.test_number)"
                + "WHERE s.customer_id = '" + cliente + "' AND "
                + "s.sampled_date BETWEEN TO_DATE('"
                + fechaMonitoreoDesde.toString()
                + "','yyyy-mm-dd') AND TO_DATE('"
                + fechaMonitoreoHasta.toString()
                + " 23:59:59','yyyy-mm-dd hh24:mi:ss') AND " +
                "r.text <> ' ' AND r.text IS NOT NULL ";

        String clausulaIn = "";
        String clausulaLike = "";

        for (EnumComponentesInvalidos aux : EnumComponentesInvalidos.values()) {
            if (aux.getDescripcion().contains("%")) {
                clausulaLike += "AND UPPER(r.name) NOT LIKE '"
                        + aux.getDescripcion() + "' ";
            } else {
                if (clausulaIn.isEmpty()) {
                    clausulaIn = "AND r.name NOT IN (";
                } else {
                    clausulaIn += ", ";
                }
                clausulaIn += "'" + aux.getDescripcion() + "'";
            }
        }

        if (!clausulaIn.isEmpty()) {
            clausulaIn += ") ";
            query += clausulaIn;
        }
        if (!clausulaLike.isEmpty()) {
            query += clausulaLike;
        }

        query += "ORDER BY \"Servicio\", \"Comuna\", \"Punto de monitoreo\", "
                + "\"Número informe\", \"Componente\"";

        ResultSet resultado = sentencia.executeQuery(query);
        
        ArrayList<Servicio> servicios = new ArrayList();
        
        while (resultado.next()) {
            String servicio = resultado.getString("Servicio");
            String comuna = resultado.getString("Comuna");
            String puntoMonitoreo = resultado.getString("Punto de monitoreo");
            String numeroInforme = resultado.getString("Número informe");
            String fechaMonitoreo = resultado.getString("Fecha de monitoreo");
            String componente = resultado.getString("Componente");
            String result = resultado.getString("Resultado");
            
            double numeroResultado;
            String operatoriaResultado = "";

            try {
                numeroResultado = Double.valueOf(result);
            } 
            catch (Exception ex) {
                operatoriaResultado += result.charAt(0);
                numeroResultado = Double.valueOf(result.substring(1));
            }

            DateFormat formato = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            
            Servicio servicioActual = new Servicio(servicio, null);
            Comuna comunaActual = new Comuna(comuna, null);
            PuntoMonitoreo puntoMonitoreoActual = new PuntoMonitoreo(
                    puntoMonitoreo, null);
            Analisis analisisAgregar = new Analisis(new Componente(componente),
                    formato.parse(fechaMonitoreo), numeroInforme,
                    numeroResultado, operatoriaResultado);
            
            agregarAnalisisZona(servicios, servicioActual, comunaActual,
                    puntoMonitoreoActual, analisisAgregar);
        }
        
        if(!servicios.isEmpty()) {
            datos = "Servicios {\n\n";

            for(Servicio aux : servicios) {
                datos += aux.toString();
            }

            datos += "}\n";
        }
        
        return datos;
    }
        
    private void agregarAnalisisZona(ArrayList<Servicio> servicios,
            Servicio servicioActual, Comuna comunaActual,
            PuntoMonitoreo puntoMonitoreoActual, Analisis agregar) {
        boolean exito = false;
        if (servicios.isEmpty() || !servicios.contains(servicioActual)) { 
            servicios.add(servicioActual);
        }
        for (Servicio servicio : servicios) {
            if (servicio.getNombre().equalsIgnoreCase(
                    servicioActual.getNombre())) {
                int indiceServicio = servicios.indexOf(servicio);
                if(servicios.get(indiceServicio).getComunas().isEmpty() ||
                        !servicios.get(indiceServicio).getComunas().contains(
                                comunaActual)) {
                    servicios.get(indiceServicio).getComunas().add(
                            comunaActual);
                }
                for (Comuna comuna :
                        servicios.get(indiceServicio).getComunas()) {
                    if (comuna.getNombre().equalsIgnoreCase(
                            comunaActual.getNombre())) {
                        int indiceComuna = servicios.get(indiceServicio)
                                .getComunas().indexOf(comuna);
                        if(servicios.get(indiceServicio).getComunas()
                                .get(indiceComuna).getPuntosMonitoreo()
                                .isEmpty() || !servicios.get(indiceServicio)
                                        .getComunas().get(indiceComuna)
                                        .getPuntosMonitoreo()
                                        .contains(puntoMonitoreoActual)) {
                            servicios.get(indiceServicio).getComunas()
                                    .get(indiceComuna).getPuntosMonitoreo()
                                    .add(puntoMonitoreoActual);
                        }
                        for(PuntoMonitoreo punto : servicios.get(indiceServicio)
                                .getComunas().get(indiceComuna)
                                .getPuntosMonitoreo()) {
                            if(punto.getNombre().equalsIgnoreCase(
                                    puntoMonitoreoActual.getNombre())) {
                                punto.getListaAnalisis().add(agregar);
                                exito = true;
                                break;
                            }
                        }
                    }
                    if(exito) {
                        break;
                    }
                }
            }
            if(exito) {
                break;
            }
        }
    }

    public String queryTendenciaGrupoAnalisis(
            Date fechaMonitoreoDesde, Date fechaMonitoreoHasta, String cliente)
            throws SQLException, ParseException {
        conectar();

        String datos = "";

        String query = "SELECT DISTINCT tsh.description \"Grupo de análisis\", "
                + "TRIM(s.id_numeric) \"Número de informe\", "
                + "NVL((SUBSTR(s.description,1,INSTR(s.description,';')-1)), "
                + "s.description) \"Punto de monitoreo\", "
                + "TO_CHAR(s.sampled_date,'dd-mm-yyyy hh24:mi') "
                + "\"Fecha de monitoreo\", "
                + "CASE WHEN r.name LIKE 'Media%' THEN "
                + "SUBSTR(r.name,7,LENGTH(r.name)) "
                + "ELSE r.name END \"Componente\", "
                + "TRIM(r.text) \"Resultado\" "
                + "FROM lims.test_sched_header tsh "
                + "JOIN lims.SAMPLE s ON (s.test_schedule = tsh.IDENTITY) "
                + "JOIN lims.TEST t ON (s.id_numeric = t.SAMPLE) "
                + "JOIN lims.result r ON (t.test_number = r.test_number) "
                + "WHERE s.customer_id = '" + cliente + "' AND "
                + "s.sampled_date BETWEEN TO_DATE('"
                + fechaMonitoreoDesde.toString()
                + "','yyyy-mm-dd') AND TO_DATE('"
                + fechaMonitoreoHasta.toString()
                + " 23:59:59','yyyy-mm-dd hh24:mi:ss') AND "
                + "r.text <> ' ' AND r.text IS NOT NULL ";

        String clausulaIn = "";
        String clausulaLike = "";

        for (EnumComponentesInvalidos aux : EnumComponentesInvalidos.values()) {
            if (aux.getDescripcion().contains("%")) {
                clausulaLike += "AND UPPER(r.name) NOT LIKE '"
                        + aux.getDescripcion() + "' ";
            } else {
                if (clausulaIn.isEmpty()) {
                    clausulaIn = "AND r.name NOT IN (";
                } else {
                    clausulaIn += ", ";
                }
                clausulaIn += "'" + aux.getDescripcion() + "'";
            }
        }

        if (!clausulaIn.isEmpty()) {
            clausulaIn += ") ";
            query += clausulaIn;
        }
        if (!clausulaLike.isEmpty()) {
            query += clausulaLike;
        }

        query += "ORDER BY \"Grupo de análisis\", \"Punto de monitoreo\", "
                + "\"Número de informe\", \"Componente\"";

        ResultSet resultado = sentencia.executeQuery(query);
        
        ArrayList<GrupoAnalisis> grupos = new ArrayList();
        
        while (resultado.next()) {
            String analisis = resultado.getString("Grupo de análisis");
            String numeroInforme = resultado.getString("Número de informe");
            String puntoMonitoreo = resultado.getString("Punto de monitoreo");
            String fechaMonitoreo = resultado.getString("Fecha de monitoreo");
            String componente = resultado.getString("Componente");
            String result = resultado.getString("Resultado");

            double numeroResultado;
            String operatoriaResultado = "";

            try {
                numeroResultado = Double.valueOf(result);
            } 
            catch (Exception ex) {
                try {
                operatoriaResultado += result.charAt(0);
                numeroResultado = Double.valueOf(result.substring(1));
                }
                catch(Exception ex2) {
                    continue;
                }
            }

            DateFormat formato = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            
            GrupoAnalisis grupoAnalisisActual = new GrupoAnalisis(analisis,
                    null);
            PuntoMonitoreo puntoMonitoreoActual = new PuntoMonitoreo(
                    puntoMonitoreo, null);
            Analisis analisisAgregar = new Analisis(new Componente(componente),
                    formato.parse(fechaMonitoreo), numeroInforme,
                    numeroResultado, operatoriaResultado);
            
            agregarAnalisisGrupo(grupos, grupoAnalisisActual,
                    puntoMonitoreoActual, analisisAgregar);
        }
        
        if(!grupos.isEmpty()) {
            datos = "Grupos de Análisis {\n\n";

            for(GrupoAnalisis aux : grupos) {
                datos += aux.toString();
            }

            datos += "}\n";
        }

        return datos;
    }
    
    private void agregarAnalisisGrupo(ArrayList<GrupoAnalisis> grupos,
            GrupoAnalisis grupoAnalisisActual,
            PuntoMonitoreo puntoMonitoreoActual, Analisis agregar) {
        boolean exito = false;
        if (grupos.isEmpty() || !grupos.contains(grupoAnalisisActual)) { 
            grupos.add(grupoAnalisisActual);
        }
        for (GrupoAnalisis grupo : grupos) {
            if (grupo.getDescripcion().equalsIgnoreCase(
                    grupoAnalisisActual.getDescripcion())) {
                int indiceGrupo = grupos.indexOf(grupo);
                if(grupos.get(indiceGrupo).getPuntosMonitoreo().isEmpty() ||
                        !grupos.get(indiceGrupo).getPuntosMonitoreo().contains(
                                puntoMonitoreoActual)) {
                    grupos.get(indiceGrupo).getPuntosMonitoreo().add(
                            puntoMonitoreoActual);
                }
                for (PuntoMonitoreo punto :
                        grupos.get(indiceGrupo).getPuntosMonitoreo()) {
                    if (punto.getNombre().equalsIgnoreCase(
                            puntoMonitoreoActual.getNombre())) {
                        punto.getListaAnalisis().add(agregar);
                        exito = true;
                        break;
                    }
                }
            }
            if(exito) {
                break;
            }
        }
    }
}
