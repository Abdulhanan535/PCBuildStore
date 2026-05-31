package com.project.models;

public class Build {

    private int buildId;
    private String cpuModel;
    private String cpuType;
    private String gpuModel;
    private String gpuType;
    private String ram;
    private String storage;
    private String psu;
    private int totalPrice;
    private int performanceScore;

    public Build(int buildId, String cpuModel, String cpuType, String gpuModel, String gpuType,
                 String ram, String storage, String psu, int totalPrice, int performanceScore) {
        this.buildId = buildId;
        this.cpuModel = cpuModel;
        this.cpuType = cpuType;
        this.gpuModel = gpuModel;
        this.gpuType = gpuType;
        this.ram = ram;
        this.storage = storage;
        this.psu = psu;
        this.totalPrice = totalPrice;
        this.performanceScore = performanceScore;
    }

    public int getBuildId() { return buildId; }
    public String getCpuModel() { return cpuModel; }
    public String getCpuType() { return cpuType; }
    public String getGpuModel() { return gpuModel; }
    public String getGpuType() { return gpuType; }
    public String getRam() { return ram; }
    public String getStorage() { return storage; }
    public String getPsu() { return psu; }
    public int getTotalPrice() { return totalPrice; }
    public int getPerformanceScore() { return performanceScore; }
}
