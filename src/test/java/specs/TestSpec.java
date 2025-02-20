package specs;


import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;

public class TestSpec {

    public static RequestSpecification addRequestSpec = with()
            .filter(withCustomTemplates())
            .log().all()
            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
            .basePath("/addproducttocart/catalog/45/1/1");



    public static RequestSpecification ddaddRequestSpec = with()
            .filter(withCustomTemplates())
            .log().all()
            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
            .basePath("/addproducttocart/catalog/45/1/1");


    public static ResponseSpecification addResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(STATUS)
            .log(BODY)
            .build();











}