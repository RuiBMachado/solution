package com.example.solution.dto;


import com.example.solution.domain.Parameter;

import java.io.Serializable;
import java.util.List;

public class MachineParameterDTO implements Serializable {

    private String machineKey;
    private List<Parameter> machineParameters;

    public String getMachineKey() {
        return machineKey;
    }

    public void setMachineKey(String machineKey) {
        this.machineKey = machineKey;
    }

    public List<Parameter> getMachineParameters() {
        return machineParameters;
    }

    public void setMachineParameters(List<Parameter> machineParameters) {
        this.machineParameters = machineParameters;
    }
}
