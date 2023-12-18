package com.inditex.hiring.controller.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Use this POJO on the response for brand & partnumber & offer endPoint.
 */
public class OfferByPartNumber implements Serializable {

  private Instant startDate;

  private Instant endDate;

  private BigDecimal price;

  private String currencyIso;

  public OfferByPartNumber() {

  }

  public OfferByPartNumber(Instant startDate, Instant endDate, BigDecimal price, String currencyIso) {

    this.startDate = startDate;
    this.endDate = endDate;
    this.price = price;
    this.currencyIso = currencyIso;
  }

  public Instant getStartDate() {
    return startDate;
  }

  public void setStartDate(Instant startDate) {
    this.startDate = startDate;
  }

  public Instant getEndDate() {
    return endDate;
  }

  public void setEndDate(Instant endDate) {
    this.endDate = endDate;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getCurrencyIso() {
    return currencyIso;
  }

  public void setCurrencyIso(String currencyIso) {
    this.currencyIso = currencyIso;
  }

}