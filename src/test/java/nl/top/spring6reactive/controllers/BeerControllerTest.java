package nl.top.spring6reactive.controllers;

import nl.top.spring6reactive.domain.BeerStyle;
import nl.top.spring6reactive.model.BeerDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static nl.top.spring6reactive.controllers.BeerController.*;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(2)
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(1)
    void listBeer() {
        webTestClient.get().uri(BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    void getBeerById() {
        webTestClient.get().uri(BEER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(BeerDTO.class);
    }

    @Test
    void getBeerByNonExistingId() {
        webTestClient.get().uri(BEER_PATH_ID, 10)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createNewBeer() {
        webTestClient.post().uri(BEER_PATH)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location(BASE_URL + BEER_PATH + "/4");

    }

    @Test
    void createNewBeerWithBadData() {
        BeerDTO testBeer = getTestBeer();
        testBeer.setBeerName("");

        webTestClient.post().uri(BEER_PATH)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    private static BeerDTO getTestBeer(){
        return BeerDTO.builder()
                .beerName("Space Dust")
                .beerStyle(BeerStyle.IPA)
                .price(new BigDecimal(10))
                .quantityOnHand(256)
                .upc("ipa")
                .build();
    }


    @Test
    void updateBeer(){
        BeerDTO testBeer = getTestBeer();
        testBeer.setBeerName("");

        webTestClient.put()
                .uri(BEER_PATH_ID, 1)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    void patchBeer(){
        webTestClient.patch()
                .uri(BEER_PATH_ID, 1)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    void patchBeerWithBadData(){
        BeerDTO testBeer = getTestBeer();
        testBeer.setBeerName("");

        webTestClient.patch()
                .uri(BEER_PATH_ID, 1)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void patchBeerWithIdNotFound(){
        webTestClient.patch()
                .uri(BEER_PATH_ID, 99)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateBeerWithBeerNotFound(){
        webTestClient.put()
                .uri(BEER_PATH_ID, 99)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void deleteBeer() {
        webTestClient.delete()
                .uri(BEER_PATH_ID, 3)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteBeerWithNonExistingId() {
        webTestClient.delete()
                .uri(BEER_PATH_ID, 99)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNotFound();
    }

}