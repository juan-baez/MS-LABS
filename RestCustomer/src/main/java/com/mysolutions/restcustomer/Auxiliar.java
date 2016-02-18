/////*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package com.mysolutions.restcustomer;
//
///**
// *
// * @author Emilio
// */
//public class Auxiliar {
//    import com.google.gson.Gson;
//import com.mysolutions.model.Student;
//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import org.apache.http.client.ClientProtocolException;
//import org.jboss.resteasy.client.ClientRequest;
//import org.jboss.resteasy.client.ClientResponse;
//
//public class RESTEasyClientGet {
//
//    public static void main(String[] args) {
//        Gson gson = new Gson();
//
//        try {
//
//            
//            ClientRequest request = new ClientRequest(
//                    "http://localhost:8080/RESTService/rest/jsonServices/print");
//            request.accept("application/json");
//
//            ClientResponse<String> response = request.get(String.class);
//
//            if (response.getStatus() != 200) {
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + response.getStatus());
//            }
//
//            BufferedReader brs = new BufferedReader(new InputStreamReader(
//			new ByteArrayInputStream(response.getEntity().getBytes())));
//
//            String output;
//            System.out.println("Output from Server .... \n");
//            
//
//            while ((output = brs.readLine()) != null) {
//            Student stdu = gson.fromJson(output, Student.class);
//                System.out.println(stdu.getFirstName()+" "+stdu.getLastName()+ " Edad:" + stdu.getAge());
//            }
//
//        } catch (ClientProtocolException e) {
//        } catch (IOException e) {
//
//        } catch (Exception e) {
//
//
//        }
//
////    }
////
////}
//
//}
