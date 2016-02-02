/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aa.toolbox.jar.utils;

import com.aa.toolbox.jar.enums.FileExtensions;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author pastorduran
 */
public class Reader {

    private long numberCharacter;
    private long column;
    private boolean endFile = false;

    public Reader() {
        numberCharacter = 0;
        column = 0;
    }

    /**
     * Metodo que se encarga de la lectura de Archivo con extension XLS
     *
     * @param fileName
     * @param numeroHoja
     * @param contieneCabecero
     * @param numeroFila
     * @return
     * @throws java.io.IOException
     */
    public static ArrayList<ArrayList<String>> readSheedExcelXLS(InputStream fileName, Integer numeroHoja, boolean contieneCabecero, int numeroFila) throws IOException {
        ArrayList<ArrayList<String>> filas = new ArrayList();
        Iterator rowIterator = new HSSFWorkbook(fileName).getSheetAt(numeroHoja).rowIterator();
        Integer i = 0;
        while (rowIterator.hasNext()) {
            HSSFRow fila = (HSSFRow) rowIterator.next();
            if (contieneCabecero) {
                fila = (HSSFRow) rowIterator.next();
                contieneCabecero = false;
            }
            Iterator iterator = fila.cellIterator();
            ArrayList<String> columnas = new ArrayList();
            while (iterator.hasNext()) {
                HSSFCell columna = (HSSFCell) iterator.next();
                if (columna.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                    if (columna.getNumericCellValue() % 1 != 0) {
                        columnas.add(new DecimalFormat("#0.00").format(columna.getNumericCellValue()));
                    } else {
                        columnas.add(new DecimalFormat("#0").format(columna.getNumericCellValue()));
                    }
                } else {
                    columnas.add(columna.toString());
                }
            }
            filas.add(columnas);
            i++;
            if (numeroFila > 0 && i == numeroFila) {
                break;
            }

        }
        return filas;
    }

