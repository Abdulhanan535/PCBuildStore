package com.project.models;

import java.time.LocalDateTime;

public class Bill {

    private int billId;
    private int buildId;
    private String finalCpu;
    private String finalGpu;
    private int finalPrice;
    private int finalScore;
    private LocalDateTime purchaseDate;

    public Bill(int billId, int buildId, String finalCpu, String finalGpu,
                int finalPrice, int finalScore, LocalDateTime purchaseDate) {
        this.billId = billId;
        this.buildId = buildId;
        this.finalCpu = finalCpu;
        this.finalGpu = finalGpu;
        this.finalPrice = finalPrice;
        this.finalScore = finalScore;
        this.purchaseDate = purchaseDate;
    }

    public int getBillId() { return billId; }
    public int getBuildId() { return buildId; }
    public String getFinalCpu() { return finalCpu; }
    public String getFinalGpu() { return finalGpu; }
    public int getFinalPrice() { return finalPrice; }
    public int getFinalScore() { return finalScore; }
    public LocalDateTime getPurchaseDate() { return purchaseDate; }
}
