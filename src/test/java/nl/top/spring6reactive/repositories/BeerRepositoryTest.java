package nl.top.spring6reactive.repositories;

import nl.top.spring6reactive.config.DatabaseConfig;
import nl.top.spring6reactive.domain.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@DataR2dbcTest
@Import(DatabaseConfig.class)//to import the auditing which creates the Date objects
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    Beer getTestBeer(){
        return Beer.builder()
                .beerName("Space Dust")
                .beerStyle("IPA")
                .price(new BigDecimal(10))
                .quantityOnHand(256)
                .upc("ipa")
                .build();
    }

    @Test
    void saveNewBeer(){
        beerRepository.save(getTestBeer())
                .subscribe(beer -> {
                    System.out.println(beer.toString());
                });
    }

}