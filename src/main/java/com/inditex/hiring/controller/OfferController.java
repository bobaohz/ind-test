package com.inditex.hiring.controller;

import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import com.inditex.hiring.controller.exception.OfferNotFoundException;
import com.inditex.hiring.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * You can change this controller but please do not change ends points signatures & payloads.
 */
@RestController
public class OfferController {

  //thinking of performance: max records could be returned in getAllOffers api
  @Value("${pricing.service.config.maxRecordsPerPage:1000}")
  private int maxRecordsInOneRequest;

  private final OfferService offerService;

  public OfferController(OfferService offerService) {
    this.offerService = offerService;
  }

  @RequestMapping(value = "/offer", method = RequestMethod.POST, consumes = "application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public void createNewOffer(@RequestBody @Valid Offer offer) {
      offerService.createNewOffer(offer);
  }

  @RequestMapping(value = "/offer", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.OK)
  public void deleteAllOffers() {
      offerService.deleteAllOffers();
  }

  @RequestMapping(value = "/offer/{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<HttpStatus> deleteOfferById(@PathVariable Long id) {

    //to check if the offer with given id exist?
    try {
      offerService.getOffersById(id);
    } catch (OfferNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    offerService.deleteOfferById(id);
    return ResponseEntity.ok().build();
  }


  @RequestMapping(value = "/offer", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public List<Offer> getAllOffers() {
    Long countOfOffers = offerService.count();
    if (countOfOffers <= maxRecordsInOneRequest) {
      return offerService.getAllOffers();
    }

    return offerService.getTopNOffer(maxRecordsInOneRequest);
  }

  @RequestMapping(value = "/offer/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler(OfferNotFoundException.class)
  public ResponseEntity<Offer> getOfferById(@PathVariable Long id) {

    try {
      Offer offer = offerService.getOffersById(id);
      return new ResponseEntity<>(offer, HttpStatus.OK);
    } catch (OfferNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(value = "brand/{brandId}/partnumber/{partnumber}/offer", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public List<OfferByPartNumber> getOfferByPartNumber(@PathVariable Integer brandId, @PathVariable String partnumber) {
    return offerService.findByBrandIdAndPartNumber(brandId, partnumber);
  }
}
