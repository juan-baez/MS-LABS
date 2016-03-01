/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysolutions.integrators.alfresco;

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
     * @param rutaAlfresco Ruta donde se ubican los servicios de Alfresco
     * @param user Nombre de usuario a iniciar sesión.
     * @param pass Contraseña del usuario.
     */
    public AlfrescoIntegrator(String dominio, int puerto, String rutaAlfresco, 
            String user, String pass) {
        LOGGER.info("Inicializando Conexión...");
        
        String urlDominio = "http://" + dominio + ":" + puerto + "/" + rutaAlfresco + "/";
        
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
     * debe incluir el archivo y la extension a esta ruta).
     * @param rutaSubida Ruta de destino del archivo en el servidor Alfresco.
     * Ej: /Mis Cosas/Importante (La ruta debe empezar con "/")
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
            
            /* Se crea una variable que contendra la ruta a medida que el ciclo
            for continue.*/
            String rutaTemp = "";
            
            // Se recorre el arreglo de la ruta de destino.
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
                        carpeta indicada en la ruta de destino... */
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
            
            // Se obtiene la ruta de origen como objeto Path en base al String.
            Path ruta = Paths.get(rutaArchivo);
            
            // Se obtienen los bytes del archivo en base a la ruta.
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
            Document doc = ((Folder)objeto).createDocument(propiedades,
                    contenido, VersioningState.MAJOR);
            
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
     * Método que permite descargar un documento desde el servidor Alfresco
     * mediante su ruta.
     * 
     * El archivo se guarda en un directorio determinado por el usuario.
     * 
     * @param rutaArchivo Ruta del archivo en el servidor Alfresco que se quiere
     * descargar (Company Home es representado con un '/' al inicio de la ruta).
     * @param rutaDescarga Ruta en el PC donde se descargará el documento.
     * @throws Exception si hubo algun problema interno en la descarga del
     * documento.
     */
    public void obtenerDocumentoPorRuta(String rutaArchivo, String rutaDescarga)
            throws Exception {
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

            // Variable donde se guardará la ruta de destino del archivo.
            Path ruta = Paths.get(rutaDescarga);

            // Se escriben los datos en la ruta especificada.
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
    
    /**
     * Método que permite descargar uno o varios documentos desde el servidor
     * Alfresco mediante el nombre del(de los) documento(s) o parte de este.
     * 
     * El(Los) archivo(s) se guarda(n) en un directorio determinado por el
     * usuario.
     * 
     * @param nombre Todo o parte del nombre del archivo.
     * @param rutaBusqueda Ruta en Alfresco donde se efectuará la busqueda. Esta
     * búsqueda incluye los subdirectorios de esta ubicación (Company Home es
     * representado con un '/' al inicio de la ruta).
     * @param rutaDescarga Ruta en el PC donde se descargará el archivo.
     * @param caseSensitive Indica si la búsqueda considerará mayusculas y
     * minusculas.
     * @throws Exception En caso de que haya ocurrido un error con la descarga.
     */
    public void obtenerDocumentosPorNombre(String nombre, String rutaBusqueda,
            String rutaDescarga, boolean caseSensitive) throws Exception {
        reconectar();
        Folder carpeta = (Folder) sesion.getObjectByPath(rutaBusqueda);
        
        List<Document> documentosEncontrados = obtenerListaDocumentos(nombre,
                carpeta, caseSensitive);
        
        try {
            
            for(Document aux : documentosEncontrados) {
                // Se obtiene el ContentStream del documento.
                ContentStream contenido = aux.getContentStream();

                // Se obtiene el InputStream del ContentStream.
                InputStream input = contenido.getStream();

                // Arreglo para almacenar los bytes del archivo.
                byte[] datos = IOUtils.toByteArray(input);
                
                Path rutaArchivo = Paths.get(rutaDescarga + aux.getName());
                
                // Se escriben los datos en la ruta especificada.
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
    
    /**
     * Busca y obtiene una lista de documentos desde una ubicación dentro del
     * servidor Alfresco.
     * 
     * Esta búsqueda incluye los subdirectorios de la ubicación escogida para
     * buscar. Además, este método es recursivo para poder lograr esto.
     * 
     * @param nombre Todo o parte del nombre del archivo.
     * @param carpetaActual Ubicación en la que se encuentra actualmente
     * buscando.
     * @param caseSensitive Indica si la búsqueda considerará mayusculas y
     * minusculas.
     * @return Una lista de objetos de la clase Document que contengan el nombre
     * recibido por parámetro
     */
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
