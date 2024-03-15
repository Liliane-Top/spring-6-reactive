package nl.top.spring6reactive.repositories;

import nl.top.spring6reactive.domain.Beer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BeerRepository extends ReactiveCrudRepository<Beer, Integer> {
}
