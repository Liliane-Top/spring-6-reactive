package nl.top.spring6reactive.mappers;

import nl.top.spring6reactive.domain.Customer;
import nl.top.spring6reactive.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    CustomerDTO customerToCustomerDTO(Customer customer);

}
