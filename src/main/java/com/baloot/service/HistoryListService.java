package com.baloot.service;

import com.baloot.model.HistoryList;
import com.baloot.model.User;
import com.baloot.repository.HistoryListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryListService {
    private final HistoryListRepository repo;
    public HistoryListService(HistoryListRepository historyListRepository){
        this.repo = historyListRepository;
    }
    public List<List<Object>> getUserCommodities(User user) {
        return repo.getUserCommodities(user);
    }

    public void save(HistoryList historyList){
        repo.save(historyList);
    }
    public List<HistoryList> findAll(){
        return repo.findAll();
    }
}
