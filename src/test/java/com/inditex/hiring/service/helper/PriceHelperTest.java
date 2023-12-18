package com.inditex.hiring.service.helper;

import com.inditex.hiring.TestConstants;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import com.inditex.hiring.model.OfferBean;
import com.inditex.hiring.utils.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PriceHelperTest {

    private final PriceHelper helper = new PriceHelper();

    @Test
    public void testGenerateTimeTableWithEmptyOffers() {
        List<OfferBean> offerList = new ArrayList<>();
        List<OfferByPartNumber> timeTablePriceList = helper.generateTimeTable(offerList);
        Assertions.assertTrue(timeTablePriceList.isEmpty());
    }

    @Test
    public void testGenerateTimeTableWithOneOffer() {
        List<OfferBean> offerList = new ArrayList<>();
        offerList.add(newOffer("2020-06-14T00.00.00Z", "2020-06-14T01.00.00Z", 0, BigDecimal.valueOf(100.99)));

        List<OfferByPartNumber> timeTablePriceList = helper.generateTimeTable(offerList);
        Assertions.assertEquals(1, timeTablePriceList.size());
        OfferByPartNumber offerByPartNumber = timeTablePriceList.get(0);
        Assertions.assertEquals(offerByPartNumber.getCurrencyIso(), offerList.get(0).getCurrencyIso());
        Assertions.assertEquals(offerByPartNumber.getStartDate(), offerList.get(0).getStartDate());
        Assertions.assertEquals(offerByPartNumber.getEndDate(), offerList.get(0).getEndDate());
        Assertions.assertEquals(offerByPartNumber.getPrice(), offerList.get(0).getPrice());
    }

    @Test
    public void testGenerateTimeTableWithTwoOffersWithDifferentPriority() {
        BigDecimal normalPrice = BigDecimal.valueOf(100.99);
        BigDecimal discountedPrice = BigDecimal.valueOf(66.99);
        List<OfferBean> offerList = new ArrayList<>();
        offerList.add(newOffer("2020-06-01T00:00:00Z", "2020-06-30T01:00:00Z", 0, normalPrice));
        offerList.add(newOffer("2020-06-18T00:00:00Z", "2020-06-19T00:00:00Z", 1, discountedPrice));

        List<OfferByPartNumber> timeTablePriceList = helper.generateTimeTable(offerList);
        Assertions.assertEquals(3, timeTablePriceList.size());
        OfferByPartNumber offerByPartNumber1 = timeTablePriceList.get(0);
        Assertions.assertEquals(normalPrice, offerByPartNumber1.getPrice());
        OfferByPartNumber offerByPartNumber2 = timeTablePriceList.get(1);
        Assertions.assertEquals(discountedPrice, offerByPartNumber2.getPrice());
        OfferByPartNumber offerByPartNumber3 = timeTablePriceList.get(2);
        Assertions.assertEquals(normalPrice, offerByPartNumber3.getPrice());
    }

    @Test
    public void testGenerateTimeTableWithTheCaseInExam() {

        BigDecimal price1 = BigDecimal.valueOf(35.50);
        BigDecimal price2 = BigDecimal.valueOf(25.45);
        BigDecimal price3 = BigDecimal.valueOf(30.50);
        BigDecimal price4 = BigDecimal.valueOf(38.95);

        List<OfferBean> offerList = new ArrayList<>();
        offerList.add(newOffer("2020-06-14T00:00:00Z", "2020-12-31T23:59:59Z", 0, price1));

        offerList.add(newOffer("2020-06-14T15:00:00Z", "2020-06-14T18:30:00Z", 1, price2));
        offerList.add(newOffer("2020-06-15T00:00:00Z", "2020-06-15T11:00:00Z", 1, price3));
        offerList.add(newOffer("2020-06-15T16:00:00Z", "2020-12-31T23:59:59Z", 1, price4));

        List<OfferByPartNumber> timeTablePriceList = helper.generateTimeTable(offerList);
        Assertions.assertEquals(6, timeTablePriceList.size());
        Assertions.assertEquals(price1, timeTablePriceList.get(0).getPrice());
        Assertions.assertEquals(price2, timeTablePriceList.get(1).getPrice());
        Assertions.assertEquals(price1, timeTablePriceList.get(2).getPrice());
        Assertions.assertEquals(price3, timeTablePriceList.get(3).getPrice());
        Assertions.assertEquals(price1, timeTablePriceList.get(4).getPrice());
        Assertions.assertEquals(price4, timeTablePriceList.get(5).getPrice());
    }

    @Test
    public void testGenerateTimeTableWithNeedToMergeCase() {

        BigDecimal price1 = BigDecimal.valueOf(35.50);
        BigDecimal price2 = BigDecimal.valueOf(25.45);
        BigDecimal price3 = BigDecimal.valueOf(30.50);

        List<OfferBean> offerList = new ArrayList<>();
        offerList.add(newOffer("2020-06-14T00:00:00Z", "2020-12-31T23:59:59Z", 0, price1));

        offerList.add(newOffer("2020-06-14T15:00:00Z", "2020-06-14T18:30:00Z", 1, price2));
        offerList.add(newOffer("2020-06-14T18:30:00Z", "2020-06-15T11:00:00Z", 1, price2));
        offerList.add(newOffer("2020-06-15T11:00:00Z", "2020-12-31T23:59:59Z", 1, price3));

        List<OfferByPartNumber> timeTablePriceList = helper.generateTimeTable(offerList);
        Assertions.assertEquals(3, timeTablePriceList.size());
        Assertions.assertEquals(price1, timeTablePriceList.get(0).getPrice());
        Assertions.assertEquals(price2, timeTablePriceList.get(1).getPrice());
        Assertions.assertEquals(price3, timeTablePriceList.get(2).getPrice());
    }

    private OfferBean newOffer(String startDate, String endDate, int priority, BigDecimal price) {
        return new OfferBean().offerId(Math.abs(new Random().nextLong()))
                .brandId(TestConstants.BRAND_ID_ZARA)
                .startDate(DateUtil.convertToInstant(startDate))
                .endDate(DateUtil.convertToInstant(endDate))
                .priceListId(1)
                .prodPartNumber(Long.toString(new Random().nextLong()))
                .priority(priority)
                .price(price)
                .currencyIso("EUR");
    }

}
