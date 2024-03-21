package nl.top.spring6reactive.controllers;

import nl.top.spring6reactive.model.BeerDTO;
import nl.top.spring6reactive.model.CustomerDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static nl.top.spring6reactive.controllers.BeerController.BASE_URL;
import static nl.top.spring6reactive.controllers.CustomerController.CUSTOMER_PATH;
import static nl.top.spring6reactive.controllers.CustomerController.CUSTOMER_PATH_ID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
public class CustomerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(1)
    void listCustomer() {
        webTestClient.get().uri(CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    void getCustomerById() {
        webTestClient.get().uri(CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(CustomerDTO.class);
    }

    @Test
    void getCustomerNotExistingID() {
        webTestClient.get().uri(CUSTOMER_PATH_ID, 99)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(2)
    void createNewCustomer() {
        webTestClient.post().uri(CUSTOMER_PATH)
                .body(Mono.just(getTestCustomer()), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location(BASE_URL + CUSTOMER_PATH + "/4");

    }

    @Test
    void createNewCustomerWithBadData() {
        var customer = getTestCustomer();
        customer.setCustomerName("");
        webTestClient.post().uri(CUSTOMER_PATH)
                .body(Mono.just(customer), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    private static CustomerDTO getTestCustomer() {
        return CustomerDTO.builder()
                .customerName("Yo van Gasselt")
                .build();
    }


    @Test
    void updateCustomer() {
        webTestClient.put()
                .uri(CUSTOMER_PATH_ID, 1)
                .body(Mono.just(getTestCustomer()), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void updateCustomerNotFound() {
        webTestClient.put()
                .uri(CUSTOMER_PATH_ID, 99)
                .body(Mono.just(getTestCustomer()), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNotFound();
    }
    @Test
    void updateCustomerWithBadData() {
        var customer = getTestCustomer();
        customer.setCustomerName("");

        webTestClient.put()
                .uri(CUSTOMER_PATH_ID, 99)
                .body(Mono.just(customer), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void patchCustomer() {
        webTestClient.patch()
                .uri(CUSTOMER_PATH_ID, 1)
                .body(Mono.just(getTestCustomer()), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void patchCustomerNotFound() {
        webTestClient.patch()
                .uri(CUSTOMER_PATH_ID, 99)
                .body(Mono.just(getTestCustomer()), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNotFound();
    }
    @Test
    void patchCustomerWithBadData() {
        var customer = getTestCustomer();
        customer.setCustomerName("");

        webTestClient.patch()
                .uri(CUSTOMER_PATH_ID, 1)
                .body(Mono.just(customer), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(10)
    void deleteCustomer() {
        webTestClient.delete()
                .uri(CUSTOMER_PATH_ID, 3)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }
    @Test
    void deleteCustomerNotFound() {
        webTestClient.delete()
                .uri(CUSTOMER_PATH_ID, 99)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNotFound();
    }

    //Alternative way of testing

    @Autowired
    CustomerController customerController;

    @Test
    @Order(4)
    void getCustomerList2() {
        Flux<CustomerDTO> flux = customerController.listCustomers();
        StepVerifier
                .create(flux)
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    void getCustomerById2() {
        Mono<CustomerDTO> mono = customerController.getCustomerById(1);
        StepVerifier
                .create(mono)
                .consumeNextWith(customerDTO -> {
                    assertEquals("Yo van Gasselt", customerDTO.getCustomerName());
                })
                .verifyComplete();
    }

    @Test
    @Order(4)
    void createNewCustomer2() {
        var newCustomer = customerController.createNewCustomer(getTestCustomer());
        StepVerifier
                .create(newCustomer)
                .consumeNextWith(responseEntity -> {
                    assertNotNull(responseEntity.getHeaders().getLocation());
                    assertEquals("/api/v2/customer/5", responseEntity.getHeaders().getLocation().getPath());
                    assertEquals(responseEntity.getStatusCode().value(), 201);
                })
                .verifyComplete();
    }


}
