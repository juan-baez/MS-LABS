/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.labs.ldap.authenticator;

import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 * Clase que prueba la conexión hacia un servidor con Active Directory u
 * OpenLDAP.
 * 
 * @author jr
 * @author Modificaciones por Juan Báez (Conexión hacia OpenLDAP y retoques).
 * @version 1.0
 */
public class ActiveDirectory {

    // Logger que genera mensajes definidos por el programador para avisar al usuario.
    private static final Logger LOG = Logger.getLogger(ActiveDirectory.class.getName());

    // Atributos de la clase
    
    // Propiedades de la conexión. Se comporta igual que un HashTable.
    private Properties properties;
    
    // Contiene y genera la conexión hacia el servicio.
    private DirContext dirContext;
    
    // Contiene los atributos del usuario que se retornaran.
    private SearchControls searchCtls;
    
    /* Atributos que se retornaran. Notese que el primer campo es un String
    vacio (Nombre de usuario). */
    private String[] returnAttributes = {"", "givenName", "cn", "mail"};
    
    // Indica el DN base del dominio con la sintaxis de LDAP.
    private String domainBase;
    
    // Indica el filtro de búsqueda.
    private String baseFilter; 
    
    // Indica si la conexión se esta haciendo hacia Active Directory.
    private boolean activeDir; 

    /**
     * Constructor con parametros para la clase ActiveDirectory.
     * 
     * Hace las configuraciones necesarias para establecer una conexión al servidor
     * de Active Directory/OpenLDAP. Se genera una excepción si algún dato se
     * ingresa erroneamente.
     *
     * @param username objeto de tipo {@link java.lang.String} que indica el
     * nombre de usuario de ActiveDirectory/OpenLDAP.
     * @param password objeto de tipo {@link java.lang.String} que indica la
     * contraseña del usuario de ActiveDirectory/OpenLDAP.
     * @param domainController objeto de tipo {@link java.lang.String} que
     * indica la dirección IP del dominio al que se quiere conectar.
     * @param esAD Señala true si la conexion es hacia Active Directory, false
     * si es hacia OpenLDAP.
     * @param nombreDominio Indica el DN (Nombre distinguido) del dominio. Ej:
     * ejemplo.com (Base DN se debe escribir tal como se muestra en el ejemplo,
     * ya que la aplicaciòn se encarga de separarlo en los diferentes DC que se
     * requieran segùn cada punto '.' que contenga el nombre de dominio)
     */
    public ActiveDirectory(String username, String password, String domainController, boolean esAD, String nombreDominio) {
        
        this.activeDir = esAD;
        
        /* Crea las propiedades de la conexión en base a los parametros de
        entrada. */
        properties = new Properties();
        
        //Tipo de autenticación
        properties.put(Context.SECURITY_AUTHENTICATION, "Simple");
        
        // Contexto de la conexión.
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        
        // URL del servicio LDAP
        properties.put(Context.PROVIDER_URL, "LDAP://" + domainController); 

        if(this.activeDir) { // Si la conexión se efectua hacia Active Directory...
            
            // ...Se generan estas configuraciones hacia el servicio.
            
            // Atributo que indica el nombre de usuario en Active Directory.
            returnAttributes[0] = "sAMAccountName";
            
            /* Clases que se utilizan para las personas y los usuarios
            respectivamente */
            baseFilter = "(&((&(objectCategory=Person)(objectClass=User)))";
            
            // DN de usuario en Active Directory. Utiliza el nombre del dominio.
            properties.put(Context.SECURITY_PRINCIPAL, username + "@" +
                    nombreDominio);
            
        }
        else { // ...Si no...
            
            // ...Se generan estas otras.
            
            // DN base del dominio en formato LDAP.
            String dn = getDomainBase(nombreDominio);
            
            // Atributo que indica el nombre de usuario en OpenLDAP.
            returnAttributes[0] = "uid";
            
            // Clase que se utiliza para el usuario en OpenLDAP.
            baseFilter = "(&(objectClass=posixAccount)";
            
            // DN de usuario en OpenLDAP. Utiliza el DN base.
            properties.put(Context.SECURITY_PRINCIPAL, "uid=" + username + 
                    ",ou=usuarios," + dn);
            //OJO: Hay que editar los grupos (ou) de forma manual en el codigo.
            
        }
        
        properties.put(Context.SECURITY_CREDENTIALS, password); // Contraseña
            
        //Inicializa la conexión hacia Active Directory/OpenLDAP
        try {
            
            // Inicia la conexión hacia el servidor.
            dirContext = new InitialDirContext(properties);
            
        } catch (NamingException e) {
            
            /* Muestra un mensaje en el log que indica el mensaje de la
            excepción, en caso de que la conexión haya fallado. */
            LOG.severe(e.getMessage());
            
        }

        /* Genera la sintaxis de LDAP del DN del servidor:
        Antes: ejemplo.com
        Despues: DC=EJEMPLO,DC=COM */
        domainBase = getDomainBase(domainController);

        /* Inicializa los controles de busquedas para encontrar un usuario
        especifico y se le entrega los parametros de retorno. */
        searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchCtls.setReturningAttributes(returnAttributes);
    }

