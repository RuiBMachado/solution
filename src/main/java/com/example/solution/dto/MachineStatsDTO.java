package com.example.solution.dto;

import java.io.Serializable;

public class MachineStatsDTO implements Serializable {

    Double average;
    Double median;
    Double min;
    Double max;
    String machine;
    String parameter;

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
}
