package com.github.iauglov.mariya.demo.service;


import com.github.iauglov.mariya.demo.models.Purchase;
import com.github.iauglov.mariya.demo.models.User;
import lombok.val;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserService {
    private final Map<Integer, User> userMap = new HashMap<>();

    public User getUser(int userId) {
        return userMap.get(userId);
    }

    public void addUser(int userId) {
        userMap.put(userId, new User());
    }

    public void setupUserPreiod(int userId, int periodDates) {
        userMap.get(userId).setPeriod(periodDates);
    }

    public void setupUserBudget(int userId, BigDecimal budget) {
        userMap.get(userId).setBudget(budget);
    }

    public String getLastUserState(int userId) {
        return userMap.get(userId).getLastState();
    }

    public void setLastUserState(int userId, String lastState) {
        userMap.get(userId).setLastState(lastState);
    }

    public boolean userInited(int userId) {
        return userMap.get(userId).isUserInit();
    }

    public void addPurchase(int userId, Purchase purchase) {
        userMap.get(userId).addPurchase(purchase);
    }

    public List<String> getAllOldPurchases(int userId) {
        return userMap.get(userId).getAllPurchases().stream().map(Purchase::getName).collect(Collectors.toList());
    }

    public Optional<Purchase> getPurchase(int userId, String purchaseName) {
        return userMap.get(userId).getAllPurchases().stream().filter(it -> it.getName().equals(purchaseName)).findFirst();
    }

    public void deletePurchase(int userId, String purchaseName) {
        final User user = userMap.get(userId);
        val purchaseOpt = user.getAllPurchases().stream().filter(it -> it.getName().equals(purchaseName)).findFirst();
        user.delete(purchaseOpt.get());
    }

    public void newPeriod() {
        userMap.values().forEach(User::newPeriod);
    }

}
