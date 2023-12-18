package com.inditex.hiring.service;

import com.inditex.hiring.TestConstants;
import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import com.inditex.hiring.controller.exception.OfferNotFoundException;
import com.inditex.hiring.model.OfferBean;
import com.inditex.hiring.repository.OfferRepository;
import com.inditex.hiring.utils.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DataJpaTest
public class OfferServiceTest {

    @MockBean
    private OfferRepository offerRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Test
    public void testCreateNewOffer() {

        Offer offerDTO = getSampleOfferDTO();
        OfferBean offerEntityBean = modelMapper.map(offerDTO, OfferBean.class);

        when(offerRepository.save(any())).thenReturn(offerEntityBean);

        OfferService offerService = new OfferService(offerRepository, modelMapper);
        OfferBean createdOffer = offerService.createNewOffer(offerDTO);
        Assertions.assertNotNull(createdOffer);
        Assertions.assertEquals(offerDTO.getOfferId(), createdOffer.getOfferId());
    }

    @Test
    public void testDeleteOfferById() {

        OfferService offerService = new OfferService(offerRepository, modelMapper);
        Long testOfferId = new Random().nextLong();
        doNothing().when(offerRepository).deleteById(testOfferId);
        offerService.deleteOfferById(testOfferId);

    }

    @Test
    public void testDeleteAllOffers() {
        OfferService offerService = new OfferService(offerRepository, modelMapper);
        doNothing().when(offerRepository).deleteAll();
        offerService.deleteAllOffers();
    }

    @Test
    public void testGetAllOffers() {
        List<OfferBean> offers = new ArrayList<>();
        offers.add(getSampleOfferBean());
        offers.add(getSampleOfferBean());
        when(offerRepository.findAll()).thenReturn(offers);

        OfferService offerService = new OfferService(offerRepository, modelMapper);
        List<Offer> allOffers = offerService.getAllOffers();
        Assertions.assertTrue(allOffers != null && allOffers.size() == offers.size());
    }

    @Test
    public void testGetTopNOffers() {
        List<OfferBean> offers = new ArrayList<>();
        offers.add(getSampleOfferBean());
        offers.add(getSampleOfferBean());

        Page<OfferBean> offerPage = mock(Page.class);

        int topN = 2;
        Pageable pageable = PageRequest.of(0, topN);
        when(offerRepository.findAll(pageable)).thenReturn(offerPage);
        when(offerPage.getContent()).thenReturn(offers);

        OfferService offerService = new OfferService(offerRepository, modelMapper);
        List<Offer> allOffers = offerService.getTopNOffer(topN);
        Assertions.assertTrue(allOffers != null && allOffers.size() == topN);
    }

    @Test
    public void testGetOffersById() {
        OfferBean testOffer = getSampleOfferBean();
        when(offerRepository.findById(eq(testOffer.getOfferId()))).thenReturn(Optional.empty());

        OfferService offerService = new OfferService(offerRepository, modelMapper);
        try {
            offerService.getOffersById(testOffer.getOfferId());
            Assertions.fail("It supposes to throw exception for an invalid offerId.");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof OfferNotFoundException);
        }

