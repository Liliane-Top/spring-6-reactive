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

@RestController
@RequiredArgsConstructor
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v2/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";
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
                .map(savedDTO -> ResponseEntity.created(UriComponentsBuilder
                                .fromHttpUrl("http://localhost:8080/" + CUSTOMER_PATH + "/" + savedDTO.getId())
                                .build().toUri())
                        .build());
    }

    @PutMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> updateCustomer(@PathVariable("customerId") Integer customerId,
                                              @Validated @RequestBody CustomerDTO customerDTO) {
        return customerService.updateCustomer(customerId, customerDTO)
                .map(response -> ResponseEntity.ok().build());//transforming the Mono into a response entity
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> patchCustomer(@PathVariable("customerId") Integer customerId,
                                              @Validated @RequestBody CustomerDTO customerDTO) {
        return customerService.patchCustomer(customerId, customerDTO)
                .map(response -> ResponseEntity.ok().build());//transforming the Mono into a response entity
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteCustomerById(@PathVariable("customerId") Integer customerId) {
        return customerService.deleteCustomerById(customerId)
                .map(response -> ResponseEntity.noContent().build());
    }


}
