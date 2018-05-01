package controller;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

import siahaan.com.example.belajarci.BelajarCiApplication;
import siahaan.com.example.belajarci.entity.Product;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BelajarCiApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = { "/mysql/delete-data.sql", "/mysql/sample-product.sql" })
public class ProductControllerTests {

	private static final String BASE_URL = "/api/product";

	@LocalServerPort
	int serverPort;

	@Before
	public void setup() {
		RestAssured.port = serverPort;
	}

	@Test
	public void testSave() {
		Product p = new Product();
		p.setCode("PT-001");
		p.setName("Product Test 001");
		p.setPrice(BigDecimal.valueOf(102000.02));
		given().body(p).contentType(ContentType.JSON).when().post(BASE_URL + "/").then().statusCode(201)
				.header("Location", containsString(BASE_URL + "/")).log().headers();

		// name is undefined
		Product p1 = new Product();
		p1.setCode("P-001");
		given().body(p1).contentType(ContentType.JSON).when().post(BASE_URL + "/").then().statusCode(400);

		// code is less than 3
		Product p2 = new Product();
		p2.setCode("PT");
		p2.setName("Product Test");
		p2.setPrice(BigDecimal.valueOf(100));
		given().body(p2).contentType(ContentType.JSON).when().post(BASE_URL + "/").then().statusCode(400);

		// price negative
		Product p3 = new Product();
		p3.setCode("P-001");
		p3.setName("Product Test 001");
		p3.setPrice(new BigDecimal(-100));
		given().body(p3).contentType(ContentType.JSON).when().post(BASE_URL + "/").then().statusCode(400);
	}

	@Test
	public void testFindAll() {
		get(BASE_URL + "/").then().body("totalElements", equalTo(1)).body("content.id", hasItem("abc123"));
	}

	@Test
	public void testFindById() {
		// found
		get(BASE_URL + "/abc123").then().statusCode(200).body("id", equalTo("abc123")).body("code", equalTo("P-001"));
		// not found
		get(BASE_URL + "/1234").then().statusCode(404);
	}

	@Test
	public void testUpdate() {
		Product obj = new Product();
		obj.setCode("PX-009");
		obj.setName("Product 909");
		obj.setPrice(BigDecimal.valueOf(2000));

		given().body(obj).contentType(ContentType.JSON).when().put(BASE_URL + "/abc123").then().statusCode(200);

		get(BASE_URL + "/abc123").then().statusCode(200).body("code", equalTo("PX-009")).body("name",
				equalTo("Product 909"));

	}

	@Test
	public void testDelete() {
		delete(BASE_URL + "/abc123").then().statusCode(200);

		get(BASE_URL + "/abc123").then().statusCode(404);
	}
}
