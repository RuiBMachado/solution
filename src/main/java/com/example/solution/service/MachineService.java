package com.example.solution.service;

import com.example.solution.domain.Machine;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface MachineService {
    /**
     * Method to save a list of machines
     *
     * @param machine List of Machine to save
     */
     void save(List<Machine> machine);

     /**
     * Method to search for a machine given a machine key
     *
     * @param machineKey Machine key to search for
     * @return Optional<Machine> or Optional empty
     */
     Optional<Machine> checkIfExistsMachine(String machineKey);

    /**
     * Method to get all machines Ids
     *
     * @return list of all machine Ids
     */
     List<Long> getMachineIds();
}
