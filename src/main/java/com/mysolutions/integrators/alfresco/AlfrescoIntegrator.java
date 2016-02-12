/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysolutions.integrators.alfresco;

import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.DocumentImpl;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisPermissionDeniedException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisRuntimeException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mysolutions
 */
public class AlfrescoIntegrator {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(AlfrescoIntegrator.class);
    
    // Atributos para la sesión de Alfresco.
    
    private Session sesion; // La sesión misma.
    private SessionFactory factory; // Objeto que crea la sesión.
    
    private Map<String, String> parameter; // Parametros de conexión.
    
    /**
     * Constructor de la clase ConexionAlfresco.
     * 
     * Este genera la conexión entre esta aplicación y el servidor de Alfresco,
     * creando una sesión para que el usuario pueda hacer otras acciones.
     * 
     * @param dominio IP de dominio (La palabra "localhost" también es válida).
     * @param puerto Puerto del servidor Alfresco
     * @param user Nombre de usuario a iniciar sesión.
     * @param pass Contraseña del usuario.
     */
    public AlfrescoIntegrator(String dominio, int puerto, String user, String pass) {
        LOGGER.info("Inicializando Conexión...");
        
        String urlDominio = "http://" + dominio + ":" + puerto + "/alfresco/";
        
        // Crea una nueva instancia de un objeto de la clase SessionFactory.
        factory = SessionFactoryImpl.newInstance();
        
        /* Crea una nueva instancia del HashMap que contendrá los parametros de
        conexión */
        parameter = new HashMap();
        
        // Credenciales.
        parameter.put(SessionParameter.USER, user);
        parameter.put(SessionParameter.PASSWORD, pass);
        
        // Datos de conexión para conectarse con CMIS.
        parameter.put(SessionParameter.ATOMPUB_URL,
                urlDominio + "service/api/cmis");

        // Indica tipo de conexión.
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        
        try {
            
            /* Intenta obtener el primer repositorio disponible y se intenta
            conectar a el. */
            sesion = factory.getRepositories(parameter).get(0).createSession();
            
            LOGGER.info("Conexion iniciada.");
            
        }
        
        // Excepciones en caso de credenciales invalidas.
        catch(CmisRuntimeException | CmisPermissionDeniedException ex) { 
            
            LOGGER.error("Credenciales invalidas.");
            throw ex;
            
        }
        
        // Excepción en caso de problemas de conexión.
        catch(CmisConnectionException ex) { 
            
            LOGGER.error("No se puede conectar a " + dominio + ". Dominio inexistente o no disponible actualmente.");
            throw ex;
            
        }
        
        // Excepción en caso de otros errores.
        catch(Exception ex) { 
            
            LOGGER.error("Error desconocido.");
            throw ex;
            
        }
    }
    
    private void reconectar() {
        boolean sesionAgotada = false;
        
        try {
            sesion.getRootFolder();
        }
        catch(Exception ex) {
            LOGGER.info("La sesión de Alfresco ha expirado. Reconectando...");
            sesionAgotada = true;
        }
        
        if(sesionAgotada) {
            try {

                /* Intenta obtener el primer repositorio disponible y se intenta
                conectar a el. */
                sesion = factory.getRepositories(parameter).get(0).createSession();

                LOGGER.info("Reconeción exitosa.");

            }

            // Excepción en caso de problemas de conexión.
            catch(CmisConnectionException ex) { 

                LOGGER.error("No se puede reconectar al servidor Alfresco. Verifique conexión de red.");
                throw ex;

            }

            // Excepción en caso de otros errores.
            catch(Exception ex) { 

                LOGGER.error("Error desconocido.");
                throw ex;

            }
        }
    }
    
