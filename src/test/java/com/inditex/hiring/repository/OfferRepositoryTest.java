package com.inditex.hiring.repository;


import com.inditex.hiring.TestConstants;
import com.inditex.hiring.model.OfferBean;
import com.inditex.hiring.utils.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@DataJpaTest
public class OfferRepositoryTest {

    @Autowired
    private JpaRepository<OfferBean, Long> jpaRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Test
    public void testCreateNewOfferAndGetOfferById() {

        String testProdPartNumber = "100100";
        Long testOfferId = 1001L;
        OfferBean offer1 = new OfferBean().offerId(testOfferId).brandId(TestConstants.BRAND_ID_ZARA)
                .startDate(DateUtil.convertToInstant("2020-06-14T00.00.00Z"))
                .endDate(DateUtil.convertToInstant("2020-06-14T01.00.00Z"))
                .priceListId(1l)
                .prodPartNumber(testProdPartNumber)
                .priority(0)
                .price(BigDecimal.valueOf(100.99)).currencyIso("EUR");

        jpaRepository.save(offer1);


        Optional<OfferBean> result = jpaRepository.findById(testOfferId);
        Assertions.assertTrue(result.isPresent());
        OfferBean offerFromDB = result.get();
        compareOfferDetails(offerFromDB, offer1);
    }

    @Test
    public void testFindByBrandIdAndProductPartnumber() {

        String testProdPartNumber = "100100";
        Long testOfferId = 1001L;
        OfferBean offer1 = new OfferBean().offerId(testOfferId).brandId(TestConstants.BRAND_ID_ZARA)
                .startDate(DateUtil.convertToInstant("2020-06-14T00.00.00Z"))
                .endDate(DateUtil.convertToInstant("2020-06-14T01.00.00Z"))
                .priceListId(1l)
                .prodPartNumber(testProdPartNumber)
                .priority(0)
                .price(BigDecimal.valueOf(100.99)).currencyIso("EUR");
        jpaRepository.save(offer1);

        OfferBean offer2 = new OfferBean().offerId(Math.abs(new Random().nextLong()))
                .brandId(TestConstants.BRAND_ID_ZARA)
                .startDate(DateUtil.convertToInstant("2020-06-14T00.00.00Z"))
                .endDate(DateUtil.convertToInstant("2020-06-14T01.00.00Z"))
                .priceListId(1l)
                .prodPartNumber("999000")
                .priority(0)
                .price(BigDecimal.valueOf(100.99)).currencyIso("EUR");

        jpaRepository.save(offer2);


        List<OfferBean> result = offerRepository.findByBrandIdAndProductPartnumber(TestConstants.BRAND_ID_ZARA, testProdPartNumber);
        Assertions.assertEquals(1, result.size());
        OfferBean offerFromDB = result.get(0);
        compareOfferDetails(offerFromDB, offer1);
    }

    private static void compareOfferDetails(OfferBean offerFromDB, OfferBean offer1) {
        Assertions.assertEquals(offerFromDB.getOfferId(), offer1.getOfferId());
        Assertions.assertEquals(offerFromDB.getBrandId(), offer1.getBrandId());
        Assertions.assertEquals(offerFromDB.getStartDate(), offer1.getStartDate());
        Assertions.assertEquals(offerFromDB.getEndDate(), offer1.getEndDate());
        Assertions.assertEquals(offerFromDB.getPrice(), offer1.getPrice());
        Assertions.assertEquals(offerFromDB.getPriceListId(), offer1.getPriceListId());
        Assertions.assertEquals(offerFromDB.getProductPartnumber(), offer1.getProductPartnumber());
        Assertions.assertEquals(offerFromDB.getCurrencyIso(), offer1.getCurrencyIso());
    }
}
