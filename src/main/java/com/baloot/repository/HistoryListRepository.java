package com.baloot.repository;

import com.baloot.model.HistoryList;
import com.baloot.model.User;
import com.baloot.model.id.HistoryListId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryListRepository extends JpaRepository<HistoryList, HistoryListId> {
    @Query(value = "SELECT c.id,c.name,c.price,c.rating,c.inStock,c.provider.id, c.image, hl.quantity FROM HistoryList hl JOIN hl.commodity c WHERE hl.user = ?1")
    List<List<Object>>  getUserCommodities(User user);
}
