package com.baloot.controller;

import com.baloot.exception.CommodityNotFoundException;
import com.baloot.exception.ProviderNotFoundException;
import com.baloot.exception.ScoreOutOfBoundsException;
import com.baloot.exception.UserNotFoundException;
import com.baloot.info.AbstractCommodityInfo;
import com.baloot.info.CommentInfo;
import com.baloot.info.CommodityInfo;
import com.baloot.model.*;
import com.baloot.service.BalootSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping(value = "/commodities")
public class CommodityController {
    private final BalootSystem balootSystem;

    @Autowired
    public CommodityController(BalootSystem balootSystem) {
        this.balootSystem = balootSystem;
    }

    @GetMapping("")
    public ResponseEntity<Object> getCommodities(@RequestParam(value = "searchType", required=false) Integer searchType,
                                                 @RequestParam(value = "keyword", required=false) String keyword,
                                                 @RequestParam(value = "sortType", required=false) String sort) {
        if (!balootSystem.hasAnyUserLoggedIn())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in. Please login first");
        boolean emptySort = sort == null || sort.isBlank() || sort.isEmpty();
        boolean emptyKeyword = keyword == null || keyword.isBlank() || keyword.isEmpty();
        if (!emptySort && (!sort.equals("name")) && (!sort.equals("price"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid sort parameter.");
        }
        if ((searchType != null && emptyKeyword) || (searchType == null && !emptyKeyword)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("keyword and searchType should be provided together.");
        }
        if (searchType != null && searchType != 1 && searchType != 2 && searchType != 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("invalid search type.");
        }
        ArrayList<AbstractCommodityInfo> abstractCommodityInfos = new ArrayList<>();
        List<Commodity> commodityList = new ArrayList<>();
        if (emptyKeyword && emptySort) {
            commodityList = balootSystem.getCommodities();
        }
        else if(emptyKeyword && !emptySort){
            if(sort.equals("name"))
                commodityList = balootSystem.sortByName();
            else commodityList = balootSystem.sortByPrice();

        }
        else if (!emptyKeyword && emptySort) {
            if (searchType == 1)
                commodityList = balootSystem.filterByName(keyword);
            if (searchType == 2)
                commodityList = balootSystem.filterByCategory(keyword);
            if (searchType == 3)
                commodityList = balootSystem.filterByProviderName(keyword);
        }
        else {
            if (searchType == 1)
                if(sort.equals("name"))
                    commodityList = balootSystem.filterByNameSortByName(keyword);
                else
                    commodityList = balootSystem.filterByNameSortByPrice(keyword);
            if (searchType == 2)
                if(sort.equals("name"))
                    commodityList = balootSystem.filterByCategorySortByName(keyword);
                else
                    commodityList = balootSystem.filterByCategorySortByPrice(keyword);
            if (searchType == 3)
                if(sort.equals("name"))
                    commodityList = balootSystem.filterByProviderNameSortByName(keyword);
                else
                    commodityList = balootSystem.filterByProviderNameSortByPrice(keyword);
        }
        for (Commodity commodity : commodityList) {
            abstractCommodityInfos.add(new AbstractCommodityInfo(commodity));
        }
        return ResponseEntity.status(HttpStatus.OK).body(abstractCommodityInfos);
    }
    @GetMapping("/{commodity_id}")
    public ResponseEntity<Object> getCommodity(@PathVariable int commodity_id) {
        if (!balootSystem.hasAnyUserLoggedIn())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in. Please login first");
        try {
            Commodity co = balootSystem.getCommodity(commodity_id);
            CommodityInfo commodityInfo = new CommodityInfo(co);
            commodityInfo.setProviderName(balootSystem.getProvider(co.getProviderId()).getName());
            commodityInfo.setRatingsCount(co.getRatingCount());
            System.out.println(commodityInfo.getRating());
            return ResponseEntity.status(HttpStatus.OK).body(commodityInfo);
        } catch (CommodityNotFoundException | ProviderNotFoundException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
    @GetMapping("/{commodity_id}/comments")
    public ResponseEntity<Object> getCommodityComments(@PathVariable int commodity_id) {
        if (!balootSystem.hasAnyUserLoggedIn())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in. Please login first");
        try {
            return ResponseEntity.status(HttpStatus.OK).body(balootSystem.getCommodityComments(commodity_id));
        } catch (CommodityNotFoundException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{commodity_id}/recommended")
    public ResponseEntity<Object> getRecommendedCommodities(@PathVariable int commodity_id) {
        if (!balootSystem.hasAnyUserLoggedIn())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in. Please login first");
        try {
            List<Commodity> recommended = balootSystem.recommenderSystem(commodity_id);
            ArrayList<AbstractCommodityInfo> abstractCommodityInfos = new ArrayList<>();
            for(Commodity commodity : recommended){
                abstractCommodityInfos.add(new AbstractCommodityInfo(commodity));
            }
            return ResponseEntity.status(HttpStatus.OK).body(abstractCommodityInfos);
        } catch (CommodityNotFoundException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
    @PostMapping("/{commodity_id}")
    public ResponseEntity<Object> rateCommodity(@PathVariable Integer commodity_id, @RequestParam(value = "score") int score) {
        if (!balootSystem.hasAnyUserLoggedIn())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in. Please login first");
        try {
            balootSystem.rateCommodity(commodity_id, score);
            return ResponseEntity.status(HttpStatus.OK).body("score added successfully.");
        } catch (CommodityNotFoundException | UserNotFoundException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (ScoreOutOfBoundsException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }
}