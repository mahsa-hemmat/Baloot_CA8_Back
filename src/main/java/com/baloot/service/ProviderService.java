package com.baloot.service;

import com.baloot.exception.*;
import com.baloot.model.Provider;
import com.baloot.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {

    private final ProviderRepository repo;

    @Autowired
    public ProviderService(ProviderRepository providerRepository) {
        this.repo = providerRepository;
    }

    public Provider getProviderById(Integer id) throws ProviderNotFoundException {
        Optional<Provider> result = repo.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new ProviderNotFoundException(id);
    }

    public void save(Provider provider) {
        repo.save(provider);
    }
    public void saveAll(List<Provider> providers){
        repo.saveAll(providers);
    }

    public List<Provider> findAll() {
        return repo.findAll();
    }
}
