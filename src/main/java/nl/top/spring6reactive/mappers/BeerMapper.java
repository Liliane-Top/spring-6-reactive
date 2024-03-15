package nl.top.spring6reactive.mappers;

import nl.top.spring6reactive.domain.Beer;
import nl.top.spring6reactive.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDTOtoBeer(BeerDTO dto);
    BeerDTO beerToBeerDTO(Beer beer);
}
