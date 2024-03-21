package nl.top.spring6reactive.controllers;

import lombok.RequiredArgsConstructor;
import nl.top.spring6reactive.model.BeerDTO;
import nl.top.spring6reactive.services.BeerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class BeerController {

    public static final String BEER_PATH = "/api/v2/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
    public static final String BASE_URL = "http://localhost:8080";

    private final BeerService beerService;

    @GetMapping(BEER_PATH)
    Flux<BeerDTO> listBeer() {
        return beerService.listBeers();
    }

    @GetMapping(BEER_PATH_ID)
//if pathvariable name is exact match of the name it is optional
    Mono<BeerDTO> getBeerById(@PathVariable("beerId") Integer beerId) {//re return a mono as we expect only 1 beer
        return beerService.getBeerById(beerId);
    }

    @PostMapping(BEER_PATH)
    Mono<ResponseEntity<Void>> createNewBeer(@Validated @RequestBody BeerDTO beerDTO) {
        return beerService.saveNewBeer(beerDTO) //it's been to the process of persistence and returns a savedBeerDTO
                .map(savedDTO -> ResponseEntity.created(getUri(savedDTO.getId()))
                        .build());//we need to set the header
    }

    private static URI getUri(Integer beerId) {
        return UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .path(BEER_PATH)
                .pathSegment(beerId.toString())
                .build()
                .toUri();
    }

    @PutMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> updateBeer(@PathVariable("beerId") Integer beerId,
                                          @Validated @RequestBody BeerDTO beerDTO) {
        return beerService.updateBeer(beerId, beerDTO)
                .map(savedBeerDTO -> ResponseEntity.noContent().build());//changing the stream Mono<BeerDTO> into a ResponseEntity
    }

    @PatchMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> patchBeer(@PathVariable("beerId") Integer beerId,
                                         @Validated @RequestBody BeerDTO beerDTO) {
        return beerService.patchBeer(beerId, beerDTO)
                .map(updatedBeerDTO -> ResponseEntity.ok().build());
    }

    @DeleteMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.deleteBeerById(beerId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
