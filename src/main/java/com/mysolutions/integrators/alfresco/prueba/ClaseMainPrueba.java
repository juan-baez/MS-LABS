/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysolutions.integrators.alfresco.prueba;

import com.mysolutions.integrators.alfresco.AlfrescoIntegrator;
import java.util.Scanner;

/**
 *
 * @author mysolutions
 */
public class ClaseMainPrueba {

    // Scanner para leer lo que se ingresa por teclado.
    public static Scanner scan = new Scanner(System.in);
    
    // Instancia de la clase AlfrescoIntegrator.
    public static AlfrescoIntegrator con;
    
    /**
     * Metodo main.
     * @param args Parametros de la linea de comandos.
     */
    public static void main(String[] args) {
        
        // Bandera para definir continuidad del ciclo.
        boolean ciclo = true;
        
        // Datos necesarios para la conexión a Alfresco.
        
        // IP del dominio.
        String dominio;
        
        int puerto;
        
        // Credenciales de acceso.
        String user;
        String pass;
        
        // Variable para escoger la opción indicada por pantalla.
        int opcion;
        
        System.out.println("Conectando a Alfresco mediante CMIS...");
        
        do { // Se pedirá la IP del servidor.
            
            System.out.print("Ingrese IP de dominio: ");
            
            /* Lee por teclado la IP del dominio, la deja en minusculas y se
            eliminan los espacios en los extremos. */
            dominio = scan.nextLine().toLowerCase().trim();
            
            // Si lo que se lee por teclado es "localhost"...
            if(dominio.equals("localhost")) { 
                
                // ...Finaliza el ciclo.
                ciclo = false;
                
            }
            // Si, en cambio, lo que se lee contiene un punto (.)...
            else if(dominio.contains(".")) { 
                
                /* ...Crea un arreglo que divide la IP en partes. La razon de
                por qué se reemplazan los puntos en otro caracter es porque el
                metodo split retorna un arreglo vacio si se hace con puntos. */
                String[] dominioArray = dominio.replace('.', ':').split(":");
                
                // Si el arreglo de la IP contiene 4 partes...
                if(dominioArray.length == 4) { 
                    
                    try {
                        
                        // ... Para cada parte dentro del arreglo...
                        for(String aux : dominioArray) { 
                            
                            int parse = Integer.valueOf(aux);
                            
                            if(parse < 0 || parse > 255) {
                                throw new Exception("Valor fuera de rango");
                            }
                            
                        }
                        
                        // Si todo sale bien, termina el ciclo.
                        ciclo = false;
                        
                    }
                    
                    /* Excepcion que ocurre si una de las partes del arreglo no 
                    se puede transformar. Simplemente lo que hace es omitir
                    desde donde ocurrio la excepción, generando que el ciclo
                    continue. */
                    catch(Exception ex) {
                    }
                    
                }
                
            }
            
        }
        while(ciclo);
        
        // Reinicia la bandera.
        ciclo = true;
        
        do { // 
            
            System.out.print("Ingrese puerto de servidor Alfresco: ");
            
            try {
                puerto = scan.nextInt();
            }
            catch(Exception ex) {
                puerto = 0;
            }
            
            scan.nextLine();
                
            if(puerto > 0) {
                // ...Finaliza el ciclo.
                ciclo = false;
            }
            
        }
        while(ciclo);
        
        ciclo = true;
        
        do { // Se pide nombre de usuario con el que conectarse.
            
            System.out.print("Ingrese usuario: ");
            
            /* Lee el nombre de usuario por parametro y elimina los espacios a
            los extremos. */
            user = scan.nextLine().trim();
            
            if(user.length() > 0) { // Si el usuario ingreso algo...
                
                // ...Finaliza el ciclo.
                ciclo = false;
                
            }
            
        }
        while(ciclo);
        
        // Reinicia la bandera.
        ciclo = true;
        
        do { // Se pide contraseña del usuario.
            
            System.out.print("Ingrese contraseña: ");
            
            // Lee la contraseña por teclado.
            pass = scan.nextLine();
            
            if(pass.length() > 0) { // Si el usuario ingreso algo...
                
                // ...Finaliza el ciclo.
                ciclo = false;
                
            }
            
        }
        while(ciclo);
        
        try {
            // ...Se intenta una conexión mediante CMIS.
            con = new AlfrescoIntegrator(dominio, puerto, user, pass);
        }
        
        /* Excepción en caso de que la conexión haya fallado. Finaliza la
        aplicacion con un codigo que no es 0 (cero) (Error fatal).*/
        catch(Exception ex) {
            System.exit(1);
        }
        
        // Si se logra conectar correctamente...
        
        // ...Reinicia la bandera.
        ciclo = true;
        
        do { // Menú principal.
            
            System.out.println("Ingrese la acción que desee realizar:");
            System.out.println("1. Subir un documento.");
            System.out.println("2. Descargar un documento.");
            System.out.println("3. Descargar varios archivos");
            System.out.println("4. Salir");
            System.out.print("Opción: ");
            
            try {
                
                opcion = scan.nextInt(); // Intenta leer un número por teclado.
                
            }
            
            /* Excepción en caso de que el usuario no ingrese un número. Deja
            una opción invalida por defecto (0).*/
            catch(Exception ex) {
                
                opcion = 0;
                
            }
            
            System.out.println();
            scan.nextLine(); // Limpia el buffer del Scanner.
            
            switch(opcion) { // Según la opción que se escoja...
                
                case 1: //... En caso de que se escoja la opción 1...
                    
                    // ...Se va a la sección de agregar documento.
                    agregarDocumento();
                    break;
                    
                case 2: // ...En caso de que se escoja la opción 2...
                    
                    // ...Se va a la sección de obtener documento.
                    obtenerDocumento();
                    break;
                
                case 3:
                    obtenerVariosDocumentos();
                    break;
                    
                case 4: // ...En caso de que se escoja la opción 3...
                    
                    // ...Finaliza el ciclo y la aplicación de forma exitosa.
                    ciclo = false;
                    break;
                    
            }
            
        }
        while(ciclo);
    }
    
