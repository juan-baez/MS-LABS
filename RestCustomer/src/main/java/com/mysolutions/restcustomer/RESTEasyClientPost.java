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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;


public class RESTEasyClientPost {

	public static void main(String[] args) {
	  try {
               
		ClientRequest request = new ClientRequest(
				"http://localhost:8080/RESTService/rest/jsonServices/send");
		request.accept("application/json");
               String input = "{\"id\":14,\"firstName\":\"Emilio\",\"lastName\":\"Valdivia\",\"age\":20}"; 
	
               request.body("application/json", input);
               
                
		ClientResponse<String> response = request.get(String.class);
           
		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ response.getStatus());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
			new ByteArrayInputStream(response.getEntity().getBytes())));

		String output;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

	  } catch (ClientProtocolException e) {

		e.printStackTrace();

	  } catch (IOException e) {

		e.printStackTrace();

	  } catch (Exception e) {

		e.printStackTrace();

	  }

	}

}
