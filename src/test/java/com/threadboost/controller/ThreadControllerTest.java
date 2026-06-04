package com.threadboost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threadboost.dto.request.ThreadPoolConfigRequest;
import com.threadboost.dto.response.ThreadMetricsResponse;
import com.threadboost.execution.ThreadPoolManager;
import com.threadboost.service.ExecutorStatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ThreadController.class,
        excludeAutoConfiguration = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class}
)
class ThreadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExecutorStatsService executorStatsService;

    @MockBean
    private ThreadPoolManager threadPoolManager;

    @Test
    void getThreadMetrics_returnsPoolMetrics() throws Exception {
        when(executorStatsService.getThreadMetrics())
                .thenReturn(new ThreadMetricsResponse(2, 8, 1, 2, 5, 10, 0));

        mockMvc.perform(get("/api/thread/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.corePoolSize").value(2))
                .andExpect(jsonPath("$.maximumPoolSize").value(8))
                .andExpect(jsonPath("$.queueSize").value(5));
    }

    @Test
    void updateThreadConfig_returnsUpdatedMetrics() throws Exception {
        ThreadPoolConfigRequest request = new ThreadPoolConfigRequest(4, 12);
        when(threadPoolManager.updateConfig(request))
                .thenReturn(new ThreadMetricsResponse(4, 12, 0, 0, 0, 0, 0));

        mockMvc.perform(post("/api/thread/config")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.corePoolSize").value(4))
                .andExpect(jsonPath("$.maximumPoolSize").value(12));
    }
}
