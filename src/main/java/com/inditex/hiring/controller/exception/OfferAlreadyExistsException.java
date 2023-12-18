package com.inditex.hiring.controller.exception;

public class OfferAlreadyExistsException  extends RuntimeException {
    public OfferAlreadyExistsException(Long offerId) {
        super("An offer with id " + offerId + " already exists.");
    }
}