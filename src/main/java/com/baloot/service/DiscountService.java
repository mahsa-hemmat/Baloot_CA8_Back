package com.baloot.service;

import com.baloot.exception.InvalidDiscountException;
import com.baloot.model.Discount;
import com.baloot.model.User;
import com.baloot.model.UserDiscount;
import com.baloot.model.id.UserDiscountId;
import com.baloot.repository.DiscountRepository;
import com.baloot.repository.UserDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {
    private final DiscountRepository repo;
    private final UserDiscountRepository udRepo;
    @Autowired
    public DiscountService(DiscountRepository discountRepository, UserDiscountRepository udRepo){
        this.repo = discountRepository;
        this.udRepo = udRepo;
    }
    public Discount getDiscountById(String discount_code) throws InvalidDiscountException {
        Optional<Discount> discount = repo.findById(discount_code);
        if(discount.isPresent()){
            return discount.get();
        }
        throw new InvalidDiscountException(discount_code);
    }
    public void save(Discount discount){
        repo.save(discount);
    }
    public void saveAll(List<Discount> discount){
        repo.saveAll(discount);
    }
    public List<Discount> findAll(){
        return repo.findAll();
    }

    public boolean isDiscountUsedByUser(User user, Discount discount){
        System.out.println(udRepo.isDiscountUsedByUser(user, discount));
        return udRepo.isDiscountUsedByUser(user, discount);
    }
    public UserDiscount findById(UserDiscountId userDiscountId){
        Optional<UserDiscount> userDiscount = udRepo.findById(userDiscountId);
        return userDiscount.orElse(null);
    }

    public void saveUserDiscount(UserDiscount userDiscount) {
        udRepo.save(userDiscount);
    }
}
