package com.threadboost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threadboost.dto.request.VirtualThreadDemoRequest;
import com.threadboost.dto.response.CompletableFuturePipelineResponse;
import com.threadboost.dto.response.VirtualThreadDemoResponse;
import com.threadboost.service.CompletableFuturePipelineService;
import com.threadboost.service.VirtualThreadDemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ConcurrencyLabController.class,
        excludeAutoConfiguration = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class}
)
class ConcurrencyLabControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompletableFuturePipelineService completableFuturePipelineService;

    @MockBean
    private VirtualThreadDemoService virtualThreadDemoService;

    @Test
    void runCompletableFuturePipeline_returnsPipelineResult() throws Exception {
        when(completableFuturePipelineService.generateReport())
                .thenReturn(new CompletableFuturePipelineResponse(
                        "user-data-ready",
                        "analytics-ready",
                        "report",
                        "notification",
                        5
                ));

        mockMvc.perform(post("/api/lab/completable-future/report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userData").value("user-data-ready"))
                .andExpect(jsonPath("$.analytics").value("analytics-ready"))
                .andExpect(jsonPath("$.notification").value("notification"));
    }

    @Test
    void runVirtualThreadDemo_returnsDemoResult() throws Exception {
        VirtualThreadDemoRequest request = new VirtualThreadDemoRequest(50);
        when(virtualThreadDemoService.runDemo(request))
                .thenReturn(new VirtualThreadDemoResponse(50, 20, "newVirtualThreadPerTaskExecutor", "note"));

        mockMvc.perform(post("/api/lab/virtual-threads/demo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskCount").value(50))
                .andExpect(jsonPath("$.executor").value("newVirtualThreadPerTaskExecutor"));
    }
}