        when(offerRepository.findById(eq(testOffer.getOfferId()))).thenReturn(Optional.of(testOffer));
        Offer returnedOffer = offerService.getOffersById(testOffer.getOfferId());
        Assertions.assertNotNull(returnedOffer);
        Assertions.assertEquals(testOffer.getOfferId(), returnedOffer.getOfferId());
    }

    private Offer getSampleOfferDTO() {
        return new Offer(new Random().nextLong(), TestConstants.BRAND_ID_ZARA, DateUtil.convertToInstant("2020-06-14T00.00.00Z"), DateUtil.convertToInstant("2020-06-14T01.00.00Z"), 1l,
                Long.toString(new Random().nextLong()),
                0, BigDecimal.valueOf(100.99), "EUR");
    }

    private OfferBean getSampleOfferBean() {
        return new OfferBean().offerId(new Random().nextLong()).brandId(TestConstants.BRAND_ID_ZARA)
                .startDate(DateUtil.convertToInstant("2020-06-14T00.00.00Z"))
                .endDate(DateUtil.convertToInstant("2020-06-14T01.00.00Z"))
                .prodPartNumber("011").priceListId(new Random().nextLong())
                .priority(0)
                .price(BigDecimal.valueOf(100.99))
                .currencyIso("EUR");
    }

    @Test
    public void verifySetMethodsForOfferByPartNumber() {
        Instant newStartDate = DateUtil.convertToInstant("2020-06-14T18:30:00Z");
        Instant newEndDate = DateUtil.convertToInstant("2020-06-14T19:30:00Z");
        String newCurrency = "USD";
        BigDecimal newPrice = BigDecimal.valueOf(15.55);
        OfferByPartNumber offerByPartNumber = new OfferByPartNumber(
                DateUtil.convertToInstant("2021-06-14T18:30:00Z"),
                DateUtil.convertToInstant("2021-06-14T18:30:00Z"),
                BigDecimal.valueOf(19.88), "EUR");
        offerByPartNumber.setCurrencyIso(newCurrency);
        Assertions.assertEquals(newCurrency, offerByPartNumber.getCurrencyIso());

        offerByPartNumber.setPrice(newPrice);
        Assertions.assertEquals(newPrice, offerByPartNumber.getPrice());

        offerByPartNumber.setStartDate(newStartDate);
        Assertions.assertEquals(newStartDate, offerByPartNumber.getStartDate());

        offerByPartNumber.setEndDate(newEndDate);
        Assertions.assertEquals(newEndDate, offerByPartNumber.getEndDate());
    }

    @Test
    public void testFindByBrandIdAndPartNumber() {
        Integer brandId = TestConstants.BRAND_ID_ZARA;
        String productPartNumber = "1001001";
        List<OfferBean> mockedOffers = new ArrayList<>();

        BigDecimal normalPrice = BigDecimal.valueOf(100.99);
        BigDecimal discountedPrice = BigDecimal.valueOf(100.99 * 0.88);

        mockedOffers.add(new OfferBean().offerId(new Random().nextLong())
                .brandId(brandId)
                .startDate(DateUtil.convertToInstant("2020-06-14T00:00:00Z"))
                .endDate(DateUtil.convertToInstant("2020-06-30T23:59:59Z"))
                .priceListId(1l)
                .prodPartNumber(productPartNumber)
                .priority(0)
                .price(normalPrice).currencyIso("EUR"));
        mockedOffers.add(new OfferBean().offerId(new Random().nextLong())
                .brandId(brandId)
                .startDate(DateUtil.convertToInstant("2020-06-18T00:00:00Z"))
                .endDate(DateUtil.convertToInstant("2020-06-19T00:00:00Z"))
                .priceListId(1l)
                .prodPartNumber(productPartNumber)
                .priority(1)
                .price(discountedPrice).currencyIso("EUR"));

        when(offerRepository.findByBrandIdAndProductPartnumber(eq(brandId), eq(productPartNumber)))
                .thenReturn(mockedOffers);

        OfferService offerService = new OfferService(offerRepository, modelMapper);
        List<OfferByPartNumber> offerByPartNumbers = offerService.findByBrandIdAndPartNumber(brandId, productPartNumber);
        Assertions.assertEquals(3, offerByPartNumbers.size());

        Assertions.assertEquals(normalPrice, offerByPartNumbers.get(0).getPrice());
        Assertions.assertEquals(discountedPrice, offerByPartNumbers.get(1).getPrice());
        Assertions.assertEquals(normalPrice, offerByPartNumbers.get(2).getPrice());

        Assertions.assertEquals(DateUtil.convertToInstant("2020-06-18T00:00:00Z"), offerByPartNumbers.get(1).getStartDate());

        //no records case
        when(offerRepository.findByBrandIdAndProductPartnumber(eq(brandId), eq(productPartNumber))).thenReturn(new ArrayList<>());
        offerByPartNumbers = offerService.findByBrandIdAndPartNumber(brandId, productPartNumber);
        Assertions.assertTrue(offerByPartNumbers.isEmpty());
    }


}


