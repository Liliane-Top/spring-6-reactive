package nl.top.spring6reactive.repositories;

import nl.top.spring6reactive.domain.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
}
