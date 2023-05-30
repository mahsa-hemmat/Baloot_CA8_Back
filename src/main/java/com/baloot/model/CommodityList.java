package com.baloot.model;

import com.baloot.exception.*;
import java.util.*;
import java.util.stream.Collectors;

public class CommodityList {
    Map<Integer, Commodity> commodities;
    Map<String, List<String>> filters;

    public Map<Integer, Commodity> getCommodities() {
        return commodities;
    }

    public CommodityList() {
        commodities = new HashMap<>();
        filters = new HashMap<String, List<String>>() {
            {
                put ("name", new ArrayList<>());
                put ("category", new ArrayList<>());
                put ("provider", new ArrayList<>());
            }
        };
    }

    public Commodity getCommodityById(int id) throws CommodityNotFoundException {
        if(!(commodities.containsKey(id)))
            throw new CommodityNotFoundException(id);
        return commodities.get(id);
    }

    public void addCommodity(Commodity co) throws Exception{
        if(!(commodities.containsKey(co.getId())))
            commodities.put(co.getId(), co);
    }

    public void addFilter(String filter, String filterName){
        List<String> temp = new ArrayList<>();
        filter = filter.toLowerCase().trim().replaceAll("\\s+", " ");
        if (filter.contains(","))
            temp = Arrays.asList(filter.trim().split("\\s*,\\s*"));
        for(String key:filters.keySet())
            if(key.equals(filterName))
                if(temp.isEmpty())
                    filters.get(filterName).add(filter);
                else
                    for(String f:temp)
                        filters.get(filterName).add(f);
    }

    public void clearFilter(){
        for(String key:filters.keySet()) {
            filters.get(key).clear();
        }
    }

    /*public List<Commodity> getList(){
        List<Commodity> filteredCommodities = new ArrayList<>();
        if(filters.get("name").isEmpty() && !filters.get("category").isEmpty() && filters.get("provider").isEmpty())
            filteredCommodities.addAll(filterByCategory(filters.get("category")));
        else if(!filters.get("name").isEmpty() && filters.get("category").isEmpty() && filters.get("provider").isEmpty())
            filteredCommodities.addAll(filterByName(filters.get("name")));
        else if(filters.get("name").isEmpty() && filters.get("category").isEmpty() && !filters.get("provider").isEmpty())
            filteredCommodities.addAll(filterByName(filters.get("provider")));
        else if(filters.get("name").isEmpty() && filters.get("category").isEmpty() && !filters.get("provider").isEmpty())
            filteredCommodities.addAll(commodities.values());
        else if(!filters.get("name").isEmpty() && !filters.get("category").isEmpty() && filters.get("provider").isEmpty()) {
            filteredCommodities.addAll(filterByName(filters.get("name")));
            filteredCommodities.retainAll(filterByCategory(filters.get("category")));
        }
        else if(!filters.get("name").isEmpty() && filters.get("category").isEmpty() && !filters.get("provider").isEmpty()) {
            filteredCommodities.addAll(filterByName(filters.get("name")));
            filteredCommodities.retainAll(filterByCategory(filters.get("provider")));
        }
        else if(filters.get("name").isEmpty() && !filters.get("category").isEmpty() && !filters.get("provider").isEmpty()) {
            filteredCommodities.addAll(filterByCategory(filters.get("category")));
            filteredCommodities.retainAll(filterByCategory(filters.get("provider")));
        }
        else {
            filteredCommodities.addAll(filterByName(filters.get("name")));
            filteredCommodities.retainAll(filterByCategory(filters.get("category")));
            filteredCommodities.retainAll(filterByProviderName(filters.get("provider")));
        }
        return filteredCommodities;
    }*/

    public List<Commodity> filterByName(String name) {
        List<String> names = new ArrayList<>();
        name = name.toLowerCase().trim().replaceAll("\\s+", " ");
        if (name.contains(","))
            names = Arrays.asList(name.trim().split("\\s*,\\s*"));
        List<Commodity> allCommodities = new ArrayList<>(commodities.values());
        List<Commodity> nameFiltered = new ArrayList<>();
        if (names.isEmpty()) {
            String finalName = name;
            nameFiltered.addAll(allCommodities.stream()
                    .filter(c -> c.getName().toLowerCase().contains(finalName))
                    .toList());
            return nameFiltered;
        }
        for(int i = 0; i < names.size(); i++) {
            String substring = names.get(i);
            if (i == 0) {
                nameFiltered.addAll(allCommodities.stream()
                        .filter(c -> c.getName().toLowerCase().contains(substring))
                        .collect(Collectors.toList()));
            } else {
                nameFiltered = (nameFiltered.stream()
                        .filter(c -> c.getName().toLowerCase().contains(substring))
                        .collect(Collectors.toList()));
            }
        }
        return nameFiltered;
    }

