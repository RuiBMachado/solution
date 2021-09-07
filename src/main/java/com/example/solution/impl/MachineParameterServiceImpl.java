
package com.example.solution.impl;

import com.example.solution.domain.Machine;
import com.example.solution.domain.Parameter;
import com.example.solution.repository.MachineParameterRepository;
import com.example.solution.service.MachineService;
import com.example.solution.dto.MachineStatsDTO;
import com.example.solution.dto.MachineParameterDTO;
import com.example.solution.service.MachineParameterService;
import com.google.common.math.Quantiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Component
public class MachineParameterServiceImpl implements MachineParameterService {

   private final MachineParameterRepository machineParameterRepository;
   private final MachineService machineService;
    Logger logger = LoggerFactory.getLogger(MachineParameterServiceImpl.class);


 public MachineParameterServiceImpl(MachineParameterRepository machineParameterRepository, MachineService machineService) {
  this.machineParameterRepository = machineParameterRepository;
  this.machineService = machineService;
 }
@Override
 public void insertMachineParameter(List<MachineParameterDTO> machineParameterDTO){

     for(MachineParameterDTO machineParameterDTO1 : machineParameterDTO){
         Optional<Machine> existMachine = machineService.checkIfExistsMachine(machineParameterDTO1.getMachineKey());
         existMachine.ifPresent(machine -> checkIfExistParametersOtherwiseSave(machineParameterDTO1.getMachineParameters(), machine));
     }

    }

    private void checkIfExistParametersOtherwiseSave(List<Parameter> parameters,Machine machine){
        List<Parameter> parameterToSave = new ArrayList<>();
        for(Parameter p: parameters){

                p.setMachine(machine);
                p.setDateTime(Instant.now());
                parameterToSave.add(p);

        }

        machineParameterRepository.saveAll(parameterToSave);
    }

    @Override
    public List<MachineParameterDTO> getLatestParameterForMachine(){

        List<Long> machinesIds= machineService.getParametersIds();
        List<MachineParameterDTO> parameterList = new ArrayList<>();

        for(Long machineId : machinesIds){
            Parameter p = machineParameterRepository.findFirstByMachine_idEqualsOrderByDateTimeDesc(machineId);
            if(p!=null){
                MachineParameterDTO machineParameterDTO = new MachineParameterDTO();
                machineParameterDTO.setMachineParameters(Collections.singletonList(p));
                machineParameterDTO.setMachineKey(p.getMachine().getKey());
                parameterList.add(machineParameterDTO);
            }

        }
    return parameterList;

    }

    @Override
    public List<MachineStatsDTO> getMachineStats(Integer min){

        List<Long> machinesIds= machineService.getParametersIds();
        List<MachineStatsDTO> parameterList = new ArrayList<>();

        Instant nowLessXMinutes = Instant.now().minus(min, ChronoUnit.MINUTES);
        for(Long machineId : machinesIds){

           List<Parameter> machineParameters= machineParameterRepository.
                   findByMachineIdEqualsAndDateTimeGreaterThanAndDateTimeLessThan(machineId,nowLessXMinutes,Instant.now());

           parameterList.addAll(calculateStatsForMachineParameters(machineParameters));

        }
        return parameterList;

    }

    private List<MachineStatsDTO> calculateStatsForMachineParameters(List<Parameter> machineParameters){
        List<String> parameterAlreadyVerified = new ArrayList<>();
        List<MachineStatsDTO> machineStatsDTOList = new ArrayList<>();
        for(Parameter parameter : machineParameters){

            if(!parameterAlreadyVerified.contains(parameter.getKey())){
                MachineStatsDTO machineStatsDTO = new MachineStatsDTO();

                parameterAlreadyVerified.add(parameter.getKey());
                List<Parameter> parameterFiltered = machineParameters.stream().filter(p -> p.getKey().equals(parameter.getKey())).collect(Collectors.toList());

                machineStatsDTO.setMax(calculateMax(parameterFiltered));
                machineStatsDTO.setMin(calculateMin(parameterFiltered));
                machineStatsDTO.setMedian(calculateMedian(parameterFiltered));
                machineStatsDTO.setAverage(calculateAverage(parameterFiltered));
                machineStatsDTO.setParameter(parameter.getKey());
                machineStatsDTO.setMachine(parameter.getMachine().getKey());

                machineStatsDTOList.add(machineStatsDTO);
            }
        }
        return machineStatsDTOList;

    }

    private Double calculateMax(List<Parameter> parameterFiltered){

        return parameterFiltered.stream().max(comparing(Parameter::getValue)).get().getValue();
    }

    private Double calculateMin(List<Parameter> parameterFiltered){

        return parameterFiltered.stream().min(comparing(Parameter::getValue)).get().getValue();
    }

    private Double calculateMedian(List<Parameter> parameterFiltered){

        List<Double> values = parameterFiltered.stream().map(Parameter::getValue).collect(Collectors.toList());
        return Quantiles.median().compute(values);
    }

    private Double calculateAverage(List<Parameter> parameterFiltered){

        return parameterFiltered.stream().mapToDouble(Parameter::getValue).average().orElse(0.0);
    }
}
