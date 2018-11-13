package com.rspace.rspaceimgmetadata.microservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

import static org.junit.Assert.assertEquals;


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
		assertEquals(response.getStatusCode().toString(), HttpStatus.NO_CONTENT.toString());

		//todo: Check if the data is really in the database

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