    /**
     * Método que permite agregar un documento de cualquier tipo al servidor de
     * Alfresco.
     * 
     * Si el directorio al que se guarda no existe, se crean las carpetas
     * necesarias para almacenarlo ahí.
     * 
     * @param rutaArchivo Ruta de la ubicación del archivo en el disco duro (Se 
 debe incluir el archivo y la extension a esta rutaDescarga).
     * @param rutaSubida Ruta de destino del archivo en el servidor Alfresco.
 Ej: /Mis Cosas/Importante (La rutaDescarga debe empezar con "/")
     * @throws Exception si hubo algun problema interno en la subida del
     * documento.
     */
    public void agregarDocumento(String rutaArchivo, String rutaSubida) throws Exception {
        
        reconectar();
        try {
            /* Arreglos que contendran las rutas ingresadas por parametro
            divididas por "/"*/
            String[] rutaSplit = rutaArchivo.split("/");
            String[] subidaSplit = rutaSubida.split("/");
            
            /* Se toma la ultima posicion del arreglo, el cual contiene el
            nombre del documento. */
            String archivo = rutaSplit[rutaSplit.length - 1];
            
            // Se guarda el directorio raiz.
            Folder guardar = sesion.getRootFolder();
            
            /* Se crea una variable que contendra la rutaDescarga a medida que el ciclo
            for continue.*/
            String rutaTemp = "";
            
            // Se recorre el arreglo de la rutaDescarga de destino.
            for(int i = 1; i < subidaSplit.length; i++) {
                
                /* Se llena la variable temporal con el nombre del directorio
                actual. */
                rutaTemp += "/" + subidaSplit[i];
                
                // Bandera que define si existe la carpeta actual.
                boolean existe = false;
                
                
                /* Iterador que contiene los subdirectorios y archivos de la
                carpeta. */
                Iterator<CmisObject> hijos = guardar.getChildren().iterator();
                
                while(hijos.hasNext()) { // Mientras aun falten revisar hijos..
                    
                    // Guarda al hijo actual.
                    CmisObject aux = hijos.next();
                    
                    if(aux instanceof Folder) { // Si el hijo es una instancia de Folder...
                        
                        // Se transforma este objeto a un Folder.
                        Folder carpeta = (Folder) aux;
                        
                        /* Si el nombre de la carpeta actual es igual al de la
                        carpeta indicada en la rutaDescarga de destino... */
                        if(carpeta.getPath().split("/")[i].equalsIgnoreCase(subidaSplit[i])) {
                            
                            existe = true; // La bandera cambia su estado.
                            break; // Finaliza el ciclo while.
                            
                        }
                        
                    }
                    
                }
                
                if(!existe) { // Si la carpeta ubicada no existe...
                    
                    // ...Se notifica que el directorio se creará.
                    LOGGER.warn("Directorio " + subidaSplit[i] + " no existe. Creando...");
                    
                    // Se crean las propiedades para ese directorio.
                    Map<String, Object> propCarpeta = new HashMap();
                    
                    // Nombre de la carpeta.
                    propCarpeta.put(PropertyIds.NAME, subidaSplit[i]);
                    
                    // Tipo de objeto (Carpeta)
                    propCarpeta.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
                    
                    /* Crea la carpeta como un subdirectorio del directorio
                    actual */
                    guardar.createFolder(propCarpeta);
                    
                }
                
                /* Se obtiene la carpeta actual para utilizarla en la siguiente
                iteracion y/o al agregar el archivo.*/
                guardar = (Folder) sesion.getObjectByPath(rutaTemp);
                
            }
            
            // Se crea un HashMap para las propiedades del documento.
            Map<String, Object> propiedades = new HashMap();
            
            // Se obtiene la rutaDescarga de origen como objeto Path en base al String.
            Path ruta = Paths.get(rutaArchivo);
            
            // Se obtienen los bytes del archivo en base a la rutaDescarga.
            byte[] datos = Files.readAllBytes(ruta);
            
            // Se definen las propiedades.
            
            // Tipo de objeto (Documento).
            propiedades.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
            
            // Nombre del documento.
            propiedades.put(PropertyIds.NAME, archivo);
            
            /* Se crea un objeto de la clase MagicMatch para obtener el MIME
            Type. */
            MagicMatch match = Magic.getMagicMatch(datos);
            
            // Se crea un InputStream para preparar los bytes del archivo.
            InputStream input = new ByteArrayInputStream(datos);
            
            // Se crea un ContentStream para definir otras propiedades.
            ContentStream contenido = new ContentStreamImpl(archivo,
                    BigInteger.valueOf(datos.length), match.getMimeType(), input);
            
            // Se obtiene la carpeta que se encuentra en la rutaDescarga de destino.
            CmisObject objeto = sesion.getObjectByPath(guardar.getPath());
            
            /* Crea el documento y lo agrega al directorio en el servidor
            Alfresco. */
            Document doc = ((Folder)objeto).createDocument(propiedades, contenido, VersioningState.MAJOR);
            
            LOGGER.info("El documento se ha agregado correctamente.");
        }
        
        /* Excepción en caso de que ocurra un error durante el proceso. Deja un
        mensaje en el Logger y vuelve a lanzar la excepción.*/
        catch(Exception ex) { 
            LOGGER.error("No se ha podido agregar el documento" + ex.getLocalizedMessage());
            throw ex;
        }
    }
    
