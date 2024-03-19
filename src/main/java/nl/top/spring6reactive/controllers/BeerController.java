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

@RestController
@RequiredArgsConstructor
public class BeerController {

    public static final String BEER_PATH = "/api/v2/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
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
                .map(savedDTO -> ResponseEntity.created(UriComponentsBuilder
                                .fromHttpUrl("http://localhost:8080/" + BEER_PATH + "/" + savedDTO.getId())
                                .build().toUri())
                        .build());//we need to set the header
    }

    @PutMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> updateBeer(@PathVariable("beerId") Integer beerId,
                                          @Validated @RequestBody BeerDTO beerDTO) {
        return beerService.updateBeer(beerId, beerDTO)
                .map(savedBeerDTO -> ResponseEntity.ok().build());//changing the stream Mono<BeerDTO> into a ResponseEntity
    }

    @PatchMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> patchBeer(@PathVariable("beerId") Integer beerId,
                                         @Validated @RequestBody BeerDTO beerDTO) {
        return beerService.patchBeer(beerId, beerDTO)
                .map(updatedBeerDTO -> ResponseEntity.ok().build());
    }

    @DeleteMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.deleteBeerById(beerId).map(response -> ResponseEntity.noContent().build());
    }
}
