package com.example.solution.webRest;

import com.example.solution.domain.Machine;
import com.example.solution.service.MachineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MachineDataResource {

    private final MachineService machineService;

    public MachineDataResource(MachineService machineService) {
        this.machineService = machineService;
    }

    /**
     * Request to save a list of {@link Machine}
     */
    @PostMapping("/insert-machine-data")
    public ResponseEntity<HttpStatus> insertMachineData(@RequestBody List<Machine> machineData) {
       machineService.save(machineData);
       return ResponseEntity.ok(HttpStatus.OK);

    }
}
