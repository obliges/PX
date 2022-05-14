package project.px.entity;

import lombok.*;
import project.px.dto.ProductAddForm;
import project.px.dto.ProductDto;
import project.px.dto.ProductEditForm;


import javax.persistence.*;

@Entity
@Table(name = "product")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    private String name;

    private Integer price;

    private Integer expirationDayPeriod;

    private Integer bigBox;

    private Integer smallBox;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_company_id")
    private ProductCompany productCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_transport_id")
    private TransportCompany transportCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;

    @Enumerated(EnumType.STRING)
    private DemandStatus demandStatus;

    @Enumerated(EnumType.STRING)
    private ProductLevel productLevel;

    public Product(String name) {
        this.name = name;
    }

    public Product(String name,
                   Integer price,
                   Integer expirationDayPeriod,
                   Integer bigBox,
                   Integer smallBox,
                   ProductCompany productCompany,
                   TransportCompany transportCompany,
                   Category category,
                   ContractStatus contractStatus,
                   DemandStatus demandStatus,
                   ProductLevel productLevel) {
        this.name = name;
        this.price = price;
        this.expirationDayPeriod = expirationDayPeriod;
        this.bigBox = bigBox;
        this.smallBox = smallBox;
        this.productCompany = productCompany;
        this.transportCompany = transportCompany;
        this.category = category;
        this.contractStatus = contractStatus;
        this.demandStatus = demandStatus;
        this.productLevel = productLevel;
    }

    public static Product dtoToProduct(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setExpirationDayPeriod(productDto.getExpirationDayPeriod());
        product.setBigBox(productDto.getBigBox());
        product.setSmallBox(productDto.getSmallBox());
        product.setProductCompany(productDto.getProductCompany());
        product.setTransportCompany(productDto.getTransportCompany());
        product.setCategory(productDto.getCategory());
        product.setContractStatus(productDto.getContractStatus());
        product.setDemandStatus(productDto.getDemandStatus());
        product.setProductLevel(productDto.getProductLevel());
        return product;
    }

    public static Product dtoToProduct(ProductAddForm productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setExpirationDayPeriod(productDto.getExpirationDayPeriod());
        product.setBigBox(productDto.getBigBox());
        product.setSmallBox(productDto.getSmallBox());
        product.setProductCompany(productDto.getProductCompany());
        product.setTransportCompany(productDto.getTransportCompany());
        product.setCategory(productDto.getCategory());
        product.setContractStatus(productDto.getContractStatus());
        product.setDemandStatus(productDto.getDemandStatus());
        product.setProductLevel(productDto.getProductLevel());
        return product;
    }

    public void update(ProductEditForm form) {
        this.name = form.getName();
        this.price = form.getPrice();
        this.expirationDayPeriod = form.getExpirationDayPeriod();
        this.bigBox = form.getBigBox();
        this.smallBox = form.getSmallBox();
        this.productCompany = form.getProductCompany();
        this.transportCompany = form.getTransportCompany();
        this.category = form.getCategory();
        this.contractStatus = form.getContractStatus();
        this.demandStatus = form.getDemandStatus();
        this.productLevel = form.getProductLevel();
    }

    public static ProductDto productToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setExpirationDayPeriod(product.getExpirationDayPeriod());
        productDto.setBigBox(product.getBigBox());
        productDto.setSmallBox(product.getSmallBox());
        productDto.setProductCompany(product.getProductCompany());
        productDto.setTransportCompany(product.getTransportCompany());
        productDto.setCategory(product.getCategory());
        productDto.setContractStatus(product.getContractStatus());
        productDto.setDemandStatus(product.getDemandStatus());
        productDto.setProductLevel(product.getProductLevel());
        return productDto;
    }


}
