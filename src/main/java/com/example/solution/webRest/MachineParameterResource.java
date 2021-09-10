package com.example.solution.webRest;

import com.example.solution.service.MachineParameterService;
import com.example.solution.dto.MachineStatsDTO;
import com.example.solution.dto.MachineParameterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MachineParameterResource {

    private final MachineParameterService machineParameterService;

    public MachineParameterResource(MachineParameterService machineParameterService) {
        this.machineParameterService = machineParameterService;
    }

    /**
     * Request to save a list of {@link MachineParameterDTO}
     */
    @PostMapping("/insert-machine-parameters")
    public ResponseEntity<HttpStatus> insertMachineParameters(@RequestBody List<MachineParameterDTO> machineParameter) {

        machineParameterService.insertMachineParameter(machineParameter);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    /**
     * Request to get the latest machine parameter
     */
    @GetMapping("/get-latest-machine-parameter")
    public ResponseEntity<List<MachineParameterDTO>> getLatestMachineParameter() {

        List<MachineParameterDTO>parameterList=machineParameterService.getLatestParameterForMachine();
        return ResponseEntity.ok(parameterList);

    }

    /**
     * Request to get the machine stats for the last X minutes
     */
    @GetMapping("/get-machine-stats/{min}")
    public ResponseEntity<List<MachineStatsDTO>> getLatestMachineParameter(@PathVariable Integer min) {

        List<MachineStatsDTO>machineStatsDTOS=machineParameterService.getMachineStats(min);
        return ResponseEntity.ok(machineStatsDTOS);

    }
}
