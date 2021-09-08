package com.example.solution.impl;

import com.example.solution.domain.Machine;
import com.example.solution.domain.Parameter;
import com.example.solution.dto.MachineParameterDTO;
import com.example.solution.dto.MachineStatsDTO;
import com.example.solution.repository.MachineParameterRepository;
import com.example.solution.repository.MachineRepository;
import com.example.solution.service.MachineService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MachineParameterServiceImplTest {

    @Mock
    private MachineParameterRepository machineParameterRepository;
    @InjectMocks
    private MachineParameterServiceImpl machineParameterService;
    @Mock
    private MachineService machineService;

    @Test
    public void insertMachineParameter() {
        List<Parameter> listOfParameter = new ArrayList<>();
        listOfParameter.add(new Parameter());

        MachineParameterDTO machineParameterDTO = new MachineParameterDTO();
        machineParameterDTO.setMachineParameters(listOfParameter);

        Machine machine = new Machine();
        machine.setKey("test");

        List<MachineParameterDTO> machineParameterDTOS = Collections.singletonList(machineParameterDTO);
        when(machineService.checkIfExistsMachine(any())).thenReturn(Optional.of(machine));

        machineParameterService.insertMachineParameter(machineParameterDTOS);

        ArgumentCaptor<List<Parameter>> argument = ArgumentCaptor.forClass(List.class);
        verify(machineParameterRepository).saveAll(argument.capture());
        assertEquals("test", argument.getValue().get(0).getMachine().getKey());

    }

    @Test
    public void insertMachineParameterEmptyListOfMachineParameterDTO() {

        machineParameterService.insertMachineParameter(new ArrayList<>());

        verify(machineService,times(0)).checkIfExistsMachine(any());

    }

    @Test
    public void insertMachineParameterNoExistMachine() {
        List<Parameter> listOfParameter = new ArrayList<>();
        listOfParameter.add(new Parameter());

        MachineParameterDTO machineParameterDTO = new MachineParameterDTO();
        machineParameterDTO.setMachineParameters(listOfParameter);

        List<MachineParameterDTO> machineParameterDTOS = Collections.singletonList(machineParameterDTO);
        when(machineService.checkIfExistsMachine(any())).thenReturn(Optional.empty());

        machineParameterService.insertMachineParameter(machineParameterDTOS);

        verify(machineParameterRepository,times(0)).saveAll(any());

    }

    @Test
    public void getLatestParameterForMachine() {
        Machine m = new Machine();
        m.setKey("1");
        Parameter p = new Parameter();
        p.setMachine(m);
        when(machineService.getParametersIds()).thenReturn(Collections.singletonList(1L));
        when(machineParameterRepository.findFirstByMachine_idEqualsOrderByDateTimeDesc(any())).thenReturn(p);

        List<MachineParameterDTO> machineParameterDTOList = machineParameterService.getLatestParameterForMachine();
        assertEquals(machineParameterDTOList.get(0).getMachineKey(),m.getKey());
    }

    @Test
    public void getLatestParameterForMachineNoMachineIds() {
        when(machineService.getParametersIds()).thenReturn(new ArrayList<>());
        machineParameterService.getLatestParameterForMachine();
        verify(machineParameterRepository,times(0)).findFirstByMachine_idEqualsOrderByDateTimeDesc(any());
    }


    @Test
    public void getLatestParameterForMachineNoParameterAvailable() {

        when(machineService.getParametersIds()).thenReturn(Collections.singletonList(1L));
        when(machineParameterRepository.findFirstByMachine_idEqualsOrderByDateTimeDesc(any())).thenReturn(null);

        List<MachineParameterDTO> machineParameterDTOList = machineParameterService.getLatestParameterForMachine();
        assertTrue(machineParameterDTOList.isEmpty());
    }
    @Test
    public void getMachineStats() {
        Machine m = new Machine();
        m.setKey("1");
        Parameter parameter = new Parameter();
        parameter.setKey("speed");
        parameter.setValue(10d);
        parameter.setMachine(m);
        Parameter parameter1 = new Parameter();
        parameter1.setKey("speed");
        parameter1.setValue(20d);
        List<Parameter> parameterList = new ArrayList<>();
        parameterList.add(parameter);
        parameterList.add(parameter1);

        when(machineService.getParametersIds()).thenReturn(Collections.singletonList(1L));
        when(machineParameterRepository.findByMachineIdEqualsAndDateTimeGreaterThanAndDateTimeLessThan(any(),any(),any())).thenReturn(parameterList);
        List<MachineStatsDTO> machineStatsDTOList = machineParameterService.getMachineStats(5);

        assertEquals(machineStatsDTOList.get(0).getAverage(),15d,0);
        assertEquals(machineStatsDTOList.get(0).getMin(),10d,0);
        assertEquals(machineStatsDTOList.get(0).getMedian(),15d,0);
        assertEquals(machineStatsDTOList.get(0).getParameter(),"speed");
        assertEquals(machineStatsDTOList.get(0).getMachine(),"1");
     }

    @Test
    public void getMachineStatsNoMachineIds() {
        when(machineService.getParametersIds()).thenReturn(new ArrayList<>());
        machineParameterService.getMachineStats(5);
        verify(machineParameterRepository,times(0)).findFirstByMachine_idEqualsOrderByDateTimeDesc(any());
    }

}
