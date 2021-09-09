package com.example.solution.dto;

import java.io.Serializable;

public class MachineStatsDTO implements Serializable {

    private Double average;
    private Double median;
    private Double min;
    private Double max;
    private String machine;
    private String parameter;

    public void setAverage(Double average) {
        this.average = average;
    }

    public void setMedian(Double median) {
        this.median = median;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Double getAverage() {
        return average;
    }

    public Double getMedian() {
        return median;
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    public String getMachine() {
        return machine;
    }

    public String getParameter() {
        return parameter;
    }
}