    /**
     * Busca un usuario en Active Directory/OpenLDAP por su nombre de usuario o
     * e-mail.
     *
     * @param searchValue objeto de tipo {@link java.lang.String} que indica el
     * valor a buscar (nombre de usuario o e-mail)
     * @param searchBy objeto de tipo {@link java.lang.String} que indica por que
     * atributo se va a buscar (username/email)
     * @param searchBase objeto de tipo {@link java.lang.String} que indica el
     * DN base para buscar. Ej: DC=EJEMPLO,DC=COM
     * @return Un {@link javax.naming.NamingEnumeration} que indica el resultado
     * de la busqueda
     * @throws NamingException
     */
    public NamingEnumeration<SearchResult> searchUser(String searchValue,
            String searchBy, String searchBase) throws NamingException {
        
        // Obtiene el filtro de busqueda.
        String filter = getFilter(searchValue, searchBy);
        
        // Genera el DN base, si el que se entrega por parametro es nulo.
        String base = (null == searchBase) ? domainBase : getDomainBase(searchBase);
        
        // Retorna al usuario si los datos son correctos, o lanza un NamingException
        // si ocurrió un error al buscarlo o si este no existe.
        return this.dirContext.search(base, filter, this.searchCtls);
        
    }

    /**
     * Cierra la conexión de Active Directory/OpenLDAP.
     */
    public void closeLdapConnection() {
        try {
            
            if (dirContext != null) { // Si la conexión esta abierta...
                
                // ...Cerrarla.
                dirContext.close(); 
                
                
            }
        } catch (NamingException e) {
            
            // Muestra un mensaje de error si la desconexión obtuvo algun error.
            LOG.severe(e.getMessage()); 
            
        }
    }

    /**
     * Metodo que genera la sintaxis para hacer el filtro de la busqueda.
     *
     * @param searchValue objeto de tipo {@link java.lang.String} que indica el
     * valor a buscar (nombre de usuario o e-mail)
     * @param searchBy objeto de tipo {@link java.lang.String} que indica por que
     * atributo se va a buscar (username/email)
     * @return Un {@link java.lang.String} que indica el filtro de busqueda.
     */
    private String getFilter(String searchValue, String searchBy) {
        
        // Toma el atributo de filtro.
        String filter = this.baseFilter; 
        
        if (searchBy.equals("email")) { // Si el parametro de busqueda es el e-mail...
            
            // ...Se agrega el e-mail al filtro.
            filter += "(mail=" + searchValue + "))"; 
            
        } else if (searchBy.equals("username")) { // Si, en cambio, equivale al nombre de usuario...
            
            if(this.activeDir) { // ...Si se esta haciendo una conexión a Active Directory....
                
                /* ...Utiliza el parametro de nombre de usuario de Active
                Directory (sAMAccountName). */
                filter += "(samaccountname=" + searchValue + "))"; 
            
            }
            else { // Si es hacia OpenLDAP...
                
                // ...Utiliza el parametro de OpenLDAP (uid).
                filter += "(uid=" + searchValue + "))"; 
                
            }
        }
        return filter; // Retorna el filtro de busqueda.
    }

    /**
     * Crea un DN con sintaxis de LDAP. Ej: DC=EJEMPLO,DC=COM
     *
     * @param base objeto de tipo {@link java.lang.String} que indica el nombre
     * del dominio. Ej: ejemplo.com 
     * @return Un {@link java.lang.String} con la sintaxis del DN modificada.
     */
    private static String getDomainBase(String base) {
        /* Toma todo el parametro y genera un arreglo de char en base a el en
        mayusculas. */
        char[] namePair = base.toUpperCase().toCharArray(); 
        
        // Variable que contendra el DN en el nuevo formato.
        String dn = "DC="; 
        
        for (int i = 0; i < namePair.length; i++) { // Recorremos el arreglo.
            
            if (namePair[i] == '.') { // Si en la posición actual se encuentra un punto (.)...
                
                // ...Se omite y se agrega otro DC.
                dn += ",DC=" + namePair[++i];
                
            } else { // ...Si no...
                
                /* ... Se toma el caracter en esa posicion y se agrega a la
                variable. */
                dn += namePair[i];
                
            }
        }
        return dn; // Retorna el DN en formato de LDAP
    }
}
