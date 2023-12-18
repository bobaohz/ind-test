package com.inditex.hiring.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.inditex.hiring.TestConstants;
import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import com.inditex.hiring.controller.exception.OfferNotFoundException;
import com.inditex.hiring.service.OfferService;
import com.inditex.hiring.utils.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OfferController.class)
class OfferControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferService offerService;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    static  {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    @Test
    public void testNewOfferThenShouldReturn201() throws Exception {

        String testOfferId = Long.toString(Math.abs(new Random().nextLong()));
        String requestBody = prepareCreateOfferRequest(testOfferId);
        invokeCreateOffer(requestBody);

        Offer mockedOffer = new Offer(Long.valueOf(testOfferId), TestConstants.BRAND_ID_ZARA,
                DateUtil.convertToInstant("2020-06-14T00.00.00Z"),
                DateUtil.convertToInstant("2020-06-14T01.00.00Z"), 1l,
                "0001002",
                0, BigDecimal.valueOf(100.99), "EUR");
        given(offerService.getOffersById(Long.valueOf(testOfferId)))
                .willReturn(mockedOffer);
        MvcResult getOfferResult = mockMvc.perform(get("/offer/" + testOfferId))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = getOfferResult.getResponse().getContentAsString();
        Assertions.assertTrue(responseString.contains(testOfferId));

        //for NOT FOUND case
        given(offerService.getOffersById(Long.valueOf(testOfferId)))
                .willThrow(new OfferNotFoundException(Long.valueOf(testOfferId)));
        mockMvc.perform(get("/offer/" + testOfferId))
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    public void testDeleteOfferById() throws Exception {

        String testOfferId = Long.toString(Math.abs(new Random().nextLong()));
        String requestBody = prepareCreateOfferRequest(testOfferId);
        invokeCreateOffer(requestBody);

        Offer mockedOffer = new Offer(Long.valueOf(testOfferId), TestConstants.BRAND_ID_ZARA,
                DateUtil.convertToInstant("2020-06-14T00.00.00Z"),
                DateUtil.convertToInstant("2020-06-14T01.00.00Z"),
                1l, "0001002",
                0, BigDecimal.valueOf(100.99), "EUR");
        given(offerService.getOffersById(Long.valueOf(testOfferId)))
                .willReturn(mockedOffer);
        mockMvc.perform(delete("/offer/" + testOfferId))
                .andExpect(status().isOk())
                .andReturn();


        //for invalid case
        given(offerService.getOffersById(Long.valueOf(testOfferId)))
                .willThrow(new OfferNotFoundException(Long.valueOf(testOfferId)));
        mockMvc.perform(delete("/offer/" + testOfferId))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testDeleteAllOffers() throws Exception {

        mockMvc.perform(delete("/offer"))
                .andExpect(status().isOk())
                .andReturn();
    }

    private void invokeCreateOffer(String requestBody) throws Exception {
        mockMvc.perform(post("/offer").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();
    }

    private static String prepareCreateOfferRequest(String testOfferId) {
        String requestBody = String.format("{\n" +
                "    \"offerId\": %s,\n" +
                "    \"brandId\": 1,\n" +
                "    \"startDate\": \"2020-06-14T00:00:00Z\",\n" +
                "    \"endDate\": \"2020-12-31T23:59:59Z\",\n" +
                "    \"priceListId\": 1,\n" +
                "    \"productPartnumber\": \"0001002\",\n" +
                "    \"priority\": 0,\n" +
                "    \"price\": 35.50,\n" +
                "    \"currencyIso\": \"EUR\"\n" +
                "}", testOfferId);
        return requestBody;
    }

    @Test
    public void testGetAllOffers() throws Exception {

        Random random = new Random();

        List<Offer> mockedOffers = new ArrayList<>();
        mockedOffers.add(new Offer(random.nextLong(), TestConstants.BRAND_ID_ZARA, DateUtil.convertToInstant("2020-06-14T00:00:00Z"),
                DateUtil.convertToInstant("2020-06-14T01:00:00Z"), 1l,
                "0001002",
                0, BigDecimal.valueOf(100.99), "EUR"));
        mockedOffers.add(new Offer(random.nextLong(), TestConstants.BRAND_ID_ZARA, DateUtil.convertToInstant("2020-06-30T00:00:00Z"),
                DateUtil.convertToInstant("2020-06-30T23:00:00Z"), 1l,
                "0001002",
                0, BigDecimal.valueOf(101.99), "EUR"));

        given(offerService.getAllOffers())
                .willReturn(mockedOffers);
        MvcResult getOfferResult = mockMvc.perform(get("/offer"))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = getOfferResult.getResponse().getContentAsString();
        Assertions.assertTrue(responseString.contains(Long.toString(mockedOffers.get(0).getOfferId())) &&
                responseString.contains(Long.toString(mockedOffers.get(1).getOfferId())));

    }

    @Test
    public void testGetOfferByPartNumber() throws Exception {

        String testCurrency = "EUR";
        List<OfferByPartNumber> mockedOfferByPartNumbers = new ArrayList<>();
        mockedOfferByPartNumbers.add(new OfferByPartNumber(DateUtil.convertToInstant("2020-06-14T00:00:00Z"),
                DateUtil.convertToInstant("2020-06-14T09:59:59Z"), BigDecimal.valueOf(50), testCurrency));
        mockedOfferByPartNumbers.add(new OfferByPartNumber(DateUtil.convertToInstant("2020-06-14T10:00:00Z"),
                DateUtil.convertToInstant("2020-06-14T23:59:59Z"), BigDecimal.valueOf(48), testCurrency));

        String testProductPartNumber = "1001";
        given(offerService.findByBrandIdAndPartNumber(TestConstants.BRAND_ID_ZARA, testProductPartNumber)).willReturn(mockedOfferByPartNumbers);

        String requestUrlOfGetOfferByPartNumber = String.format("/brand/%s/partnumber/%s/offer", TestConstants.BRAND_ID_ZARA, testProductPartNumber);
        MvcResult getOfferResult = mockMvc.perform(get(requestUrlOfGetOfferByPartNumber))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = getOfferResult.getResponse().getContentAsString();
        List<OfferByPartNumber> responseOfferPNList = OBJECT_MAPPER.readValue(responseString,
                new TypeReference<List<OfferByPartNumber>>() {
                });
        Assertions.assertEquals(2, responseOfferPNList.size());

    }


}
