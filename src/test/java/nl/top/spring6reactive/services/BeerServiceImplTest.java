package nl.top.spring6reactive.services;

import nl.top.spring6reactive.model.BeerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BeerServiceImplTest {

    @Autowired
    private BeerService beerService;

    @Test
    void listBeers() {
        Flux<BeerDTO> beerDTOFlux = beerService.listBeers();
        StepVerifier
                .create(beerDTOFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void getBeerById() {
        Mono<BeerDTO> beerDTOMono = beerService.getBeerById(2);
        StepVerifier
                .create(beerDTOMono)
                .consumeNextWith(beerResponse -> {
                    assertEquals("Crank", beerResponse.getBeerName());
                })
                .verifyComplete();

    }
}