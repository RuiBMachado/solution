package com.example.solution.service;

import com.example.solution.dto.MachineStatsDTO;
import com.example.solution.dto.MachineParameterDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MachineParameterService {
    void insertMachineParameter(List<MachineParameterDTO> machineParameterDTO);

    List<MachineParameterDTO> getLatestParameterForMachine();


    List<MachineStatsDTO> getMachineStats(Integer min);
}
