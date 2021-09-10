package com.example.solution.webRest;

import com.example.solution.SolutionApplication;
import com.example.solution.dto.MachineParameterDTO;
import com.example.solution.service.MachineParameterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SolutionApplication.class)
public class MachineParameterResourceTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Mock
    private MachineParameterService machineParameterService;
    private MockMvc restMachineParameterMock;

    @Before
    public void setup(){
        final MachineParameterResource machineParameterResource = new MachineParameterResource(machineParameterService);
        this.restMachineParameterMock= MockMvcBuilders.standaloneSetup(machineParameterResource).build();
    }

    @Test
    public void insertMachineParameters() throws Exception {
        List<MachineParameterDTO> machineList = new ArrayList<>();
        restMachineParameterMock.perform(post("/insert-machine-parameters").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(machineList))
        ).andExpect(status().isOk());
        verify(machineParameterService,times(1)).insertMachineParameter(anyList());
    }

    @Test
    public void getLatestMachineParameter() throws Exception {
        restMachineParameterMock.perform(get("/get-latest-machine-parameter")).andExpect(status().isOk());
        verify(machineParameterService,times(1)).getLatestParameterForMachine();
    }

    @Test
    public void testGetLatestMachineParameter() throws Exception {
        restMachineParameterMock.perform(get("/get-machine-stats/5")).andExpect(status().isOk());
        verify(machineParameterService,times(1)).getMachineStats(5);
    }
}
