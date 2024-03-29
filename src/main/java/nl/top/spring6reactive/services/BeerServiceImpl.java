package nl.top.spring6reactive.services;

import lombok.RequiredArgsConstructor;
import nl.top.spring6reactive.mappers.BeerMapper;
import nl.top.spring6reactive.model.BeerDTO;
import nl.top.spring6reactive.repositories.BeerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;


    @Override
    public Flux<BeerDTO> listBeers() {
        return beerRepository.findAll()
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> getBeerById(Integer id) {
        return beerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> saveNewBeer(BeerDTO beerDTO) {
       return beerRepository.save(beerMapper.beerDTOtoBeer(beerDTO))//we're saving the beer object
               .map(beerMapper::beerToBeerDTO);//but we return a beerDTO object
    }

    @Override
    public Mono<BeerDTO> updateBeer(Integer beerId, BeerDTO beerDTO) {
        return beerRepository.findById(beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(beerFound ->
                {
                    beerFound.setBeerName(beerDTO.getBeerName());
                    beerFound.setBeerStyle(beerDTO.getBeerStyle());
                    beerFound.setPrice(beerDTO.getPrice());
                    beerFound.setUpc(beerDTO.getUpc());
                    beerFound.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    return beerFound;//now the beer that was returned is now updated with new values
                }).flatMap(beerRepository::save)//why flatmap I do not understand save the updated version to the DB
                .map(beerMapper::beerToBeerDTO);//the beer object mapped to a beerDTO which will be returned as monotype

    }

    @Override
    public Mono<BeerDTO> patchBeer(Integer beerId, BeerDTO beerDTO) {
        return beerRepository.findById(beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Beer with ID " + beerId + " not found")))
                .map(beerFound ->
                {
                    if(StringUtils.hasText(beerDTO.getBeerName())){
                        beerFound.setBeerName(beerDTO.getBeerName());
                    }
                    if(beerDTO.getBeerStyle() != null) {
                        beerFound.setBeerStyle(beerDTO.getBeerStyle());
                    }
                    if(beerDTO.getPrice() != null) {
                        beerFound.setPrice(beerDTO.getPrice());
                    }
                    if(beerDTO.getQuantityOnHand() != null) {
                        beerFound.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    }
                    if(StringUtils.hasText(beerDTO.getUpc())) {
                        beerFound.setUpc(beerDTO.getUpc());
                    }
                    return beerFound;
                } ).flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<Void> deleteBeerById(Integer beerId) {
        return beerRepository.findById(beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(foundCustomer ->beerRepository.deleteById(beerId));
    }
}
