package com.inditex.hiring.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;


/**
 * Use this POJO for offer service end point responses.
 */
@Component
@Data
public class Offer implements Serializable {

  @Positive(
          message = "The offer id must be greater than zero."
  )
  @NotNull(message = "The offer id must be defined.")
  private Long offerId;


  @NotNull(message = "The offer brand must be defined.")
  private Integer brandId;

  @NotNull(message = "The offer start date must be defined.")
  private Instant startDate;

  @NotNull(message = "The offer end date must be defined.")
  private Instant endDate;

  @NotNull(message = "The offer price list must be defined.")
  private Long priceListId;

  @NotNull(message = "The product part number must be defined.")
  private String productPartnumber;

  @NotNull(message = "The offer priority must be defined.")
  private Integer priority;

  @Positive(
          message = "The offer price must be greater than zero."
  )
  private BigDecimal price;

  @NotNull(message = "The offer currency must be defined.")
  private String currencyIso;


  public Offer() {

  }

  public Offer(Long offerId, Integer brandId, Instant startDate, Instant endDate, Long priceListId, String productPartnumber,
      Integer priority, BigDecimal price, String currencyIso) {

    this.offerId = offerId;
    this.brandId = brandId;
    this.startDate = startDate;
    this.endDate = endDate;
    this.priceListId = priceListId;
    this.productPartnumber = productPartnumber;
    this.priority = priority;
    this.price = price;
    this.currencyIso = currencyIso;
  }


}