package com.rspace.rspaceimgmetadata.microservice;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
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

import java.security.PublicKey;


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

		ResponseEntity<String> response = restTemplate.exchange("/img_metadata/custTest847/uid_test/10043/2/insert", HttpMethod.PUT, entity, String.class, "");


		// Expect HTTP Ok
		assertEquals(response.getStatusCode().toString(), HttpStatus.OK.toString());

		//todo: Check if the data is really in the database

	}

	//todo: Only perliminary testcase!! (the correct data has to be in the database)

	/**
	 * Sends a POST-Request with a search Term and a Keyset. Expected that the json search result is part of the answer.
	 * Condition: The Test images have to be in the test Database (todo)
	 */
	@Test
	public void searchPrefixInKeysTest(){
		// The Keyset for the search, which is send in the post request.
		String jsonKeySet = "[\"Flash\", \"ColorSpace\"]";
		String searchTerm = "1";
		String expectedJson = "{\"id\": null, \"metadata\": [{\"$.ColorSpace\": \"1\"}], \"customer_id\": \"cust847\", \"image_version\": 2, \"rspace_image_id\": 10043}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(jsonKeySet, headers);

		ResponseEntity<String> response = restTemplate.exchange("/img_metadata//search/prefix/inKeys/" + searchTerm, HttpMethod.POST, entity, String.class, "");
		String body = response.getBody();

		//todo: problems with the escape chars
		//assertThat( response.toString(), containsString(expectedJson));
		assertThat( body, containsString("cust847"));
		assertThat( body, containsString("10043"));
		assertThat( body, containsString("2"));
	}

	/**
	 *
	 */
	@Test
	public void searchPrefixTest(){
		//Searches a term that should return nothing
		String emptyBody = this.restTemplate.getForObject("/img_metadata/search/xyz", String.class);
		assertThat(emptyBody, is("[]"));

		//Searches a term that should return results
		String body = this.restTemplate.getForObject("/img_metadata/search/EVA", String.class);
		assertThat(body, containsString("EVA-L09"));
		assertThat(body, containsString("custTest847"));

		//todo: Answers that should ne returned
	}


	@Test
	public void searchPrefixInKeysOfUsersTest(){
		String jsonParameters = "{\"keys\":[\"Model\"], \"users\":[\"uid_test\"]}";
		String searchTerm = "EVA";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(jsonParameters, headers);

		ResponseEntity<String> response = restTemplate.exchange("/img_metadata//search/prefix/inKeys/ofUsers/" + searchTerm, HttpMethod.POST, entity, String.class, "");
		String body = response.getBody();

		assertThat(body, containsString("EVA-L09"));
		assertThat(body, containsString("uid_test"));
		assertThat(body, not("uid1"));
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
