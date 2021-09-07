package com.example.solution.impl;

import com.example.solution.domain.Machine;
import com.example.solution.repository.MachineRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
@RunWith(MockitoJUnitRunner.class)
public class MachineServiceImplTest {

    @Mock
    private MachineRepository machineRepository;
    @InjectMocks
    private MachineServiceImpl machineService;


    @Test
    public void testCheckIfExistsMachine() {
        machineService.checkIfExistsMachine("test");
        verify(machineRepository,times(1)).findMachineByKeyEquals("test");
    }

    @Test
    public void testSave() {
        List<Machine> machineList = new ArrayList<>();
        machineList.add(new Machine());
        machineService.save(machineList);
        verify(machineRepository,times(1)).saveAll(machineList);
    }

    @Test
    public void testGetParametersIds() {
        machineService.getParametersIds();
        verify(machineRepository,times(1)).findAll();
    }
}
