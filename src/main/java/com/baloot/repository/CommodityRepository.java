package com.baloot.repository;

import com.baloot.model.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommodityRepository extends JpaRepository<Commodity, Integer>{
    @Query("SELECT ca.type FROM Commodity c JOIN c.categories ca WHERE c.id = ?1")
    Set<String> getCategoriesForCommodity(int commodityId);

    @Query(value = "select c from Commodity c where c.name LIKE %:name% ")
    List<Commodity> filterByName(@Param("name") String name);

    @Query(value = "SELECT c FROM Commodity c JOIN c.categories cc  WHERE cc.type LIKE %:category%")
    List<Commodity> filterByCategory(@Param("category") String category);

    @Query(value = "SELECT c FROM Commodity c JOIN FETCH c.provider p WHERE p.name LIKE %:name%")
    List<Commodity> filterByProviderName(@Param("name") String name);

    @Query(value = "SELECT c1 FROM Commodity c1,Commodity c2 WHERE c2.id=:id ORDER BY c1.rating desc limit 4")
    List<Commodity>recommenderSystem(@Param("id") int commodityId);

    @Query(value = "UPDATE Commodity SET rating=:score WHERE id=:id")
    void rateCommodity(@Param("id") Integer commodityId,@Param("score") int score);

    @Query("SELECT c FROM Commodity c ORDER BY c.name ASC")
    List<Commodity> sortByName();

    @Query("SELECT c FROM Commodity c ORDER BY c.price ASC")
    List<Commodity> sortByPrice();

    @Query("SELECT c FROM Commodity c where c.name LIKE %:name%  ORDER BY c.name ASC")
    List<Commodity> filterByNameSortByName(@Param("name") String name);

    @Query("SELECT c FROM Commodity c where c.name LIKE %:name%  ORDER BY c.price ASC")
    List<Commodity> filterByNameSortByPrice(@Param("name") String name);

    @Query(value= "SELECT c FROM Commodity c JOIN c.categories cc  WHERE cc.type LIKE %:category% ORDER BY c.name ASC")
    List<Commodity> filterByCategorySortByName(@Param("category") String category);

    @Query(value= "SELECT c FROM Commodity c JOIN c.categories cc  WHERE cc.type LIKE %:category% ORDER BY c.price ASC")
    List<Commodity> filterByCategorySortByPrice(@Param("category") String category);

    @Query(value = "SELECT c FROM Commodity c JOIN FETCH c.provider p WHERE p.name LIKE %:name% ORDER BY c.name ASC")
    List<Commodity> filterByProviderNameSortByName(@Param("name") String name);

    @Query(value = "SELECT c FROM Commodity c JOIN FETCH c.provider p WHERE p.name LIKE %:name% ORDER BY c.price ASC")
    List<Commodity> filterByProviderNameSortByPrice(@Param("name") String name);
}