    /**
     * Método que permite descargar el documento desde el servidor Alfresco.
     * 
     * El archivo se guarda en el directorio de descargas del sistema operativo,
     * el cual se determina en base al nombre del mismo y al nombre del usuario
     * que esta utilizando la aplicación.
     * 
     * @param rutaArchivo Ruta del archivo en el servidor Alfresco que se quiere
     * descargar.
     * @throws Exception si hubo algun problema interno en la descarga del
     * documento.
     */
    public void obtenerDocumento(String rutaArchivo) throws Exception {
        reconectar();
        try {
            
            // Se obtene el archivo de la rutaDescarga ingresada.
            CmisObject objeto = sesion.getObjectByPath(rutaArchivo);
            
            // Se transforma a un objeto de la clase Document.
            Document doc = (DocumentImpl) objeto;
            
            // Se obtiene el ContentStream del documento.
            ContentStream contenido = doc.getContentStream();
            
            // Se obtiene el InputStream del ContentStream.
            InputStream input = contenido.getStream();
            
            // Arreglo para almacenar los bytes del archivo.
            byte[] datos = IOUtils.toByteArray(input);

            // Obtiene el nombre del sistema operativo.
            String sistema = System.getProperty("os.name");

            // Obtiene el usuario actual del sistema operativo.
            String usuario = System.getProperty("user.name");

            // Variable donde se guardará la rutaDescarga de destino del archivo.
            Path ruta;

            switch (sistema) { // Según el nombre del sistema...
                case "Windows XP": // ...Si es Windows 7...

                    // Utiliza la Ruta de descarga por defecto de Windows XP.
                    ruta = Paths.get("C:/Documents and settings/" + usuario + 
                            "/Mis documentos/Descargas");
                    break;

                case "Windows 7": // ...Si es Windows 7...

                    // Utiliza la rutaDescarga de descargas por defecto de Windows 7 en adelante.
                    ruta = Paths.get("C:/Users/" + usuario + "/Downloads/" + doc.getName());
                    break;

                case "Windows 8": // ...Si es Windows 8...

                    // Utiliza la rutaDescarga de descargas por defecto de Windows 7 en adelante.
                    ruta = Paths.get("C:/Users/" + usuario + "/Downloads/" + doc.getName());
                    break;

                case "Windows 8.1": // ...Si es Windows 8.1...

                    // Utiliza la rutaDescarga de descargas por defecto de Windows 7 en adelante.
                    ruta = Paths.get("C:/Users/" + usuario + "/Downloads/" + doc.getName());
                    break;

                case "Windows 10": // ...Si es Windows 8...

                    // Utiliza la rutaDescarga de descargas por defecto de Windows 7 en adelante.
                    ruta = Paths.get("C:/Users/" + usuario + "/Downloads/" + doc.getName());
                    break;

                case "Linux":// ...Si es Linux...

                    // Utiliza la rutaDescarga de descarga por defecto de sistemas GNU/Linux.
                    ruta = Paths.get("/home/" + usuario + "/Descargas/" + doc.getName());
                    break;

                default: // ...Por defecto...

                    // Utiliza una rutaDescarga por defecto.
                    ruta = Paths.get("/");
                    break;
            }

            // Se escriben los datos en la rutaDescarga especificada.
            Files.write(ruta, datos);

            LOGGER.info("El archivo se ha guardado correctamente en: " + ruta.toString());
        }
        
        /* Excepción en caso de que ocurra un error durante el proceso. Deja un 
        mensaje en el Logger y vuelve a lanzar la excepción. */
        catch(Exception ex) { 
            
            LOGGER.error("No se pudo descargar el archivo: " + ex.getLocalizedMessage());
            throw ex;
            
        }
    }
    
