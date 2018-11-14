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
     * Positiv test:
     * - Searches an (exact) existing Term
     * Negative tests:
     * - Searches existing prefix
     * - Searches not existing term
     */
    @Test
    public void searchTermTest(){
        String testUrl = "/img_metadata/search/";

        //Searches an existing Term
        String searchTerm1 = "EVA-L09";
        String testBody1 = this.restTemplate.getForObject( testUrl + searchTerm1, String.class);
        assertThat(testBody1, containsString("EVA-L09"));
        assertThat(testBody1, containsString("cust_test_1"));
        assertThat(testBody1, containsString("cust_test_4"));

        assertThat(testBody1, not(containsString("cust_test_2")));
        assertThat(testBody1, not(containsString("cust_test_3")));

        String searchTerm2 = "eva-l09";  // Searches for same search tag, but know in lower cases
        String testBody2 = this.restTemplate.getForObject( testUrl + searchTerm2, String.class);

        assertThat(testBody2, containsString("EVA-L09"));  // the matched string should be in original cases
        assertThat(testBody1, containsString("cust_test_1"));
        assertThat(testBody1, containsString("cust_test_4"));

        assertThat(testBody1, not(containsString("cust_test_2")));
        assertThat(testBody1, not(containsString("cust_test_3")));

        //Searches an existing prefix, should not return anything
        String searchTerm3 = "EVA";
        String testBody3 = this.restTemplate.getForObject(testUrl + searchTerm3, String.class);
        assertThat(testBody3, is("[]"));

        //Searches a term that should return nothing
        String searchTerm4 = "xyz";
        String testBody4 = this.restTemplate.getForObject(testUrl + searchTerm4, String.class);
        assertThat(testBody4, is("[]"));
    }


    /**
     * Positiv test:
     * - Search for a exact term of a particular key
     *
     * Negativ tests:
     * - Search for a exact term wich is in another key
     * - Search for a prefix of a term of the selected key
     */
    @Test
    public void searchTermInKeysOfAllUsersTest(){
        String testUrl = "/img_metadata/search/inKeys/";
        String jsonKeySet = "[\"City\"]";
        String searchTerm1 = "City (Core) (ref2017.1)";
        //String expectedJson = "{\"id\": null, \"metadata\": [{\"$.ColorSpace\": \"1\"}], \"customer_id\": \"cust847\", \"image_version\": 2, \"rspace_image_id\": 10043}";

        String testBody1 = performSearchTestWithParameter(searchTerm1, jsonKeySet, testUrl);

        //Should be the only answer image:
        assertThat(testBody1, containsString("City (Core) (ref2017.1)"));
        assertThat( testBody1, containsString("cust_test_2"));
        assertThat( testBody1, containsString("cust_test_3"));

        //Should not be in the answer images
        assertThat(testBody1,not(containsString("cust_test_1")));
        assertThat(testBody1,not(containsString("cust_test_4")));

        // Use several keys in the json key array
        String jsonKeySet2 = "[\"Model\", \"City\"]";
        String searchTerm2 = "City (Core) (ref2017.1)";
        //String expectedJson = "{\"id\": null, \"metadata\": [{\"$.ColorSpace\": \"1\"}], \"customer_id\": \"cust847\", \"image_version\": 2, \"rspace_image_id\": 10043}";

        String testBody2 = performSearchTestWithParameter(searchTerm1, jsonKeySet, testUrl);

        //Should be the only answer image:
        assertThat(testBody2, containsString("City (Core) (ref2017.1)"));
        assertThat(testBody2, containsString("cust_test_2"));
        assertThat(testBody2, containsString("cust_test_3"));

        //Should not be in the answer images
        assertThat(testBody2,not(containsString("cust_test_1")));
        assertThat(testBody2,not(containsString("cust_test_4")));


        // Negativ Test: Check that "EVA", which is in other images on other keys, should not return any images
        String searchTerm3 = "EVA-L09";
        String testBody3 = performSearchTestWithParameter(searchTerm3, jsonKeySet, testUrl);

        assertThat(testBody3, is("[]"));

        // Negativ Test: Check that "City", which is only a prefix, should not return any images
        String searchTerm4 = "City";
        String testBody4 = performSearchTestWithParameter(searchTerm4, jsonKeySet, testUrl);

        assertThat(testBody4, is("[]"));
    }

    /**
     * Positive test:
     * - Searches for exact term of particular user
     *
     * Negative tests:
     * - Searches for a term that only exists by other (not selected) users
     * - Searches for a prefix, that contains the selected user
     */
    @Test
    public void searchTermInAllKeysOfUsersTest(){
        String testUrl = "/img_metadata/search/ofUsers/";
        String jsonUserSet1 = "[\"uid_test_3\"]";
        String searchTerm1 = "City (Core) (ref2017.1)";

        String bodyTest1 = performSearchTestWithParameter(searchTerm1, jsonUserSet1, testUrl);

        assertThat(bodyTest1, containsString("City"));
        assertThat(bodyTest1, containsString("cust_test_3"));
        assertThat(bodyTest1, not(containsString("cust_test_1")));
        assertThat(bodyTest1, not(containsString("cust_test_2")));
        assertThat(bodyTest1, not(containsString("cust_test_4")));

        // Use several user_ids
        String jsonUserSet2 = "[\"uid_test_2\", \"uid_test_3\"]";
        String searchTerm2 = "City (Core) (ref2017.1)";

        String bodyTest2 = performSearchTestWithParameter(searchTerm2, jsonUserSet2, testUrl);

        assertThat(bodyTest2, containsString("City"));
        assertThat(bodyTest2, containsString("cust_test_3"));
        assertThat(bodyTest2, containsString("cust_test_2"));
        assertThat(bodyTest2, not(containsString("cust_test_1")));
        assertThat(bodyTest2, not(containsString("cust_test_4")));

        // Search term that only exists for other users
        String jsonUserSet3 = "[\"uid_test_1\"]";
        String searchTerm3 = "City (Core) (ref2017.1)";
        String bodyTest3 = performSearchTestWithParameter(searchTerm3, jsonUserSet3, testUrl);

        assertThat(bodyTest3, is("[]"));

        // Search prefix that exists for selected user
        String jsonUserSet4 = "[\"uid_test_3\"]";
        String searchTerm4 = "City";
        String bodyTest4 = performSearchTestWithParameter(searchTerm4, jsonUserSet4, testUrl);

        assertThat(bodyTest4, is("[]"));
    }

    @Test
    public void searchTermInKeysOfUsersTest(){
        String testUrl = "/img_metadata/search/inKeys/ofUsers/";
        String jsonParameter1 = "{\"keys\":[\"Model\"], \"users\":[\"uid_test_3\"]}";
        String searchTerm1 = "EVA-L09";

        String bodyTest1 = performSearchTestWithParameter(searchTerm1, jsonParameter1, testUrl);

        assertThat( bodyTest1, containsString("EVA-L09"));
        assertThat( bodyTest1, containsString("cust_test_4"));

        assertThat( bodyTest1, not(containsString("cust_test_1")));
        assertThat( bodyTest1, not(containsString("cust_test_2")));
        assertThat( bodyTest1, not(containsString("cust_test_3")));


        /// Check if images get returned if the search term exists for another user

        String jsonParameter2 = "{\"keys\":[\"Model\"], \"users\":[\"uid_test_2\"]}";
        String searchTerm2 = "EVA-L09";

        String bodyTest2 = performSearchTestWithParameter(searchTerm2, jsonParameter2, testUrl);

        assertThat( bodyTest2, is("[]"));

        /// Check if images get returned if the search term exists for the selected user but not in the selected keys

        String jsonParameter3 = "{\"keys\":[\"City\"], \"users\":[\"uid_test_3\"]}";
        String searchTerm3 = "EVA-L09";

        String bodyTest3 = performSearchTestWithParameter(searchTerm3, jsonParameter3, testUrl);

        assertThat( bodyTest3, is("[]"));

        // Check prefix
        String jsonParameter4 = "{\"keys\":[\"Model\"], \"users\":[\"uid_test_3\"]}";
        String searchTerm4 = "EVA";

        String bodyTest4 = performSearchTestWithParameter(searchTerm4, jsonParameter4, testUrl);

        assertThat(bodyTest4, is("[]"));
    }




    // ************** Prefix - Tests *********************

    /**
     *
     */
    @Test
    public void searchPrefixTest(){
        //Searches a term that should return nothing
        String emptyBody = this.restTemplate.getForObject("/img_metadata/search/prefix/xyz", String.class);
        assertThat(emptyBody, is("[]"));

        //Searches a term that should return results but NOT an image with the id 566
        String body = this.restTemplate.getForObject("/img_metadata/search/prefix/EVA", String.class);
        assertThat(body, containsString("EVA"));
        assertThat(body, containsString("cust_test_1"));
        assertThat(body, containsString("cust_test_4"));

        assertThat(body, not(containsString("cust_test_2")));
        assertThat(body, not(containsString("cust_test_3")));
    }

    /**
     * Sends a POST-Request with a search Term and a Keyset. Expected that the json search result is part of the answer.
     */
    @Test
    public void searchPrefixInKeysOfAllUsersTest(){
        String testUrl = "/img_metadata/search/prefix/inKeys/";
        String jsonKeySet = "[\"City\"]";
        String searchTerm = "City";
        //String expectedJson = "{\"id\": null, \"metadata\": [{\"$.ColorSpace\": \"1\"}], \"customer_id\": \"cust847\", \"image_version\": 2, \"rspace_image_id\": 10043}";

        String body = performSearchTestWithParameter(searchTerm, jsonKeySet, testUrl);

        //Should be the only answer image:
        assertThat( body, containsString("cust_test_2"));
        assertThat( body, containsString("cust_test_3"));
        assertThat(body, containsString("City"));

        //Should not be in the answer images
        assertThat(body,not(containsString("cust_test_1")));
        assertThat(body,not(containsString("cust_test_4")));

        // Negativ Test: Check that "EVA", which is in other images on other keys, should not return any images
        String searchTerm2 = "EVA";

        body = performSearchTestWithParameter(searchTerm2, jsonKeySet, testUrl);

        assertThat(body, is("[]"));
    }


    @Test
    public void searchPrefixInAllKeysOfUsersTest(){
        String testUrl = "/img_metadata/search/prefix/ofUsers/";
        String jsonUserSet1 = "[\"uid_test_3\"]";
        String searchTerm1 = "City";

        String bodyTest1 = performSearchTestWithParameter(searchTerm1, jsonUserSet1, testUrl);

        assertThat(bodyTest1, containsString("City"));
        assertThat(bodyTest1, containsString("cust_test_3"));
        assertThat(bodyTest1, not(containsString("cust_test_1")));
        assertThat(bodyTest1, not(containsString("cust_test_2")));
        assertThat(bodyTest1, not(containsString("cust_test_4")));

        String jsonUserSet2 = "[\"uid_test_2\"]";
        String searchTerm2 = "EVA";
        String bodyTest2 = performSearchTestWithParameter(searchTerm2, jsonUserSet2, testUrl);

        assertThat(bodyTest2, is("[]"));
    }


    /**
     * Positive test:
     * Checks if a search returns an image that is from the user and contains the searchTerm
     *
     * Negativ tests:
     * Check also if:
     * - A image from the same user that does not contain the search term will not returned
     * - image from another user that does contain the search term will not returned
     *
     */
    @Test
    public void searchPrefixInKeysOfUsersTest(){
        String testUrl = "/img_metadata/search/prefix/inKeys/ofUsers/";
        String jsonParameter1 = "{\"keys\":[\"Model\"], \"users\":[\"uid_test_3\"]}";
        String searchTerm1 = "EVA";

        String bodyTest1 = performSearchTestWithParameter(searchTerm1, jsonParameter1, testUrl);

        assertThat("Tried valied search, needs to find image: cust_test_4, 1000, 1, (uid_test_3)", bodyTest1, containsString("EVA-L09"));
        assertThat("Tried valied search, needs to find image: cust_test_4, 1000, 1, (uid_test_3)", bodyTest1, containsString("cust_test_4"));

        assertThat("Tried valied search, needs to find image: cust_test_4, 1000, 1, (uid_test_3)", bodyTest1, not(containsString("cust_test_1")));
        assertThat("Tried valied search, needs to find image: cust_test_4, 1000, 1, (uid_test_3)", bodyTest1, not(containsString("cust_test_2")));
        assertThat("Tried valied search, needs to find image: cust_test_4, 1000, 1, (uid_test_3)", bodyTest1, not(containsString("cust_test_3")));


        /// Check if images get returned if an empty (not existing) user is set
        String jsonParameter2 = "{\"keys\":[\"Model\"], \"users\":[\"uid_test_empty\"]}";
        String searchTerm2 = "EVA";

        String bodyTest2 = performSearchTestWithParameter(searchTerm2, jsonParameter2, testUrl);

        assertThat("Tried not existing user",bodyTest2, is("[]"));


        /// Check if images get returned if the search term exists for another user

        String jsonParameter3 = "{\"keys\":[\"Model\"], \"users\":[\"uid_test_2\"]}";
        String searchTerm3 = "EVA";

        String bodyTest3 = performSearchTestWithParameter(searchTerm3, jsonParameter3, testUrl);

        assertThat("Tried not search term that exists only for other user", bodyTest3, is("[]"));

        /// Check if images get returned if the search term exists for the selected user but not in the selected keys

        String jsonParameter4 = "{\"keys\":[\"City\"], \"users\":[\"uid_test_3\"]}";
        String searchTerm4 = "EVA";

        String bodyTest4 = performSearchTestWithParameter(searchTerm4, jsonParameter4, testUrl);

        assertThat("Tried not search term that exists only for other user", bodyTest4, is("[]"));
    }


    private String performSearchTestWithParameter(String searchTerm, String jsonParameter, String url){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(jsonParameter, headers);

        ResponseEntity<String> response = restTemplate.exchange(url + searchTerm, HttpMethod.POST, entity, String.class, "");
        return response.getBody();
    }
}
