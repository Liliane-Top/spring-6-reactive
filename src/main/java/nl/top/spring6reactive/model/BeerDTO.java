package nl.top.spring6reactive.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.top.spring6reactive.domain.BeerStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDTO {

    //validation is required in the DTO
    private Integer id;
    @Size(min = 3, max = 255)
    private String beerName;
    @NotBlank
    private BeerStyle beerStyle;
    @Size(max = 25)
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
