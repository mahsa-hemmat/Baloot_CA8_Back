package com.baloot.service;

import com.baloot.exception.CommodityNotFoundException;
import com.baloot.model.Commodity;
import com.baloot.repository.CommodityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CommodityService {
    private final CommodityRepository repo;
    @Autowired
    public CommodityService(CommodityRepository commodityRepository) {
        this.repo = commodityRepository;
    }
    public Commodity getCommodityById(Integer id) throws CommodityNotFoundException {
        Optional<Commodity> result = repo.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new CommodityNotFoundException(id);
    }
    public List<Commodity> getCommodities(){
        return repo.findAll();
    }
    //    public List<Commodity> filterByCategory(String category){
//        return repo.f
//    }
    public List<Commodity> filterByName(String name){
        return repo.filterByName(name);
    }
    public List<Commodity> filterByCategory(String category){
        return repo.filterByCategory(category);
    }
    public List<Commodity> filterByProviderName(String name){
        return repo.filterByProviderName(name);
    }
    public void save(Commodity commodity) {
        repo.save(commodity);
    }

    public Set<String> getCategoriesForCommodity(int commodity_id){
        return repo.getCategoriesForCommodity(commodity_id);
    }

    public List<Commodity> recommenderSystem(int commodityId) {
        return repo.recommenderSystem(commodityId);
    }

    public void rateCommodity(Integer commodityId, int score) {
        repo.rateCommodity(commodityId,score);
    }

    public List<Commodity> sortByName() {
        return repo.sortByName();
    }

    public List<Commodity> sortByPrice() {
        return repo.sortByPrice();
    }

    public List<Commodity> filterByNameSortByName(String keyword) {
        return repo.filterByNameSortByName(keyword);
    }

    public List<Commodity> filterByNameSortByPrice(String keyword) {
        return repo.filterByNameSortByPrice(keyword);
    }

    public List<Commodity> filterByCategorySortByName(String keyword) {
        return repo.filterByCategorySortByName(keyword);
    }

    public List<Commodity> filterByCategorySortByPrice(String keyword) {
        return repo.filterByCategorySortByPrice(keyword);
    }

    public List<Commodity> filterByProviderNameSortByName(String keyword) {
        return repo.filterByProviderNameSortByName(keyword);
    }

    public List<Commodity> filterByProviderNameSortByPrice(String keyword) {
        return repo.filterByProviderNameSortByPrice(keyword);
    }
}
