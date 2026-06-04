package com.threadboost.service;

import com.threadboost.dto.response.RaceSimulationResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RaceSimulationServiceTest {

    private final RaceSimulationService raceSimulationService = new RaceSimulationService();

    @Test
    void runSimulation_keepsAtomicAndLockedCountersCorrect() {
        RaceSimulationResponse response = raceSimulationService.runSimulation();

        assertThat(response.expectedCount()).isEqualTo(response.atomicCount());
        assertThat(response.expectedCount()).isEqualTo(response.lockedCount());
        assertThat(response.unsafeCount()).isLessThanOrEqualTo(response.expectedCount());
    }
}
