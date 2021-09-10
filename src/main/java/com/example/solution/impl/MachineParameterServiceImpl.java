
package com.example.solution.impl;

import com.example.solution.domain.Machine;
import com.example.solution.domain.Parameter;
import com.example.solution.repository.MachineParameterRepository;
import com.example.solution.service.MachineService;
import com.example.solution.dto.MachineStatsDTO;
import com.example.solution.dto.MachineParameterDTO;
import com.example.solution.service.MachineParameterService;
import com.google.common.math.Quantiles;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Component
public class MachineParameterServiceImpl implements MachineParameterService {

   private final MachineParameterRepository machineParameterRepository;
   private final MachineService machineService;


   public MachineParameterServiceImpl(MachineParameterRepository machineParameterRepository, MachineService machineService) {
      this.machineParameterRepository = machineParameterRepository;
      this.machineService = machineService;
   }

    /**
     * Method to start the process of save a parameter for each machine
     * For each {@link MachineParameterDTO} check if the respective machine exists and then proceed with the association process
     * @param machineParameterDTO List of {@link MachineParameterDTO}
     */
   @Override
   @Transactional
   public void insertMachineParameter(List<MachineParameterDTO> machineParameterDTO){

       for(MachineParameterDTO machineParameterDTO1 : machineParameterDTO){
           Optional<Machine> existMachine = machineService.checkIfExistsMachine(machineParameterDTO1.getMachineKey());
           existMachine.ifPresent(machine -> associateMachineAndDateToParameterAndSave(machineParameterDTO1.getMachineParameters(), machine));
       }

    }

    /**
     * Method to associate a machine and a date for each parameter and save them
     * @param parameters List of {@link Parameter}
     * @param machine {@link Machine} to associate
     */
    private void associateMachineAndDateToParameterAndSave(List<Parameter> parameters,Machine machine){
       List<Parameter> parameterToSave = new ArrayList<>();
       for(Parameter p: parameters){
           p.setMachine(machine);
           p.setDateTime(Instant.now());
           parameterToSave.add(p);
        }
        if(!parameterToSave.isEmpty()){
            machineParameterRepository.saveAll(parameterToSave);
        }
    }

    /**
     * Method to get the latest parameter for each machine that exists in system
     * @return List of {@link MachineParameterDTO}
     */
    @Override
    @Transactional
    public List<MachineParameterDTO> getLatestParameterForMachine(){

        List<Long> machinesIds= machineService.getMachineIds();
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

    /**
     * Method to start the process to get the machine statistics for the last X minutes
     * @param min Number of minutes
     * @return List of {@link MachineStatsDTO}
     */
    @Override
    @Transactional
    public List<MachineStatsDTO> getMachineStats(Integer min){

        List<Long> machinesIds= machineService.getMachineIds();
        List<MachineStatsDTO> parameterList = new ArrayList<>();

        Instant nowLessXMinutes = Instant.now().minus(min, ChronoUnit.MINUTES);
        for(Long machineId : machinesIds){

           List<Parameter> machineParameters= machineParameterRepository.
                   findByMachineIdEqualsAndDateTimeGreaterThanAndDateTimeLessThan(machineId,nowLessXMinutes,Instant.now());

           parameterList.addAll(calculateStatsForMachineParameters(machineParameters));

        }
        return parameterList;

    }

    /**
     * Method to fill the {@link MachineStatsDTO} object with the statistics value for each parameter
     * @param machineParameters List of {@link Parameter} with the machine parameters
     * @return List of {@link MachineStatsDTO}
     */
    private List<MachineStatsDTO> calculateStatsForMachineParameters(List<Parameter> machineParameters){
        List<MachineStatsDTO> machineStatsDTOList = new ArrayList<>();

        Map<String, List<Parameter>> parametersGrouped = machineParameters.stream()
            .collect(Collectors.groupingBy(Parameter::getKey));

        for (Map.Entry<String, List<Parameter>> entry : parametersGrouped.entrySet()) {
            String key = entry.getKey();
            List<Parameter> parameterList = entry.getValue();
            MachineStatsDTO machineStatsDTO = new MachineStatsDTO();

            machineStatsDTO.setMax(calculateMax(parameterList));
            machineStatsDTO.setMin(calculateMin(parameterList));
            machineStatsDTO.setMedian(calculateMedian(parameterList));
            machineStatsDTO.setAverage(calculateAverage(parameterList));
            machineStatsDTO.setParameter(key);
            machineStatsDTO.setMachine(parameterList.get(0).getMachine().getKey());

            machineStatsDTOList.add(machineStatsDTO);
        }

        return machineStatsDTOList;

    }

    /**
     * Method to calculate the maximum value for a given list of {@link Parameter}
     * @param parameterFiltered List of {@link Parameter} with the machine parameters
     * @return Maximum value
     */
    private Double calculateMax(List<Parameter> parameterFiltered){

        return parameterFiltered.stream().max(comparing(Parameter::getValue)).get().getValue();
    }

    /**
     * Method to calculate the minimum value for a given list of {@link Parameter}
     * @param parameterFiltered List of {@link Parameter} with the machine parameters
     * @return Minimum value
     */
    private Double calculateMin(List<Parameter> parameterFiltered){

        return parameterFiltered.stream().min(comparing(Parameter::getValue)).get().getValue();
    }

    /**
     * Method to calculate the media for a given list of {@link Parameter}
     * @param parameterFiltered List of {@link Parameter} with the machine parameters
     * @return Median
     */
    private Double calculateMedian(List<Parameter> parameterFiltered){

        List<Double> values = parameterFiltered.stream().map(Parameter::getValue).collect(Collectors.toList());
        return Quantiles.median().compute(values);
    }

    /**
     * Method to calculate the average for a given list of {@link Parameter}
     * @param parameterFiltered List of {@link Parameter} with the machine parameters
     * @return Average
     */
    private Double calculateAverage(List<Parameter> parameterFiltered){

        return parameterFiltered.stream().mapToDouble(Parameter::getValue).average().orElse(0.0);
    }
}