    /**
     * Metodo que despliega la sección de agregar documento.
     */
    public static void agregarDocumento() {
        
        // Bandera para definir continuidad del ciclo.
        boolean ciclo = true;

        // Rutas de origen y destino respectivamente.
        String rutaArchivo, rutaSubida;

        System.out.println("AVISO: - Para indicar un directorio, tanto de "
                + "Alfresco como del PC, utilice \'/\'");
        System.out.println("       - El directorio raiz de Alfresco debe "
                + "especificar el 'Company Home' colocando '/' al inicio del "
                + "directorio.");
        System.out.println("Ejemplo: /<carpeta>/<archivo>.<ext>");

        do { // Pregunta por nombre y ubicación del archivo.

            System.out.print("Ingrese ruta y nombre del archivo que desee "
                    + "subir: ");

            // Lee por teclado el directorio de origen del archivo.
            rutaArchivo = scan.nextLine().trim();

            if(rutaArchivo.length() > 0) { // Si el usuario ingreso algo...

                // ...Finaliza el ciclo.
                ciclo = false;

            }

        }
        while(ciclo);

        // Reinicia la bandera.
        ciclo = true;

        do { // Pregunta por la nombre de destino del archivo a subir.

            System.out.print("Ingrese ruta de subida del archivo: ");

            // Lee por teclado el directorio de destino en el servidor Alfresco.
            rutaSubida = scan.nextLine().trim();

            if(rutaSubida.length() > 0) { // Si el usuario ingreso algo...

                // ...Finaliza el ciclo.
                ciclo = false;

            }

        }
        while(ciclo);

        try {
            // ...Intenta subir el documento especificado mediante CMIS.
            con.agregarDocumento(rutaArchivo, rutaSubida);

        }

        /* Excepción en caso de que la subida haya fallado. Muestra un pequeño
        mensaje y vuelve al menú anterior. */
        catch(Exception ex) {

            System.out.println("¡La subida ha fallado! Intentelo nuevamente.");

        }
        
        /* Si todo sale bien, el archivo se agrega en la nombre especificada y el
        usuario retorna al menú anterior. */
    }
    