    public List<Commodity> filterByCategory(String category){
        List<String> cat = new ArrayList<>();
        category = category.toLowerCase().trim().replaceAll("\\s+", " ");
        if (category.contains(","))
            cat = Arrays.asList(category.trim().split("\\s*,\\s*"));
        else
            cat.add(category);
        List<Commodity> list= new ArrayList<>();
        for(Commodity co : commodities.values()) {
            List<Category> tempList = new ArrayList<>(co.getCategories());
            //tempList.replaceAll(String::toLowerCase);
            if (new HashSet<>(tempList).containsAll(cat))
                list.add(co);
        }
        return list;
    }

    public List<Commodity> filterByProviderName(String name) {
        List<String> names = new ArrayList<>();
        name = name.toLowerCase().trim().replaceAll("\\s+", " ");
        if (name.contains(","))
            names = Arrays.asList(name.trim().split("\\s*,\\s*"));
        List<Commodity> allCommodities = new ArrayList<>(commodities.values());
        List<Commodity> nameFiltered = new ArrayList<>();
        if (names.isEmpty()) {
            String finalName = name;
            nameFiltered.addAll(allCommodities.stream()
                    .filter(c -> c.getName().toLowerCase().contains(finalName))
                    .toList());
            return nameFiltered;
        }
        for(int i = 0; i < names.size(); i++) {
            String substring = names.get(i);
            if (i == 0) {
                nameFiltered.addAll(allCommodities.stream()
                        .filter(c -> c.getName().toLowerCase().contains(substring))
                        .collect(Collectors.toList()));
            } else {
                nameFiltered = (nameFiltered.stream()
                        .filter(c -> c.getName().toLowerCase().contains(substring))
                        .collect(Collectors.toList()));
            }
        }
        return nameFiltered;
    }

    public boolean hasCommodity(int id){
        return (commodities.containsKey(id));
    }

    public void voteComment(String id, int vote) throws CommentNotFoundException {
        for(Commodity commodity: commodities.values()){
            /*if(commodity.getComments().containsKey(UUID.fromString(id))){
                commodity.getComments().get(UUID.fromString(id)).addLikeDislike(vote);
                return;
            }*/
        }
        throw new CommentNotFoundException("id");
    }

    public List<Commodity> filterByPrice(int start, int end) {
        List<Commodity> list= new ArrayList<Commodity>();
        for(Commodity co : commodities.values())
            if((co.getPrice()>=start) && (co.getPrice()<end))
                list.add(co);
        return list;
    }

    public void sortByRate() {
        List<Commodity> allCommodities = new ArrayList<>(commodities.values());
        allCommodities.sort(Collections.reverseOrder(Comparator.comparing(Commodity::getRating)));
        Map<Integer, Commodity> sorted = new LinkedHashMap<>();
        for(Commodity co:allCommodities)
            sorted.put(co.getId(), co);
        commodities = sorted;
    }

    public List<Commodity> sortByPrice() {
        List<Commodity> allCommodities = new ArrayList<>(commodities.values());
        allCommodities.sort(Collections.reverseOrder(Comparator.comparing(Commodity::getPrice)));
        return allCommodities;
    }
    public List<Commodity> sortByPrice(List<Commodity> allCommodities) {
        allCommodities.sort(Collections.reverseOrder(Comparator.comparing(Commodity::getPrice)));
        return allCommodities;
    }
    public List<Commodity> sortByName() {
        List<Commodity> allCommodities = new ArrayList<>(commodities.values());
        allCommodities.sort(Comparator.comparing(Commodity::getName).reversed());
        return allCommodities;
    }
    public List<Commodity> sortByName(List<Commodity> allCommodities) {
        allCommodities.sort(Comparator.comparing(Commodity::getName).reversed());
        return allCommodities;
    }

}
