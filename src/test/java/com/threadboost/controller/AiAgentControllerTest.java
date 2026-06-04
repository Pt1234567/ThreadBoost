package com.threadboost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threadboost.ai.AiAgentService;
import com.threadboost.domain.enums.ExecutionStrategy;
import com.threadboost.domain.enums.JobType;
import com.threadboost.domain.enums.Priority;
import com.threadboost.dto.request.WorkloadClassificationRequest;
import com.threadboost.dto.response.WorkloadClassificationResponse;
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
        controllers = AiAgentController.class,
        excludeAutoConfiguration = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class}
)
class AiAgentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AiAgentService aiAgentService;

    @Test
    void classify_returnsAgentRecommendation() throws Exception {
        WorkloadClassificationRequest request = new WorkloadClassificationRequest("Generate customer report");
        when(aiAgentService.classify(request)).thenReturn(new WorkloadClassificationResponse(
                JobType.REPORT,
                Priority.HIGH,
                ExecutionStrategy.THREAD_POOL,
                "reason"
        ));

        mockMvc.perform(post("/api/ai/classify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("REPORT"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.executor").value("THREAD_POOL"));
    }
}
