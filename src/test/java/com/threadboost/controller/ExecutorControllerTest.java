package com.threadboost.controller;

import com.threadboost.dto.response.ExecutorStatsResponse;
import com.threadboost.service.ExecutorStatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ExecutorController.class,
        excludeAutoConfiguration = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class}
)
class ExecutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExecutorStatsService executorStatsService;

    @Test
    void getStats_returnsExecutorStats() throws Exception {
        when(executorStatsService.getStats()).thenReturn(new ExecutorStatsResponse("CUSTOM_THREAD_POOL", 2, 0, 8));

        mockMvc.perform(get("/api/executor/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mode").value("CUSTOM_THREAD_POOL"))
                .andExpect(jsonPath("$.executedJobCount").value(2))
                .andExpect(jsonPath("$.activeJobCount").value(0))
                .andExpect(jsonPath("$.availableProcessors").value(8));
    }
}
