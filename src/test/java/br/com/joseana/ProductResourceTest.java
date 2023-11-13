package br.com.joseana;

import java.time.LocalDate;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;



@QuarkusTest
class ProductResourceTest {

    @Test
    public void testListAll() {
        given()
                .when().get("/product")
                .then()
                .statusCode(200)
                .body("", hasSize(greaterThan(0)));
    }

    @Test
    public void testGetById() {
        Long existingProductId = 1L;

        Response response = given()
                .accept("application/json")
                .expect().defaultParser(Parser.JSON)
                .when().get("/product/{id}", existingProductId);

        if (response.getContentType() != null && response.getContentType().equalsIgnoreCase("application/json")) {
            response.then()
                    .statusCode(200)
                    .body("id", equalTo(existingProductId.intValue()));
        } else {
            System.out.println("Resposta vazia");
        }
    }

    @Test
    public void testValid() {
        given()
                .when().get("/product/valid")
                .then()
                .statusCode(200)
                .body("", hasSize(greaterThan(0)));
    }

    @Test
    public void testCreate() {
        LocalDate expiryDate = LocalDate.now().plusMonths(1);

        Product newProduct = new Product();
        newProduct.name = "Nova Fruta";
        newProduct.quantity = 10;
        newProduct.description = "Fruta de teste";
        newProduct.expiry_date = expiryDate;

    }

    // @Test
    // public void testDelete() {
    // Long existingProductId = 1L;
    // given()
    // .when().delete("/product/{id}", existingProductId)
    // .then()
    // .statusCode(204);
    // given()
    // .when().get("/product/{id}", existingProductId)
    // .then()
    // .statusCode(404)
    // .body(emptyOrNullString());
    // }  

}