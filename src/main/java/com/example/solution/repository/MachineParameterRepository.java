package com.example.solution.repository;

import com.example.solution.domain.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface MachineParameterRepository extends JpaRepository<Parameter, Long> {

    Parameter findFirstByMachine_idEqualsOrderByDateTimeDesc(Long machineId);

    List<Parameter>  findByMachineIdEqualsAndDateTimeGreaterThanAndDateTimeLessThan(Long id, Instant lessXMinutes, Instant now);
}
