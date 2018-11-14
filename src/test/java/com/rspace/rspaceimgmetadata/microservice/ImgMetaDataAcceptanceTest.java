package com.rspace.rspaceimgmetadata.microservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

import static org.junit.Assert.assertEquals;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class ImgMetaDataAcceptanceTest {
	
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void statusTest() {
		String body = this.restTemplate.getForObject("/status", String.class);
		assertEquals("OK",body);
	}

	@Test
	public void ImageInsertTest(){
		String url = "/img_metadata/insert/cust_test/uid_test/1000/";
		HttpMethod httpMethod = HttpMethod.PUT;

		// ** JPG Test **
		String filenameTestJPG = "jpg_test.jpg";
		HttpStatus JPGTestResponse = performHTTPTestWithFile(url + "1", filenameTestJPG, httpMethod);

		// Expect HTTP-Status: NO_CONTENT because insert operation does not response content.
		assertEquals(JPGTestResponse, HttpStatus.NO_CONTENT);

		// ** PNG Test **

		String filenameTestPNG = "png_test.png";
		HttpStatus PNGTestResponse = performHTTPTestWithFile(url + "2", filenameTestPNG, httpMethod);

		// Expect HTTP Ok
		assertEquals(PNGTestResponse, HttpStatus.NO_CONTENT);

		// ** TIFF Test **

		String filenameTestTIF = "tif_test.tif";
		HttpStatus TIFTestResponse = performHTTPTestWithFile(url + "3", filenameTestTIF, httpMethod);

		// Expect HTTP Ok
		assertEquals(TIFTestResponse, HttpStatus.NO_CONTENT);

		//todo: Check if the data is really in the database

	}


	/**
	 * Creates HTTP Request with the image and sends to the server.
	 * The image file has to be in the test/resources folder.
	 * @param url Requested URL (including the parameters like customerId etc.)
	 * @param filename Image name including the ending (e.g. .jpg). The file has to be in the test/resources folder.
	 * @param httpMethod
	 * @return The received http status
	 */
	private HttpStatus performHTTPTestWithFile(String url, String filename, HttpMethod httpMethod){
		LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();

		parameters.add("file", new org.springframework.core.io.ClassPathResource(filename));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(parameters, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, httpMethod, entity, String.class, "");

		return response.getStatusCode();
	}



	/*
	Testcases:
	Application Tests:
	- Insert image and test if the result is in db
	- Update metadata
	- Check different File Formats

	- Search operations:

	ComponentTests:
	Convert Metadata to hash map
	Convert Metadata to json


	-------
	Try to insert wrong json into the db
	Try to send wrong file format


	 */

}
