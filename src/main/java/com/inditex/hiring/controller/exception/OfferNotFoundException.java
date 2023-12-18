package com.inditex.hiring.controller.exception;

public class OfferNotFoundException extends RuntimeException {
    public OfferNotFoundException(Long id) {
        super("The offer with offer_id " + id + " was not found.");
    }
}