    public static ArrayList<ArrayList<String>> readSheedCVS(InputStream fileName, Integer numeroHoja, boolean contieneCabecero, int numeroFila) throws IOException {
        BufferedReader fileBuffer = null;
        ArrayList<ArrayList<String>> filas = new ArrayList();
        try {
            int i = 0;
            String line;
            fileBuffer = new BufferedReader(new InputStreamReader(fileName));
            // How to read file in java line by line?
            while ((line = fileBuffer.readLine()) != null) {
                filas.add(columnCSVtoArrayList(line));
                i++;
                if (i == numeroFila) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileBuffer != null) {
                    fileBuffer.close();
                }
            } catch (IOException fileException) {
                fileException.printStackTrace();
            }
        }
        return filas;
    }

    // Utility which converts CSV to ArrayList using Split Operation
    public static ArrayList<String> columnCSVtoArrayList(String filaCSV) {
        ArrayList<String> column = new ArrayList();

        if (filaCSV != null) {
            String[] splitData = filaCSV.split(";");
            for (int i = 0; i < splitData.length; i++) {
                if (!(splitData[i] == null) || !(splitData[i].length() == 0)) {
                    column.add(splitData[i].trim());
                }
            }
        }

        return column;
    }

    /**
     * Metodo que se encarga de la lectura de Archivo con extension XLSX Si el
     * parametro numeroFila es 0 se leera el total de filas que poseea archivo
     * de lo contrario se leera la cantidad de filas indicadas
     *
     * @param fileName
     * @param numeroHoja
     * @param contieneCabecero
     * @param numeroFila
     * @return
     * @throws java.io.IOException
     */
    public static ArrayList<ArrayList<String>> readSheedExcelXLSX(InputStream fileName, Integer numeroHoja, boolean contieneCabecero, Integer numeroFila) throws IOException, InvalidFormatException {
        ArrayList<ArrayList<String>> filas = new ArrayList();
        XSSFWorkbook workBook = new XSSFWorkbook(fileName);
        Sheet xssfSheet = workBook.getSheetAt(numeroHoja);
        Iterator<Row> rowIterator = xssfSheet.rowIterator();
        Integer i = 0;
        while (rowIterator.hasNext()) {
            XSSFRow fila = (XSSFRow) rowIterator.next();
            if (contieneCabecero) {
                fila = (XSSFRow) rowIterator.next();
                contieneCabecero = false;
            }
            Iterator iterator = fila.cellIterator();
            ArrayList<String> columnas = new ArrayList();
            while (iterator.hasNext()) {
                XSSFCell columna = (XSSFCell) iterator.next();

                if (columna.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                    if (DateUtil.isCellDateFormatted(columna)) {
                        columnas.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(columna.getDateCellValue()));
                    } else {
                        if (columna.getNumericCellValue() % 1 != 0) {
                            columnas.add(new DecimalFormat("#0.00").format(columna.getNumericCellValue()));
                        } else {
                            columnas.add(new DecimalFormat("#0").format(columna.getNumericCellValue()));
                        }
                    }
                } else {
                    if (!columna.toString().equalsIgnoreCase("")) {
                        columnas.add(columna.toString());
                    }
                }
            }
            filas.add(columnas);
            i++;
            if (numeroFila > 0 && i == numeroFila) {
                break;
            }
        }
        return filas;
    }

    /**
     * Metodo que se encarga de recibir por parametro el fichero excel el cual
     * se va leer, el numero de hoja que se va a leer, el tipo de extension para
     * discriminar y si la primera fila del excel se tomara en cuenta para la
     * lectura como cabecera;
     *
     * @param fileInputStream
     * @param numeroHoja
     * @param extension
     * @param contieneCabecero
     * @param numeroFila
     * @return
     * @throws java.io.IOException
     */
    public static ArrayList<ArrayList<String>> readSheedExcel(InputStream fileInputStream, Integer numeroHoja, String extension, boolean contieneCabecero, Integer numeroFila) throws IOException, InvalidFormatException {
        if (extension.equalsIgnoreCase(FileExtensions.CSV.getNombre())) {
            return Reader.readSheedCVS(fileInputStream, numeroHoja, contieneCabecero, numeroFila);
        }
        if (extension.equalsIgnoreCase(FileExtensions.XLS.getNombre())) {
            return Reader.readSheedExcelXLS(fileInputStream, numeroHoja, contieneCabecero, numeroFila);
        } else {
            return Reader.readSheedExcelXLSX(fileInputStream, numeroHoja, contieneCabecero, numeroFila);
        }
    }

    /**
     * Metodo que se encarga de recibir por parametro la ruta del archivo excel,
     * el numero de hoja que se va a leer, el tipo de extension para discriminar
     * y si la primera fila del excel se tomara en cuenta para la lectura como
     * cabecera;
     *
     * @param fileName
     * @param numeroHoja
     * @param extension
     * @param contieneCabecero
     * @param numeroFila
     * @return
     * @throws FileNotFoundException
     * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException
     */
    public static ArrayList<ArrayList<String>> readSheedExcel(String fileName, Integer numeroHoja, String extension, boolean contieneCabecero, Integer numeroFila) throws FileNotFoundException, IOException, InvalidFormatException {
        if (extension.equalsIgnoreCase(FileExtensions.CSV.getNombre())) {
            return Reader.readSheedCVS(new FileInputStream(fileName), numeroHoja, contieneCabecero, numeroFila);
        }
        if (extension.equalsIgnoreCase(FileExtensions.XLS.getNombre())) {
            return Reader.readSheedExcelXLS(new FileInputStream(fileName), numeroHoja, contieneCabecero, numeroFila);
        } else {
            return Reader.readSheedExcelXLSX(new FileInputStream(fileName), numeroHoja, contieneCabecero, numeroFila);
        }
    }

    public static List<List<String>> readSheedCVS(String fileName, char delimiter) throws IOException {
        BufferedReader fileBuffer = null;
        List<List<String>> rows = new ArrayList();
        try {
            int i = 0;
            int columm = 0;
            String line;
            fileBuffer = new BufferedReader(new FileReader(new File(fileName)));
            while ((line = fileBuffer.readLine()) != null) {
                i++;
                String[] row = line.split(String.valueOf(delimiter));
                if (columm == 0) {
                    columm = row.length;
                }
                if (columm == row.length) {
                    rows.add(Arrays.asList(row));
                }
                if (i == 1024) {
                    break;
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                if (fileBuffer != null) {
                    fileBuffer.close();
                }
            } catch (IOException fileException) {
            }
        }
        return rows;
    }

    public static List<List<String>> readSheedCVS(String fileName, char delimiter, final int batchSize) throws IOException {
        BufferedReader fileBuffer = null;
        List<List<String>> rows = new ArrayList();
        try {
            int i = 0;
            int column = 0;
            String line;
            fileBuffer = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            while ((line = fileBuffer.readLine()) != null) {
                i++;
                String lineReplace = line.replace(',', delimiter);
                String[] row = lineReplace.split(String.valueOf(delimiter));
                if (column == 0) {
                    column = row.length;
                }
                if (column == row.length) {
                    rows.add(Arrays.asList(row));
                }
                System.out.println("index: " + i + " line " + line);
                if (batchSize > 0 && i == batchSize) {
                    break;
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                if (fileBuffer != null) {
                    fileBuffer.close();
                }
            } catch (IOException ex) {
            }
        }
        return rows;
    }

    public List<List<String>> readSheedCVS(String fileName, char delimiter, final int batchSize, long delay) {
        int i = 0;
        String line;
        List<List<String>> rows = new ArrayList();
        try {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
                br.skip(numberCharacter);
                while ((line = br.readLine()) != null) {
                    i++;
                    String[] row = line.split(String.valueOf(delimiter));
                    if (column == 0) {
                        column = row.length;
                    }
                    if (column == row.length) {
                        rows.add(Arrays.asList(row));
                        System.out.println(line);
                    }
                    Logger.getLogger(Reader.class.getName()).log(Level.INFO, line);
                    System.out.println(line);
                    numberCharacter = line.length() + numberCharacter;
                    if (i == batchSize) {
                        break;
                    }
                }
                endFile = br.ready();
            } catch (Exception ex) {
                Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            }
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rows;
    }

    public long getNumberCharacter() {
        return numberCharacter;
    }

    public void setNumberCharacter(long numberCharacter) {
        this.numberCharacter = numberCharacter;
    }

    public boolean isEndFile() {
        return endFile;
    }

    public void setEndFile(boolean endFile) {
        this.endFile = endFile;
    }

}
