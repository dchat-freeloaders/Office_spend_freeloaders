package com.github.iauglov.mariya.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Purchase {
    private String name;
    private BigDecimal cost;
    private int count;

    public Purchase copy(Purchase purchase) {
        return new Purchase(
                purchase.name,
                purchase.cost,
                purchase.count
        );
    }
}
