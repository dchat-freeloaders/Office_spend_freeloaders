package com.github.iauglov.mariya.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
public class Period {
    private Instant start;
    private Instant stop;
    private Set<Purchase> history;
}
