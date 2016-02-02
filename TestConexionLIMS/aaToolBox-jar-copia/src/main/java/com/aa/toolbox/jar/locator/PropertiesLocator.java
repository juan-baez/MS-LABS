/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aa.toolbox.jar.locator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author José Jerez
 */
public class PropertiesLocator {

    public static String getProperty(String configFile, String property) {
        Properties prop = new Properties();
        try {
            CodeSource fuente = PropertiesLocator.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(fuente.getLocation().toURI().getPath());
            if (jarFile.isDirectory()) {
                File propFile = new File(jarFile, configFile);
                prop.load(new BufferedReader(new FileReader(propFile.getAbsoluteFile())));
            }
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
        return !prop.isEmpty() ? prop.getProperty(property) : null;
    }

    public static Set<Object> getkeys(String configFile) {
        Properties prop = new Properties();
        try {
            CodeSource fuente = PropertiesLocator.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(fuente.getLocation().toURI().getPath());
            if (jarFile.isDirectory()) {
                File propFile = new File(jarFile, configFile);
                prop.load(new BufferedReader(new FileReader(propFile.getAbsoluteFile())));
            }
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
        return !prop.isEmpty() ? prop.keySet() : null;
    }
}
