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

    @Override
    public Optional<Machine> checkIfExistsMachine(String machineKey){
       return machineRepository.findMachineByKeyEquals(machineKey);
    }

    @Override
    public void save(List<Machine> machine) {
        machineRepository.saveAll(machine);
    }

    @Override
    public List<Long> getParametersIds() {
        return machineRepository.findAll().stream().map(Machine::getId).collect(Collectors.toList());
    }
}