    public void obtenerVariosDocumentos(String nombre, String ruta,
            boolean caseSensitive) throws Exception {
        reconectar();
        Folder carpeta = (Folder) sesion.getObjectByPath(ruta);
        
        List<Document> documentosEncontrados = obtenerListaDocumentos(nombre,
                carpeta, caseSensitive);
        
        try {
            // Obtiene el nombre del sistema operativo.
            String sistema = System.getProperty("os.name");

            // Obtiene el usuario actual del sistema operativo.
            String usuario = System.getProperty("user.name");
            
            // Variable donde se guardará la rutaDescarga de destino del archivo.
            String rutaDescarga;

            switch (sistema) { // Según el nombre del sistema...
                case "Windows XP": // ...Si es Windows XP...

                    // Utiliza la ruta de descarga por defecto de Windows XP.
                    rutaDescarga = "C:/Documents and settings/" + usuario + 
                            "/Mis documentos/Descargas";
                    break;

                case "Windows 7": // ...Si es Windows 7...

                    /* Utiliza la ruta de descargas por defecto de Windows 7 en
                    adelante. */
                    rutaDescarga = "C:/Users/" + usuario + "/Downloads/";
                    break;

                case "Windows 8": // ...Si es Windows 8...

                    /* Utiliza la ruta de descargas por defecto de Windows 7 en
                    adelante. */
                    rutaDescarga = "C:/Users/" + usuario + "/Downloads/";
                    break;

                case "Windows 8.1": // ...Si es Windows 8.1...

                    /* Utiliza la rutaDescarga de descargas por defecto de
                    Windows 7 en adelante. */
                    rutaDescarga = "C:/Users/" + usuario + "/Downloads/";
                    break;

                case "Windows 10": // ...Si es Windows 8...

                    /* Utiliza la rutaDescarga de descargas por defecto de
                    Windows 7 en adelante. */
                    rutaDescarga = "C:/Users/" + usuario + "/Downloads/";
                    break;

                case "Linux":// ...Si es Linux...

                    /* Utiliza la ruta de descarga por defecto de sistemas
                    GNU/Linux. */
                    rutaDescarga = "/home/" + usuario + "/Descargas/";
                    break;

                default: // ...Por defecto...

                    // Utiliza una ruta por defecto.
                    rutaDescarga = "/";
                    break;
            }
            
            for(Document aux : documentosEncontrados) {
                // Se obtiene el ContentStream del documento.
                ContentStream contenido = aux.getContentStream();

                // Se obtiene el InputStream del ContentStream.
                InputStream input = contenido.getStream();

                // Arreglo para almacenar los bytes del archivo.
                byte[] datos = IOUtils.toByteArray(input);
                
                Path rutaArchivo = Paths.get(rutaDescarga + aux.getName());
                
                // Se escriben los datos en la rutaDescarga especificada.
                Files.write(rutaArchivo, datos);

                LOGGER.info("El archivo se ha guardado correctamente en: "
                        + rutaArchivo.toString());
            }
        }
        
        /* Excepción en caso de que ocurra un error durante el proceso. Deja un 
        mensaje en el Logger y vuelve a lanzar la excepción. */
        catch(Exception ex) { 
            
            LOGGER.error("No se pudo descargar el archivo: " +
                    ex.getLocalizedMessage());
            throw ex;
            
        }
    }
    
    private List<Document> obtenerListaDocumentos(String nombre,
            Folder carpetaActual, boolean caseSensitive) {
        List<Document> listaDocumentos = new ArrayList();
        
        Iterator<CmisObject> hijos = carpetaActual.getChildren().iterator();
        
        while(hijos.hasNext()) {
            CmisObject hijoActual = hijos.next();
            
            if (hijoActual instanceof Document) {
                Document doc = (Document) hijoActual;
                if((doc.getName().contains(nombre) && caseSensitive) ||
                        (doc.getName().toUpperCase().contains(
                                nombre.toUpperCase()) && !caseSensitive)) {
                    listaDocumentos.add((Document) hijoActual);
                }
            }
            else if(hijoActual instanceof Folder) {
                listaDocumentos.addAll(obtenerListaDocumentos(nombre,
                        (Folder) hijoActual, caseSensitive));
            }
        }
        
        return listaDocumentos;
    }
}
