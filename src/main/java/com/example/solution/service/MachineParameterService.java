package com.example.solution.service;

import com.example.solution.dto.MachineStatsDTO;
import com.example.solution.dto.MachineParameterDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MachineParameterService {
    /**
     * Method to start the process of save a parameter for each machine
     * For each {@link MachineParameterDTO} check if the respective machine exists and then proceed with the association process
     * @param machineParameterDTO List of {@link MachineParameterDTO}
     */
    void insertMachineParameter(List<MachineParameterDTO> machineParameterDTO);

    /**
     * Method to get the latest parameter for each machine that exists in system
     * @return List of {@link MachineParameterDTO}
     */
    List<MachineParameterDTO> getLatestParameterForMachine();


    /**
     * Method to start the process to get the machine statistics for the last X minutes
     * @param min Number of minutes
     * @return List of {@link MachineStatsDTO}
     */
    List<MachineStatsDTO> getMachineStats(Integer min);
}
