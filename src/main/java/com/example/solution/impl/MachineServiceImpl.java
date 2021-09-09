package com.example.solution.impl;

import com.example.solution.domain.Machine;
import com.example.solution.repository.MachineRepository;
import com.example.solution.service.MachineService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;

    public MachineServiceImpl(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    /**
     * Method to search for a machine given a machine key
     *
     * @param machineKey Machine key to search for
     * @return Optional<Machine> or Optional empty
     */
    @Override
    public Optional<Machine> checkIfExistsMachine(String machineKey){
       return machineRepository.findMachineByKeyEquals(machineKey);
    }

    /**
     * Method to save a list of machines
     *
     * @param machine List of Machine to save
     */
    @Override
    public void save(List<Machine> machine) {
        machineRepository.saveAll(machine);
    }

    /**
     * Method to get all machines Ids
     *
     * @return list of all machine Ids
     */
    @Override
    public List<Long> getMachineIds() {
        return machineRepository.findAll().stream().map(Machine::getId).collect(Collectors.toList());
    }
}
