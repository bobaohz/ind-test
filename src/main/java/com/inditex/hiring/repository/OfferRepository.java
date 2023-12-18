package com.inditex.hiring.repository;

import com.inditex.hiring.model.OfferBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<OfferBean, Long> {

    List<OfferBean> findByBrandIdAndProductPartnumber(Integer brandId, String productPartnumber);
}