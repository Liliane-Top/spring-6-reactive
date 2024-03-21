package nl.top.spring6reactive.controllers;

import lombok.RequiredArgsConstructor;
import nl.top.spring6reactive.model.CustomerDTO;
import nl.top.spring6reactive.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v2/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";
    public static final String BASE_URL = "http://localhost:8080";
    private final CustomerService customerService;

    @GetMapping(CUSTOMER_PATH)
    Flux<CustomerDTO> listCustomers() {
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    Mono<CustomerDTO> getCustomerById(@PathVariable("customerId") Integer customerId) {
        return customerService.getCustomerById(customerId);
    }

    @PostMapping(CUSTOMER_PATH)
    Mono<ResponseEntity<Void>> createNewCustomer(@Validated @RequestBody CustomerDTO customerDTO) {
        return customerService.saveNewCustomer(customerDTO)
                .map(savedDTO -> ResponseEntity.created(getCustomerUri(savedDTO.getId()))
                        .build());
    }

    private static URI getCustomerUri(Integer customerId) {
        return UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .path(CUSTOMER_PATH)
                .pathSegment(customerId.toString())
                .build()
                .toUri();
    }

    @PutMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> updateCustomer(@PathVariable("customerId") Integer customerId,
                                              @Validated @RequestBody CustomerDTO customerDTO) {
        return customerService.updateCustomer(customerId, customerDTO)
                .map(response -> ResponseEntity.noContent().build());//transforming the Mono into a response entity
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> patchCustomer(@PathVariable("customerId") Integer customerId,
                                              @Validated @RequestBody CustomerDTO customerDTO) {
        return customerService.patchCustomer(customerId, customerDTO)
                .map(response -> ResponseEntity.noContent().build());//transforming the Mono into a response entity
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteCustomerById(@PathVariable("customerId") Integer customerId) {
        return customerService.deleteCustomerById(customerId)
                .thenReturn(ResponseEntity.noContent().build());
    }


}
