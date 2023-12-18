package com.inditex.hiring.service;

import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import com.inditex.hiring.controller.exception.InvalidDateException;
import com.inditex.hiring.controller.exception.OfferAlreadyExistsException;
import com.inditex.hiring.controller.exception.OfferNotFoundException;
import com.inditex.hiring.model.OfferBean;
import com.inditex.hiring.repository.OfferRepository;
import com.inditex.hiring.service.helper.PriceHelper;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferService {

    private final OfferRepository offerRepository;

    private final ModelMapper modelMapper;

    public OfferService(OfferRepository offerRepository, ModelMapper modelMapper) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
    }

    public OfferBean createNewOffer(Offer offer) {
        if (offer.getStartDate().isAfter(offer.getEndDate())) {
            throw new InvalidDateException(offer.getStartDate(), offer.getEndDate());
        }
        if (offerRepository.findById(offer.getOfferId()).isPresent()) {
            throw new OfferAlreadyExistsException(offer.getOfferId());
        }
        return offerRepository.save(modelMapper.map(offer, OfferBean.class));
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll().stream().map(offerBean -> modelMapper.map(offerBean, Offer.class)).collect(Collectors.toList());
    }

    public Long count() {
        return offerRepository.count();
    }

    public Offer getOffersById(Long id) {
        Optional<OfferBean> offerBeanOpl = offerRepository.findById(id);
        if (offerBeanOpl.isEmpty()) {
            throw new OfferNotFoundException(id);
        }
        return modelMapper.map(offerBeanOpl.get(), Offer.class);
    }

    public void deleteOfferById(Long id) {
        offerRepository.deleteById(id);
    }

    public void deleteAllOffers() {
        offerRepository.deleteAll();
    }

    public List<OfferByPartNumber> findByBrandIdAndPartNumber(Integer brandId, String productPartNumber) {
        List<OfferBean> offers = offerRepository.findByBrandIdAndProductPartnumber(brandId, productPartNumber);
        if (offers == null || offers.isEmpty()) {
            return new ArrayList<>();
        }
        return new PriceHelper().generateTimeTable(offers);
    }

    public List<Offer> getTopNOffer(int topN) {
        Pageable pageable = PageRequest.of(0, topN);
        Page<OfferBean> topNOffers = offerRepository.findAll(pageable);
        List<OfferBean> offerBeans = topNOffers.getContent();
        if (offerBeans.isEmpty()) {
            return new ArrayList<>();
        } else {
            return offerBeans.stream().map(offerBean -> modelMapper.map(offerBean, Offer.class)).collect(Collectors.toList());
        }
    }
}
