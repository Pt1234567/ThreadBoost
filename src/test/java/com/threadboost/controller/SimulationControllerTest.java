package com.threadboost.controller;

import com.threadboost.dto.response.RaceSimulationResponse;
import com.threadboost.service.RaceSimulationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = SimulationController.class,
        excludeAutoConfiguration = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class}
)
class SimulationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RaceSimulationService raceSimulationService;

    @Test
    void runRaceSimulation_returnsCounterComparison() throws Exception {
        when(raceSimulationService.runSimulation())
                .thenReturn(new RaceSimulationResponse(8, 2_000, 16_000, 14_500, 16_000, 16_000));

        mockMvc.perform(post("/api/simulation/race"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expectedCount").value(16_000))
                .andExpect(jsonPath("$.unsafeCount").value(14_500))
                .andExpect(jsonPath("$.atomicCount").value(16_000))
                .andExpect(jsonPath("$.lockedCount").value(16_000));
    }
}