    /**
     * Metodo que despliega la sección de obtener documento.
     */
    public static void obtenerDocumento() {
        
        // Bandera para definir continuidad del ciclo.
        boolean ciclo = true;
        
        // Ruta de origen del archivo en el servidor Alfresco.
        String ruta;
        
        System.out.println("AVISO: - Para indicar un directorio utilice \'/\'");
        System.out.println("       - El directorio raiz debe especificar el "
                + "'Company Home' colocando '/' al inicio del directorio.");
        System.out.println("Ejemplo: /<carpeta>/<archivo>.<ext>");
        
        do { // Pregunta por nombre y ubicación del archivo en Alfresco.
            
            System.out.print("Ingrese ruta y nombre del archivo que desea "
                    + "descargar: ");
            
            // Lee por teclado el directorio de origen del archivo.
            ruta = scan.nextLine().trim();
            
            if(ruta.length() > 0) { // Si el usuario ingreso algo...
                
                ciclo = false; // ...Finaliza el ciclo.
                
            }
            
        }
        while(ciclo);
        
        try {
                
            // ...Intenta obtener el documento mediante CMIS.
            con.obtenerDocumento(ruta);
            
        }
        
        /* Excepción en caso de que la descarga del documento haya fallado.
        Muestra un mensaje y luego vuelve al menú anteriór.*/
        catch(Exception ex) {
            System.out.println("¡La descarga ha fallado! "
                    + "Intentelo nuevamente.");
        }
        
        /* Si todo sale bien, el documento se guarda en el directorio de
        descargas por defecto y el usuario retorna al menú anterior.*/
    }

    private static void obtenerVariosDocumentos() {
        // Bandera para definir continuidad del ciclo.
        boolean ciclo = true;
        
        String nombre; // Parte del nombre de los archivos que se buscan.
        
        String ruta; // Ruta donde se encontraría el (los) archivo(s) buscados.
        boolean caseSensitive = false;
        
        System.out.println("AVISO: - Para indicar un directorio utilice \'/\'");
        System.out.println("       - El directorio raiz debe especificar el "
                + "'Company Home' colocando '/' al inicio del directorio.");
        System.out.println("Ejemplo: /<carpeta>/<archivo>.<ext>");
        
        do { // Pregunta por nombre y ubicación del archivo en Alfresco.
            
            System.out.print("Ingrese ruta donde se encuentran el (los) "
                    + "archivo(s) que desea descargar: ");
            
            // Lee por teclado el directorio de origen del archivo.
            ruta = scan.nextLine().trim();
            
            if(ruta.length() > 0 && ruta.contains("/")) { // Si el usuario ingreso algo...
                
                ciclo = false; // ...Finaliza el ciclo.
                
            }
            
        }
        while(ciclo);
        
        do { // Pregunta por nombre y ubicación del archivo en Alfresco.
            
            System.out.print("Ingrese parte de los nombres de los archivos que "
                    + "desea descargar: ");
            
            // Lee por teclado el directorio de origen del archivo.
            nombre = scan.nextLine().trim();
            
            if(nombre.length() > 0) { // Si el usuario ingreso algo...
                
                ciclo = false; // ...Finaliza el ciclo.
                
            }
            
        }
        while(ciclo);
        
        ciclo = true;
        
        do { // Pregunta por nombre y ubicación del archivo en Alfresco.
            
            System.out.print("¿Case-sensitive? S/N: ");
            
            // Lee por teclado la opción escogida.
            String opcionCaseSensitive = scan.nextLine().trim();
            
            if(opcionCaseSensitive.equalsIgnoreCase("s") ||
                    opcionCaseSensitive.equalsIgnoreCase("si")) {
                caseSensitive = true;
                ciclo = false;
            }
            else if (opcionCaseSensitive.equalsIgnoreCase("n") ||
                    opcionCaseSensitive.equalsIgnoreCase("no")) {
                caseSensitive = false;
                ciclo = false;
            }
            
        }
        while(ciclo);
        
        try {
                
            // ...Intenta obtener el documento mediante CMIS.
            con.obtenerVariosDocumentos(nombre, ruta, caseSensitive);
            
        }
        
        /* Excepción en caso de que la descarga del documento haya fallado.
        Muestra un mensaje y luego vuelve al menú anteriór.*/
        catch(Exception ex) {
            System.out.println("¡La descarga ha fallado! Intentelo nuevamente.");
        }
        
        /* Si todo sale bien, el documento se guarda en el directorio de
        descargas por defecto y el usuario retorna al menú anterior.*/
    }
    
}
