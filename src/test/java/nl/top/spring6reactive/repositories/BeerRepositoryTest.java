package nl.top.spring6reactive.repositories;

import nl.top.spring6reactive.config.DatabaseConfig;
import nl.top.spring6reactive.domain.Beer;
import nl.top.spring6reactive.domain.BeerStyle;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

@DataR2dbcTest
@Import(DatabaseConfig.class)//to import the auditing which creates the Date objects
    @Order(4)
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    private static void print(Beer beer) {
        System.out.println(beer.toString());
    }

    Beer getTestBeer(){
        return Beer.builder()
                .beerName("Space Dust")
                .beerStyle(BeerStyle.IPA)
                .price(new BigDecimal(10))
                .quantityOnHand(256)
                .upc("ipa")
                .build();
    }

    @Test
    void saveNewBeer(){
        beerRepository.save(getTestBeer())
                .subscribe(BeerRepositoryTest::print);
    }

}