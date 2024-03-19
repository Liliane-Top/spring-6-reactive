package nl.top.spring6reactive.controllers;

import nl.top.spring6reactive.domain.BeerStyle;
import nl.top.spring6reactive.model.BeerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerControllerTest {

    @Autowired
    BeerController beerController;

    @Test
    void listBeer() {
        Flux<BeerDTO> beerDTOFlux = beerController.listBeer();
        StepVerifier
                .create(beerDTOFlux)
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void getBeerById() {
        Mono<BeerDTO> beerDTOMono = beerController.getBeerById(3);
        StepVerifier
                .create(beerDTOMono)
                .consumeNextWith(beerDTO ->assertEquals("Sunshine City", beerDTO.getBeerName()))
                .verifyComplete();
    }


    @Test
    void createNewBeer() {
        var newBeer = beerController.createNewBeer(getTestBeer());
        StepVerifier
                .create(newBeer)
                .consumeNextWith(responseEntity -> {
                    assertNotNull(responseEntity.getHeaders().getLocation());
                    assertTrue(responseEntity.getHeaders().getLocation().getPath().endsWith("4"));
                    assertTrue(responseEntity.getHeaders().getLocation().getPath().contains("/api/v2/beer"));
                })
                .verifyComplete();
    }

    private BeerDTO getTestBeer(){
        return BeerDTO.builder()
                .beerName("Space Dust")
                .beerStyle(BeerStyle.IPA)
                .price(new BigDecimal(10))
                .quantityOnHand(256)
                .upc("ipa")
                .build();
    }
}