package project.px.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.px.entity.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ProductEditForm {
    private Long id;

    @NotBlank(message = "Cannot be blank")
    private String name;

    @NotNull(message = "Cannot be null")
    @Min(1)
    private Integer price;

    @NotNull(message = "Cannot be null")
    @Min(1)
    private Integer expirationDayPeriod;

    @NotNull(message = "Cannot be null")
    @Min(1)
    private Integer bigBox;

    @Min(1)
    private Integer smallBox;

    @NotNull(message = "Cannot be null")
    private ProductCompany productCompany;

    @NotNull(message = "Cannot be null")
    private TransportCompany transportCompany;

    @NotNull(message = "Cannot be null")
    private Category category;

    @NotNull(message = "Cannot be null")
    private ContractStatus contractStatus;

    @NotNull(message = "Cannot be null")
    private DemandStatus demandStatus;

    @NotNull(message = "Cannot be null")
    private ProductLevel productLevel;


}
