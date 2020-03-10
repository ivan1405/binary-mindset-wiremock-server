package com.binary.mindset.wiremock;

import com.binary.mindset.wiremock.runner.WiremockRunner;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.binary.mindset.wiremock.runner.WiremockRunner.DEFAULT_STUB_MAPPINGS_PATTERN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class WiremockAppTest {

    private WiremockRunner wiremockRunner;

    private String wiremockPort;

    @Before
    public void setup() {
        wiremockRunner = new WiremockRunner(DEFAULT_STUB_MAPPINGS_PATTERN);
        wiremockPort = "9090";
        String[] args = {wiremockPort};
        wiremockRunner.run(args);
    }

    @Test
    public void testGetProjectsMock_should_return_200() {
        given()
                .when()
                .get("http://localhost:" + wiremockPort + "/tasklist-management/api/projects")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("", hasSize(2))
                // first project
                .body("get(0).projectId", equalTo(1))
                .body("get(0).title", equalTo("My awesome project 1"))
                .body("get(0).description", equalTo("This is the first project created with the best api"))
                // second project
                .body("get(1).projectId", equalTo(2))
                .body("get(1).title", equalTo("My second project"))
                .body("get(1).description", equalTo("Another awesome project"));
    }

    @After
    public void finish() {
        wiremockRunner.stop();
    }
}
