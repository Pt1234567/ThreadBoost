package com.threadboost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threadboost.distributed.DistributedLockService;
import com.threadboost.distributed.TokenBucketRateLimiter;
import com.threadboost.dto.request.RateLimitRequest;
import com.threadboost.dto.response.DistributedLockResponse;
import com.threadboost.dto.response.RateLimitResponse;
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
        controllers = DistributedController.class,
        excludeAutoConfiguration = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class}
)
class DistributedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DistributedLockService distributedLockService;

    @MockBean
    private TokenBucketRateLimiter rateLimiter;

    @Test
    void acquireLock_returnsLockStatus() throws Exception {
        when(distributedLockService.tryLock("job-1", "worker-a"))
                .thenReturn(new DistributedLockResponse("job-1", true, "worker-a"));

        mockMvc.perform(post("/api/distributed/locks/{lockKey}/acquire", "job-1")
                        .header("owner", "worker-a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acquired").value(true))
                .andExpect(jsonPath("$.owner").value("worker-a"));
    }

    @Test
    void checkRateLimit_returnsTokenBucketDecision() throws Exception {
        RateLimitRequest request = new RateLimitRequest("client-a");
        when(rateLimiter.allow("client-a")).thenReturn(new RateLimitResponse("client-a", true, 99));

        mockMvc.perform(post("/api/distributed/rate-limit/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowed").value(true))
                .andExpect(jsonPath("$.remainingTokens").value(99));
    }
}
