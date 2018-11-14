package com.rspace.rspaceimgmetadata.microservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
//@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class ImgMetaDataAcceptanceTest {
	
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void statusTest() {
		String body = this.restTemplate.getForObject("/status", String.class);
		assertEquals("OK",body);
	}

	/**
	 * Tests to insert images of jpg, png and tif format via a Http PUT
	 * Positiv tests:
	 * - insert jpg
	 * - insert png
	 * - insert tif
	 * Tests for each filetype, that an image with the same customerId can get from the database (via get function)
	 *
	 * Negativ tests:
	 * - duplicated insert of the same image (key)
	 * - insert of wrong file format
	 */
	@Test
	public void ImageInsertTest(){
		String testUrl = "/img_metadata/insert";
		HttpMethod httpMethod = HttpMethod.PUT;


		// ** JPG Test ** //

		String filenameTestJPG = "jpg_test.jpg";
		String imageKeyParsJPG = "/cust_test_jpg/uid_test/1000/1";
		HttpStatus JPGTestResponse = performHTTPTestWithFile(testUrl + imageKeyParsJPG, filenameTestJPG, httpMethod);

		// Check if data is really in database
		String checkAfterTestJPG = restTemplate.getForObject( "/img_metadata/get/cust_test_jpg/1000/1", String.class);

		// Expect HTTP-Status: NO_CONTENT because insert operation does not response content.
		assertThat(JPGTestResponse, is(HttpStatus.NO_CONTENT));

		assertThat("Tries to check if data is really in DB. Error may be an get error", checkAfterTestJPG, containsString("cust_test_jpg"));


		// ** PNG Test ** //

		String filenameTestPNG = "png_test.png";
		String imageKeyParsPNG = "/cust_test_png/uid_test/1000/1";
		HttpStatus PNGTestResponse = performHTTPTestWithFile(testUrl + imageKeyParsPNG, filenameTestPNG, httpMethod);

		// Check if data is really in database
		String checkAfterTestPNG = restTemplate.getForObject( "/img_metadata/get/cust_test_png/1000/1", String.class);

		// Expect HTTP NO_CONTENT because insert operation does not response content.
		assertThat(PNGTestResponse, is(HttpStatus.NO_CONTENT));

		assertThat("Tries to check if data is really in DB. Error may be an get error", checkAfterTestPNG, containsString("cust_test_png"));


		// ** TIFF Test ** //

		String filenameTestTIF = "tif_test.tif";
		String imageKeyParsTIF = "/cust_test_tif/uid_test/1000/1";
		HttpStatus TIFTestResponse = performHTTPTestWithFile(testUrl + imageKeyParsTIF, filenameTestTIF, httpMethod);

		// Check if data is really in database
		String checkAfterTestTIF = restTemplate.getForObject( "/img_metadata/get/cust_test_tif/1000/1", String.class);

		// Expect HTTP NO_CONTENT because insert operation does not response content.
		assertThat(TIFTestResponse, is(HttpStatus.NO_CONTENT));

		assertThat("Tries to check if data is really in DB. Error may be an get error", checkAfterTestTIF, containsString("cust_test_tif"));


		// ** Try to insert an already existing image (key) ** //

		String imageKeyParsDuplicate = "/cust_test_duplicate/uid_test/1000/1";
		HttpStatus DuplicateTestResponse1 = performHTTPTestWithFile(testUrl + imageKeyParsDuplicate, filenameTestTIF, httpMethod);
		HttpStatus DuplicateTestResponse2 = performHTTPTestWithFile(testUrl + imageKeyParsDuplicate, filenameTestTIF, httpMethod);

		// Expect HTTP NO_CONTENT for the first and HTTP CONFLICT for the second insert
		assertThat(DuplicateTestResponse1, is(HttpStatus.NO_CONTENT));
		assertThat(DuplicateTestResponse2, is(HttpStatus.CONFLICT));


		// ** Try to insert wrong file format ** //

		String imageKeyParsWrongFile = "/cust_test_duplicate/uid_test/1000/1";
		String filenameWrongFile = "application.properties";

		HttpStatus wrongFileResponse = performHTTPTestWithFile(testUrl + imageKeyParsWrongFile, filenameWrongFile, httpMethod);

		// Expect HTTP UNSUPPORTED_MEDIA_TYPE
		assertThat(wrongFileResponse, is(HttpStatus.UNSUPPORTED_MEDIA_TYPE));
	}

	/**
	 * Tests updating of a image that is in the (pre-defined) test-database
	 * Positiv test:
	 * - Update existing image (key)
	 * - Update not existing image (key) --> inserts the image as new one (with new key)
	 *
	 */
	@Test
	@Sql("/test_image_metadata.sql")
	public void updateTest(){
		String testUrl = "/img_metadata/update";
		HttpMethod httpMethod = HttpMethod.POST;


		// ** Update existing image ** //

		String imageKeyPars1 = "/cust_test_1/uid_test/1000/1";
		String filenameTest1 = "/iptc_meta.jpg";

		String checkBeforeTest1 = restTemplate.getForObject("/img_metadata/get/cust_test_1/1000/1", String.class);
		HttpStatus testResponse1 = performHTTPTestWithFile(testUrl + imageKeyPars1, filenameTest1, httpMethod);
		String checkAfterTest1 = restTemplate.getForObject("/img_metadata/get/cust_test_1/1000/1", String.class);

		// Pre-Condition
		assertThat(checkBeforeTest1, containsString("HUAWEI"));
		assertThat(checkBeforeTest1, not(containsString("City")));

		assertThat(testResponse1, is(HttpStatus.NO_CONTENT));

		// Post-Condition
		assertThat(checkAfterTest1, containsString("City"));
		assertThat(checkAfterTest1, not(containsString("HUAWEI")));


		// ** Update not existing image ** //

		String imageKeyPars2 = "/cust_test_new/uid_test/1000/1";
		String filenameTest2 = "/tif_test.tif";

		String checkBefore2 = restTemplate.getForObject("/img_metadata/get/cust_test_new/1000/1", String.class);
		HttpStatus testResponse2 = performHTTPTestWithFile(testUrl + imageKeyPars2, filenameTest2, httpMethod);
		String checkAfterTest2 = restTemplate.getForObject("/img_metadata/get/cust_test_new/1000/1", String.class);

		// Pre-Condition: cust_test_new should not exists
		assertThat(checkBefore2, not(containsString("cust_test_new")));

		// Should return HTTP-Status NO_CONTENT
		assertThat(testResponse2, is(HttpStatus.NO_CONTENT));

		// Post-Condition - If cust_test_new now exists, it is created.
		assertThat(checkAfterTest2, containsString("cust_test_new"));

	}


	/**
	 * Tests if images can get from the database via http get request
	 * Positiv test:
	 * - Request existing image
	 *
	 * Negativ test:
	 * - Request not existing image
	 */
	@Test
	@Sql("/test_image_metadata.sql")
	public void getTest(){
		String testUrl = "/img_metadata/get";


		// ** Request existing image ** //

		String imageKeyPars1 = "/cust_test_2/1000/1";
		String testBody1 = restTemplate.getForObject( testUrl + imageKeyPars1, String.class);
		assertThat(testBody1, containsString("cust_test_2"));


		// ** Request not existing image ** //

		String imageKeyPars2 = "/cust_test_5/1000/1";
		ResponseEntity<String> testBody2 = restTemplate.getForEntity( testUrl + imageKeyPars2, String.class);
		assertThat(testBody2.getStatusCode(), is(HttpStatus.NOT_FOUND));
	}


	/**
	 * - Changes API-URLs such that all parameters are on the end
	 * - Creates Controller and Service Method for deleting
	 * - Get-Requests: Return the result as one Json Object, which includes all Variables (excpet the db_id)
	 * - Create testcases for insert, update, get and delete methods (as documented in the testcase-document)
	 * - Drops the testdatabase before each test run.
	 */

	/**
	 * Tests of images (keys) can deleted via http delete request
	 * Positiv test:
	 * - delete existing image (key)
	 * Negative test:
	 * - Try to delete not existing image (key)
	 */
	@Test
	@Sql("/test_image_metadata.sql")
	public void deleteTest(){
		String url = "/img_metadata/delete";
		HttpMethod httpMethod = HttpMethod.DELETE;


		// ** Delete existing image (key) ** //
		String imageKeyPars1 = "/cust_test_2/1000/1";
		ResponseEntity<String> deleteResponse1 = restTemplate.exchange(url + imageKeyPars1, httpMethod, null, String.class, "");
		ResponseEntity checkAfterTest2 = restTemplate.getForEntity("/img_metadata/get" + imageKeyPars1, String.class);

		assertThat(deleteResponse1.getStatusCode(), is(HttpStatus.OK));
		// Post-Condition
		assertThat(checkAfterTest2.getStatusCode(), is(HttpStatus.NOT_FOUND));

		// ** Try to delete not existing image (key) ** //
		String imageKeyPars2 = "/cust_test_not_exists/1000/1";
		ResponseEntity<String> deleteResponse2 = restTemplate.exchange(url + imageKeyPars2, httpMethod, null, String.class, "");

		assertThat(deleteResponse2.getStatusCode(), is(HttpStatus.NOT_FOUND));
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
	Todo Testcases:
	Application Tests:
	- Update metadata

	ComponentTests:
	Convert Metadata to hash map
	Convert Metadata to json
	Creating (Parameter) Key lists
	Extracting of Parameters

	 */

}
