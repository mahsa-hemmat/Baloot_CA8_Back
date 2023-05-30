package com.baloot.repository;

import com.baloot.model.BuyList;
import com.baloot.model.Commodity;
import com.baloot.model.User;
import com.baloot.model.id.BuyListId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyListRepository extends JpaRepository<BuyList, BuyListId> {
    @Query(value = "SELECT c.id,c.name,c.price,c.rating,c.inStock,c.provider.id, c.image, bl.inCart FROM BuyList bl JOIN bl.commodity c WHERE bl.user = ?1")
    List<List<Object>>  getUserCommodities(User user);

    @Query(value = "SELECT bl.cost FROM BuyList bl JOIN bl.commodity c WHERE bl.user = ?1")
    List<Integer> findUserBuyListCosts(User user);

    @Query(value = "SELECT bl FROM BuyList bl WHERE bl.user = ?1 AND bl.commodity = ?2")
    BuyList findUserBuyListById(User user, Commodity commodity);

    @Query(value = "SELECT bl FROM BuyList bl WHERE bl.user = ?1")
    List<BuyList> findUserBuyListById(User user);
    @Query(value = "SELECT c.inStock FROM BuyList bl JOIN bl.commodity c WHERE bl.user = ?1 AND bl.commodity = ?2")
    Integer findUserBuyListInStock(User user, Commodity commodity);
}
