package com.threadboost.controller;

import com.threadboost.dto.response.RaceSimulationResponse;
import com.threadboost.service.RaceSimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    private final RaceSimulationService raceSimulationService;

    public SimulationController(RaceSimulationService raceSimulationService) {
        this.raceSimulationService = raceSimulationService;
    }

    @PostMapping("/race")
    public ResponseEntity<RaceSimulationResponse> runRaceSimulation() {
        return ResponseEntity.ok(raceSimulationService.runSimulation());
    }
}
