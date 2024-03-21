package nl.top.spring6reactive.services;

import nl.top.spring6reactive.domain.BeerStyle;
import nl.top.spring6reactive.model.BeerDTO;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Order(5)
class BeerServiceImplTest {

    @Autowired
    private BeerService beerService;

    @Test
    void listBeers() {
        Flux<BeerDTO> beerDTOFlux = beerService.listBeers();
        StepVerifier
                .create(beerDTOFlux)
                .expectNextCount(4)
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

    @Test
    void createNewBeer(){
        Mono<BeerDTO> beerDTOMono = beerService.saveNewBeer(getTestBeer());
        StepVerifier
                .create(beerDTOMono)
                .consumeNextWith(beerDTO -> {
                    assertEquals("Space Dust", beerDTO.getBeerName());
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