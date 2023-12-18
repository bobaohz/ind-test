package com.inditex.hiring.service.helper;

import com.inditex.hiring.controller.dto.OfferByPartNumber;
import com.inditex.hiring.model.OfferBean;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PriceHelper {

    public List<OfferByPartNumber> generateTimeTable(List<OfferBean> offerList) {
        //handle simple case firstly
        if (offerList == null || offerList.isEmpty()) {
            return new ArrayList<>();
        }

        //one offer case
        if (offerList.size() == 1) {
            return offerList.stream().map(offer ->
                    new OfferByPartNumber(offer.getStartDate(), offer.getEndDate(), offer.getPrice(), offer.getCurrencyIso())).collect(Collectors.toList());
        }

        //to handle cases with time overlap
        List<Instant> allDateList = new ArrayList<>();

        //get all dates from start_date and end_date
        offerList.stream().forEach(offer -> {
            allDateList.add(offer.getStartDate());
            allDateList.add(offer.getEndDate());
        });

        //sort the dates
        allDateList.sort(Comparator.comparing(Instant::toEpochMilli));

        //get the period list (start_date, end_date)
        List<PricingPeriod> periods = splitIntoPeriods(allDateList);

        //for each period, to apply right price plan based on time window and priority
        periods.stream().forEach(period -> setApplicableOffer(period, offerList));

        //for some case, may need to merge some neighbouring periods with same price
        List<PricingPeriod> mergedPeriods = mergePeriodsWithSamePrice(periods);

        //convert to OfferByPartNumber
        return mergedPeriods.stream().map(PricingPeriod::toOfferByPartNumber).collect(Collectors.toList());
    }

    private List<PricingPeriod> mergePeriodsWithSamePrice(List<PricingPeriod> periods) {
        List<PricingPeriod> mergedPeriods = new ArrayList<>();
        PricingPeriod previousPeriod = null;
        for (PricingPeriod currentPeriod : periods) {
            if (currentPeriod.getAppliedOffer() == null) {//no applicable offer case
                continue;
            }
            if (previousPeriod == null) {
                mergedPeriods.add(currentPeriod);
                previousPeriod = currentPeriod;
                continue;
            }
            if (isSamePrice(previousPeriod.getAppliedOffer().getPrice(), currentPeriod.getAppliedOffer().getPrice())) {
                previousPeriod.setEndDate(currentPeriod.getEndDate());
            } else {//a new price
                mergedPeriods.add(currentPeriod);
                previousPeriod = currentPeriod;
            }
        }
        return mergedPeriods;
    }

    private boolean isSamePrice(BigDecimal price1, BigDecimal price2) {
        return Math.abs(price1.doubleValue() - price2.doubleValue()) < 1e-3;
    }

    /**
     * if any offers are applicable for current period.
     * In case multiple offers are applicable, pickup the one with highest priority
     * @param period
     * @param offerList
     */
    private void setApplicableOffer(PricingPeriod period, List<OfferBean> offerList) {
        offerList.stream().forEach(currentOffer -> {
            OfferBean appliedOffer = period.getAppliedOffer();
            if (appliedOffer != null) {
                //applied offer's priority is higher => nothing to do
                if (appliedOffer.getPriority() > currentOffer.getPriority()) {
                    return;
                }
            }

            Instant offerStartDate = currentOffer.getStartDate();
            Instant offerEndDate = currentOffer.getEndDate();
            //offer's start date and end date are not applicable for current period
            if (!isPeriodApplicable(period, offerStartDate, offerEndDate)) {
                return;
            }

            period.setAppliedOffer(currentOffer);
        });
    }

    private static boolean isPeriodApplicable(PricingPeriod period, Instant offerStartDate, Instant offerEndDate) {
        return offerStartDate.getEpochSecond() <= period.getStartDate().getEpochSecond() &&
                offerEndDate.getEpochSecond() >= period.getEndDate().getEpochSecond();
    }

    private List<PricingPeriod> splitIntoPeriods(List<Instant> sortedDateList) {
        List<PricingPeriod> periods = new ArrayList<>();
        //the first one
        Instant startInstant = sortedDateList.get(0);
        for (int i = 1; i < sortedDateList.size(); i++) {
            Instant currentInstant = sortedDateList.get(i);
            if (startInstant.isBefore(currentInstant)) {
                periods.add(new PricingPeriod(startInstant, currentInstant));
                startInstant = currentInstant;
            }
        }
        return periods;
    }

}
