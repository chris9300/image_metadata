package com.rspace.rspaceimgmetadata.microservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertTrue;

import javafx.application.Application;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
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
		LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();

		// todo: why Paths are not possible here?
		//parameters.add("file", new org.springframework.core.io.ClassPathResource(File.separator + "src"+ File.separator + "tagged_test_image.jpg"));
		parameters.add("file", new org.springframework.core.io.ClassPathResource("tagged_test_image.jpg"));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(parameters, headers);

		ResponseEntity<String> response = restTemplate.exchange("/img_metadata/cust847/10043/2/insert", HttpMethod.PUT, entity, String.class, "");


		// Expect HTTP Ok
		assertEquals(response.getStatusCode().toString(), HttpStatus.OK.toString());

		//todo: Check if the data is really in the database

	}

	//todo: Only perliminary testcase!! (the correct data has to be in the database

	/**
	 * Sends a POST-Request with a search Term and a Keyset. Expected that the json search result is part of the answer.
	 * Condition: The Test images have to be in the test Database (todo)
	 */
	@Test
	public void searchPrefixInTest(){
		// The Keyset for the search, which is send in the post request.
		String jsonKeySet = "[\"$.Flash\", \"$.ColorSpace\"]";
		String searchTerm = "1";
		String expectedJson = "{\"id\": null, \"metadata\": [{\"$.ColorSpace\": \"1\"}], \"customer_id\": \"cust847\", \"image_version\": 2, \"rspace_image_id\": 10043}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(jsonKeySet, headers);

		ResponseEntity<String> response = restTemplate.exchange("/img_metadata//search/prefix/in/" + searchTerm, HttpMethod.POST, entity, String.class, "");

		assertThat( response.toString(), containsString(expectedJson));
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
