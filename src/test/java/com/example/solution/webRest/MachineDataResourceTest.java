package com.example.solution.webRest;

import com.example.solution.SolutionApplication;
import com.example.solution.domain.Machine;
import com.example.solution.service.MachineService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SolutionApplication.class)
public class MachineDataResourceTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Mock
    private MachineService machineService;
    private MockMvc restMachineDataMock;

    @Before
    public void setup(){
        final MachineDataResource machineDataResource = new MachineDataResource(machineService);
        this.restMachineDataMock= MockMvcBuilders.standaloneSetup(machineDataResource).build();
    }
    @Test
    public void insertMachineData() throws Exception {
        List<Machine> machineList = new ArrayList<>();
        restMachineDataMock.perform(post("/insert-machine-data").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(machineList))
        ).andExpect(status().isOk());

        verify(machineService,times(1)).save(anyList());

    }
}
