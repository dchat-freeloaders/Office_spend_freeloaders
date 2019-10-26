package com.github.iauglov.mariya.demo.models;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class User {
    private String lastState;
    private int period;
    private BigDecimal budget;
    private Set<Purchase> cart;
    private List<Period> history;
    private Instant startPeriod;

    public User() {
        cart = new HashSet<>();
        history = new ArrayList<>();
        startPeriod = Instant.now();
    }

    public void newPeriod() {
        history.add(new Period(
                startPeriod,
                startPeriod.plus(period, ChronoUnit.DAYS),
                new HashSet<>(cart)
        ));
        startPeriod = Instant.now();
        cart.clear();
    }

    public boolean isUserInit() {
        return budget != null && period > 0;
    }

    public List<Purchase> getAllPurchases() {
        return Stream.concat(
                cart.stream(),
                history.stream()
                        .map(Period::getHistory)
                        .flatMap(Set::stream)

        ).distinct().collect(Collectors.toList());
    }

    public void addPurchase(Purchase purchase) {
        Optional<Purchase> first = cart.stream().filter(it -> it.getName().equals(purchase.getName()))
                .findFirst();
        if (first.isPresent()) {
            Purchase purchase1 = first.get();
            purchase1.setCount(purchase1.getCount() + purchase.getCount());
        } else {
            cart.add(purchase);
        }
    }

    public void delete(Purchase p) {
        cart.remove(p);
    }

    public BigDecimal freeAmount() {
        return budget.subtract(cart.stream().map(it -> it.getCost().multiply(new BigDecimal(it.getCount()))).reduce(BigDecimal::add).get());
    }
}
