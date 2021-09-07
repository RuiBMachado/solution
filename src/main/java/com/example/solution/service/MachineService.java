package com.example.solution.service;

import com.example.solution.domain.Machine;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface MachineService {

     void save(List<Machine> machine);

     Optional<Machine> checkIfExistsMachine(String machine);

     List<Long> getParametersIds();
}
