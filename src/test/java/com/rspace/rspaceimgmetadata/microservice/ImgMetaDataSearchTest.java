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
     *
     */
    @Test
    public void searchPrefixTest(){
        //Searches a term that should return nothing
        String emptyBody = this.restTemplate.getForObject("/img_metadata/search/xyz", String.class);
        assertThat(emptyBody, is("[]"));

        //Searches a term that should return results but NOT an image with the id 566
        String body = this.restTemplate.getForObject("/img_metadata/search/EVA", String.class);
        assertThat(body, containsString("EVA-L09"));
        assertThat(body, containsString("custTest847"));

        assertThat(body, not(containsString("566")));
    }

    /**
     * Sends a POST-Request with a search Term and a Keyset. Expected that the json search result is part of the answer.
     */
    @Test
    public void searchPrefixInKeysOfAllUsersTest(){
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


    //todo tbd
    public void searchPrefixInAllKeysOfUsersTest(){}


    /**
     * Checks if a search returns an image that is from the user and contains the searchTerm
     *
     * Check also if:
     * A image from the same user that does not contain the search term will not returned
     *
     * AND
     *
     * A image from another user that does contain the search term will not returned
     *
     */
    @Test
    public void searchPrefixInKeysOfUsersTest(){
        String jsonParametersPOS = "{\"keys\":[\"Model\"], \"users\":[\"uid_test\"]}";
        String searchTermPOS = "EVA";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(jsonParametersPOS, headers);

        ResponseEntity<String> response = restTemplate.exchange("/img_metadata//search/prefix/inKeys/ofUsers/" + searchTermPOS, HttpMethod.POST, entity, String.class, "");
        String bodyPOS = response.getBody();

        assertThat("Tried valied search, needs to find image: custTest847, 10043, 2, (uid_test)", bodyPOS, containsString("EVA-L09"));
        assertThat("Tried valied search, needs to find image: custTest847, 10043, 2, (uid_test)", bodyPOS, containsString("uid_test"));
        assertThat("Tried valied search, needs to find image: custTest847, 10043, 2, (uid_test)", bodyPOS, not(containsString("uid1")));
        assertThat("Tried valied search, needs to find image: custTest847, 10043, 2, (uid_test)", bodyPOS, not(containsString("custTest3")));



        /// Check if images get returned if an empty (not existing) user is set
        String jsonParametersNEG = "{\"keys\":[\"Model\"], \"users\":[\"uid_empty\"]}";
        String searchTermNEG = "EVA";

        headers = new HttpHeaders();
        entity = new HttpEntity<String>(jsonParametersNEG, headers);

        response = restTemplate.exchange("/img_metadata//search/prefix/inKeys/ofUsers/" + searchTermNEG, HttpMethod.POST, entity, String.class, "");
        String bodyNEG = response.getBody();

        assertThat("Tried not existing user",bodyNEG, is("[]"));


        /// Check if images get returned if an empty (not existing) searchTerm is set

        jsonParametersNEG = "{\"keys\":[\"Model\"], \"users\":[\"uid_test\"]}";
        searchTermNEG = "xyz";

        headers = new HttpHeaders();
        entity = new HttpEntity<String>(jsonParametersNEG, headers);

        response = restTemplate.exchange("/img_metadata//search/prefix/inKeys/ofUsers/" + searchTermNEG, HttpMethod.POST, entity, String.class, "");
        bodyNEG = response.getBody();

        assertThat("Tried not existing searchTerm", bodyNEG, is("[]"));

        /// Check if images get returned if an empty (not existing) key is set

        jsonParametersNEG = "{\"keys\":[\"Empty\"], \"users\":[\"uid_test\"]}";
        searchTermNEG = "EVA";

        headers = new HttpHeaders();
        entity = new HttpEntity<String>(jsonParametersNEG, headers);

        response = restTemplate.exchange("/img_metadata//search/prefix/inKeys/ofUsers/" + searchTermNEG, HttpMethod.POST, entity, String.class, "");
        bodyNEG = response.getBody();

        assertThat("Tried not existing key", bodyNEG, is("[]"));
    }
}
