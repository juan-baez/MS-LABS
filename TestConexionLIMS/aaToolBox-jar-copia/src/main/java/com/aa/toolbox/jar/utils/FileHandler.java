/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aa.toolbox.jar.utils;

import com.aa.toolbox.jar.exceptions.FileHandlerException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author José Leonardo Jerez
 */
public class FileHandler extends FilenameUtils {

    /**
     * Este método permite guardar la data en un archivo con una ruta específica
     *
     * @param inputStream
     * @param route - Ruta donde se desea crear el archivo
     * @param fileName - Nombre que tendrá el archivo
     * @param arrayByte - Tamaño máximo del archivo a crear (ejemplo: 1024)
     * @throws IOException
     */
    public static void writeFile(final InputStream inputStream, final String route, final String fileName, final int arrayByte) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(new File(route, fileName))) {
            int reading;
            final byte[] bytes = new byte[arrayByte];
            
            while ((reading = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, reading);
            }
            inputStream.close();
            outputStream.flush();
        }
    }

    /**
     * Permite eliminar un archivo ubicado en la ruta y con el nombre indicado
     *
     * @param absolutePath
     * @param fileName
     */
    public static void deleteFile(final String absolutePath, final String fileName) {
        new File(new StringBuilder().append(absolutePath).append("//").append(fileName).toString()).delete();
    }

    /**
     * Este metodo realiza una validacion de la existencia de un archivo ubicado
     * en la ruta que se indica en el parametro
     *
     * @param path - Ruta del archivo a validar
     * @param isAbsolutePath - Indica si la ruta que se va a leer esta
     * relacionada al Target del despliegue o una ruta absoluta
     * @throws com.grupoconamerica.backend.exception.FileHandlerException
     */
    public static void isExistsFile(final String path, final boolean isAbsolutePath) throws FileHandlerException {
        if (isAbsolutePath) {
            if (new File(path).exists()) {
                throw new FileHandlerException("El archivo ya existe", 92);
            }
        } else {

        }
    }

    /**
     * Este metodo se encarga de generar un arreglo de bytes con el contenido
     * del archivo alojado en el directorio que se indica por parametro
     *
     * @param absolutePath - contenedor del archivo
     * @return
     * @throws java.io.IOException en caso de que sea imposible leer el archivo
     */
    public static byte[] generarArregloByte(final String absolutePath) throws IOException {
        return Files.readAllBytes(Paths.get(absolutePath));
    }

    /**
     * Este método permite generar el nombre del archivo en caso de que ya
     * exista otro, dentro del ruta indicado, con el mismo nombre se le agrega
     * un numero que lo identifique, ejemplo: si el nombre del archivo
     * (archivoPrueba.txt) ya existe se obtendra un nombre como
     * archivoPrueba(1).txt
     *
     * @param path
     * @param fileName
     * @return Nombre del archivo
     */
    public static String nameGenerator(final String path, final String fileName) {
        final StringBuilder name = new StringBuilder().append(FilenameUtils.getBaseName(fileName)).append(".").append(FilenameUtils.getExtension(fileName));
        final StringBuilder pathVerfication = new StringBuilder().append(path).append(new StringBuilder().append(FilenameUtils.getBaseName(fileName)).append(".").append(FilenameUtils.getExtension(fileName)).toString());
        boolean continua = true;
        int sequence = 0;
        do {
            try {
                isExistsFile(pathVerfication.toString(), true);
                continua = false;
            } catch (FileHandlerException excepcionArchivo) {
                if (excepcionArchivo.getError() == 92) {
                    sequence++;
                    name.setLength(0);
                    name.append(FilenameUtils.getBaseName(fileName)).append("(").append(sequence).append(")").append(".").append(FilenameUtils.getExtension(fileName));
                    pathVerfication.setLength(0);
                    pathVerfication.append(path).append(name.toString());
                } else {
                    continua = false;
                }
            }
        } while (continua);
        return name.toString();
    }
}
