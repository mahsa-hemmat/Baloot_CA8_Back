package com.baloot.controller;

import com.baloot.exception.ProviderNotFoundException;
import com.baloot.info.AbstractCommodityInfo;
import com.baloot.info.ProviderInfo;
import com.baloot.model.*;
import com.baloot.service.BalootSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/providers")
public class ProviderController {
    private final BalootSystem balootSystem;
    @Autowired
    public ProviderController(BalootSystem balootSystem) {
        this.balootSystem = balootSystem;
    }

    @GetMapping("/{provider_id}")
    public ResponseEntity<Object> getProvider(@PathVariable int provider_id) throws Exception {
        try {
            Provider provider = balootSystem.getProvider(provider_id);
            ProviderInfo providerInfo = new ProviderInfo(provider);
            return ResponseEntity.status(HttpStatus.OK).body(providerInfo);

        } catch (ProviderNotFoundException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{provider_id}/commodities")
    public ResponseEntity<Object> getProviderCommodities(@PathVariable int provider_id) throws ProviderNotFoundException {
        try {
            ArrayList<AbstractCommodityInfo> abstractCommodityInfos = new ArrayList<>();
            for(Commodity commodity : balootSystem.getProvider(provider_id).getCommodities()){
                abstractCommodityInfos.add(new AbstractCommodityInfo(commodity));
            }
            return ResponseEntity.status(HttpStatus.OK).body(abstractCommodityInfos);
        } catch (ProviderNotFoundException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
