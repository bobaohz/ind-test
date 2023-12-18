package com.inditex.hiring.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name="OFFER")
@EntityListeners(AuditingEntityListener.class)
public class OfferBean implements Serializable {

    @Id
    @Column(name = "OFFER_ID", length = 50, nullable = false, unique = true)
    private Long offerId;

    @Column(name = "BRAND_ID", nullable = false)
    private Integer brandId;

    @Column(name = "START_DATE", length = 25, nullable = false)
    private Instant startDate;

    @Column(name = "END_DATE", length = 25, nullable = false)
    private Instant endDate;

    @Column(name = "PRICE_LIST", nullable = false)
    private Long priceListId;

    @Column(name = "PARTNUMBER", length = 25, nullable = false)
    private String productPartnumber;

    @Column(name = "PRIORITY", nullable = false)
    private Integer priority;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Column(name = "CURR", length = 3, nullable = false)
    private String currencyIso;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @Version
    private int version;

    public OfferBean() {

    }

    public OfferBean offerId(Long offerId){
        this.offerId = offerId;
        return this;
    }

    public OfferBean brandId(int brandId){
        this.brandId = brandId;
        return this;
    }

    public OfferBean startDate(Instant startDate){
        this.startDate = startDate;
        return this;
    }

    public OfferBean endDate(Instant endDate){
        this.endDate = endDate;
        return this;
    }

    public OfferBean priceListId(long priceListId){
        this.priceListId = priceListId;
        return this;
    }

    public OfferBean prodPartNumber(String prodPartNumber){
        this.productPartnumber = prodPartNumber;
        return this;
    }

    public OfferBean priority(int priority){
        this.priority = priority;
        return this;
    }

    public OfferBean price(BigDecimal price){
        this.price = price;
        return this;
    }
    public OfferBean currencyIso(String currencyIso){
        this.currencyIso = currencyIso;
        return this;
    }

}