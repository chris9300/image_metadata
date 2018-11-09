package com.rspace.rspaceimgmetadata.microservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Sql("/test_image_metadata.sql")
public class ImgMetaDataSearchTest {

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Sends a POST-Request with a search Term and a Keyset. Expected that the json search result is part of the answer.
     */
    @Test
    public void searchPrefixInKeysTest(){
        // The Keyset for the search, which is send in the post request.
        String jsonKeySet = "[\"City\"]";
        String searchTerm = "City";
        //String expectedJson = "{\"id\": null, \"metadata\": [{\"$.ColorSpace\": \"1\"}], \"customer_id\": \"cust847\", \"image_version\": 2, \"rspace_image_id\": 10043}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(jsonKeySet, headers);

        ResponseEntity<String> response = restTemplate.exchange("/img_metadata//search/prefix/inKeys/" + searchTerm, HttpMethod.POST, entity, String.class, "");
        String body = response.getBody();

        //Should be the only answer image:
        assertThat( body, containsString("custTest2"));
        assertThat( body, containsString("566"));
        assertThat( body, containsString("1"));

        //Should not be in the answer images
        assertThat(body,not(containsString("custTest847")));
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


    /**
     *
     */
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
}
