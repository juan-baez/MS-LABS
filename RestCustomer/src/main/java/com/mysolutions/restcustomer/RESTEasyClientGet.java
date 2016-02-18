/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysolutions.restcustomer;

/**
 *
 * @author Emilio
 */
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
import com.mysolutions.model.Student;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

public class RESTEasyClientGet {

 /*param @request = ruta de acceso a Json, debe concordar con 
  ruta establecida en endpoint (Servicio rest)*/
    public static void main(String[] args) {
        Gson gson = new Gson();

        try {

            ClientRequest request = new ClientRequest(
                    "http://localhost:8080/RESTService/rest/jsonServices/printAll"); 
            request.accept("application/json");

            ClientResponse<String> response = request.get(String.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            BufferedReader brs = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(response.getEntity().getBytes())));
            System.out.println("Output from Server .... \n");
            /*Procedimiento que transforma los elementos de una lista JSON
            en una lista del objeto especificado  */ 
            
            List<Student> students = new ArrayList<>();
            for (String stdl1 : split(brs.readLine())) {
                Student s = gson.fromJson(stdl1, Student.class);
                if (s != null) {
                    students.add(s);
                }
            }
            for (Student st : students) {
                System.out.println("ID: " + st.getId() + " Nombre: " + st.getFirstName()
                        + " Apellido: " + st.getLastName() + " Edad: " + st.getAge());
            }

        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        } catch (Exception e) {

        }

    }

    /*Método que recibe como parámetros un String con una lista de objetos
    * dentro de un Json y retorna una lista con cada uno de los
    *elementos separados*/
    public static List<String> split(String jsonArray) throws Exception {
        List<String> splittedJsonElements = new ArrayList<>();
        ObjectMapper jsonMapper = new ObjectMapper();
        JsonNode jsonNode = jsonMapper.readTree(jsonArray);

        if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode individualElement = arrayNode.get(i);
                splittedJsonElements.add(individualElement.toString());
            }
        }
        return splittedJsonElements;
    }

}
