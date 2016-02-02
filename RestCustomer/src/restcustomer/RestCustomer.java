/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcustomer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
/**
 *
 * @author Emilio
 */
public class RestCustomer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	  try {

		ClientRequest request = new ClientRequest(
				"http://localhost:8080/RESTService/rest/jsonServices/print");
		request.accept("application/json");
                
		ClientResponse<String> response = request.get(String.class);

		if (response.getStatus() != 200) {
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
