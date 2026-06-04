package com.project.models;

public class GPUOption {

    private int gpuId;
    private String brand;
    private String name;
    private int forBudget;
    private int priceIncrease;
    private int performanceIncrease;

    public GPUOption(int gpuId, String brand, String name, int forBudget,
                     int priceIncrease, int performanceIncrease) {
        this.gpuId = gpuId;
        this.brand = brand;
        this.name = name;
        this.forBudget = forBudget;
        this.priceIncrease = priceIncrease;
        this.performanceIncrease = performanceIncrease;
    }

    public int getGpuId() { return gpuId; }
    public String getBrand() { return brand; }
    public String getName() { return name; }
    public int getForBudget() { return forBudget; }
    public int getPriceIncrease() { return priceIncrease; }
    public int getPerformanceIncrease() { return performanceIncrease; }
}
