/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package activedirectory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

/**
 * Clase que contiene el método main.
 * 
 * @author jr
 * @author Modificaciones por Juan Báez (Conexión hacia OpenLDAP y retoques).
 * @version 1.0
 */

public class implementacionAD {

    /**
     * Metodo main que genera la aplicación a nivel de cliente.
     * 
     * @param args
     * @throws NamingException
     */
    public static void main(String[] args) throws NamingException, IOException {
        System.out.println("\n\nConsultando hacia un servidor LDAP usando Java");
        System.out.println("----------------------------------------------");
        
        /* Se crean variables locales que guardarán la información ingresada por
        el usuario. */
        
        String domain; // Dirección IP del dominio.
        
        String username; // Nombre de usuario que se va a conectar.
        
        String password; // Contraseña del usuario.
        
        String choice; // Parametro de búsqueda (username o email).
        
        String searchTerm; // Nombre de usuario o e-mail que se buscará.
        
        String nombreDominio; // Nombre de dominio (Ej: ejemplo.com).
        
        int opcion = 0; // Variable que define opción de conexión.
        
        boolean esAD = false; // Indica si la conexion es hacia Active Directory
        
        // Crea el objeto que leerá lo que se ingrese por teclado.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        // Bandera que indica si el ciclo del menú de tipo de conexión continúa.
        boolean ciclo = true; 
        
        System.out.println("Ingrese los datos necesarios para conectarse a AD/OpenLDAP\n");
        
        do { // Empieza el ciclo del menú.
            
            System.out.println("Ingrese a que tipo de servidor se quiere conectar");
            System.out.println("1. Active Directory");
            System.out.println("2. OpenLDAP");

            opcion = br.read(); // Lee solo una tecla.

            if(opcion == 49) { // Si la opción escogida es 1 (Caracter ASCII 49)...
                
                /* ...La variable booleana indicara que la conexión se efectuará
                hacia Active Directory. */
                esAD = true;
                
                // Finaliza el ciclo.
                ciclo = false;
                
            }
            else if(opcion == 50) { // Si, en cambio, la opción escogida es 2 (Caracter ASCII 50)... 
                
                /* ...La variable booleana indicara que la conexión se efectuará
                hacia OpenLDAP. */
                esAD = false;
                
                // Finaliza el ciclo.
                ciclo = false;
            }
        }
        while(ciclo); // Muestra nuevamente el menú si la opción escogida es invalida.
        
        br.readLine(); // Limpia el buffer del lector.
        
        /* Se pide los datos necesarios para la conexión hacia LDAP y la
        busqueda del usuario. */ 
        System.out.println("Ingrese IP de dominio:");
        domain = br.readLine();
        System.out.println("Ingrese DN base. Ej: ejemplo.com:");
        nombreDominio = br.readLine();
        System.out.println("Ingrese nombre de usuario:");
        username = br.readLine();
        System.out.println("Ingrese contraseña:");
        password = br.readLine();
        System.out.println("¿Por cuál atributo desea buscar? (username/email):");
        choice = br.readLine();
        System.out.println("Ingrese valor a buscar:");
        searchTerm = br.readLine();

        /* Se crea una instancia de la clase ActiveDirectory (Inicializa la
        conexión). */
        ActiveDirectory activeDirectory = new ActiveDirectory(username, password, domain, esAD, nombreDominio);

        // Busca al usuario 
        NamingEnumeration<SearchResult> result = activeDirectory.searchUser(searchTerm,
                choice, nombreDominio);

        if (result.hasMore()) { // Si se obtuvo un resultado...
            
            // Se toma el primer resultado obtenido.
            SearchResult rs = (SearchResult) result.next();
            
            // Se extraen los resultados.
            Attributes attrs = rs.getAttributes();
            
            // Variable temporal para mostrar los valores.
            String temp;
            
            if(esAD) { // Si la conexíon se hizo hacia Active Directory...
                
                /* ...Se utiliza el atributo de nombre de usuario de Active
                Directory. */
                temp = attrs.get("samaccountname").toString();
                
            }
            else { // ...Si, en cambio, se hizo hacia OpenLDAP...
                
                // ...Se utiliza el atributo de OpenLDAP.
                temp = attrs.get("uid").toString();
            }
            
            // Muestra el nombre del usuario encontrado.
            System.out.println("Nombre de Usuario   : " +
                    temp.substring(temp.indexOf(":") + 1));
            
            // Muestra el primer nombre del usuario, si contiene uno.
            if(attrs.get("givenname") != null) {
                temp = attrs.get("givenname").toString();
                System.out.println("Primer Nombre       : " +
                        temp.substring(temp.indexOf(":") + 1));
            }
            
            // Muestra el e-mail del usuario, si contiene uno.
            if(attrs.get("mail") != null) {
                temp = attrs.get("mail").toString();
                System.out.println("Correo Electrónico  : " +
                        temp.substring(temp.indexOf(":") + 1));
            }
            
            // Muestra el nombre completo del usuario, si contiene uno.
            if(attrs.get("cn") != null) {
                temp = attrs.get("cn").toString();
                System.out.println("Nombre Completo     : " +
                        temp.substring(temp.indexOf(":") + 1));
            }
            
        } else { // ...Si no...
            
            // Muestra un mensaje en la consola.
            System.out.println("No se han encontrado resultados.");
            
        }

        //Cierra la conexión hacia el servicio LDAP.
        activeDirectory.closeLdapConnection();
    }
}

