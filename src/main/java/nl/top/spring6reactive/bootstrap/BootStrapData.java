package nl.top.spring6reactive.bootstrap;

import lombok.RequiredArgsConstructor;
import nl.top.spring6reactive.domain.Beer;
import nl.top.spring6reactive.domain.BeerStyle;
import nl.top.spring6reactive.repositories.BeerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BootStrapData implements CommandLineRunner {
    private final BeerRepository beerRepository;

    @Override
    public void run(String... args)  {
        loadBeerData();

        beerRepository.count().subscribe(count -> {
            System.out.println("Count is :" + count);
        });
    }

    private void loadBeerData() {
        beerRepository.count().subscribe(count -> {
            if (count == 0) {
                Beer beer1 = Beer.builder()
                        .beerName("Galaxy Cat")
                        .beerStyle(BeerStyle.PALE_ALE)
                        .upc("12356")
                        .price(new BigDecimal("12.99"))
                        .quantityOnHand(122)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer2 = Beer.builder()
                        .beerName("Crank")
                        .beerStyle(BeerStyle.PALE_ALE)
                        .upc("12356222")
                        .price(new BigDecimal("11.99"))
                        .quantityOnHand(392)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer3 = Beer.builder()
                        .beerName("Sunshine City")
                        .beerStyle(BeerStyle.IPA)
                        .upc("12356")
                        .price(new BigDecimal("13.99"))
                        .quantityOnHand(144)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                beerRepository.save(beer1).subscribe();//it is required to subscribe to every save as it returns a mono which we have to subscribe to do persist the data
                beerRepository.save(beer2).subscribe();//each save operation returns a mono which needs to be subscribed to persist the data
                beerRepository.save(beer3).subscribe();
            }
        });
    }
}
