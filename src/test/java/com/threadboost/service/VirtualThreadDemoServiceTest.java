package com.threadboost.service;

import com.threadboost.dto.request.VirtualThreadDemoRequest;
import com.threadboost.dto.response.VirtualThreadDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VirtualThreadDemoServiceTest {

    private final VirtualThreadDemoService service = new VirtualThreadDemoService();

    @Test
    void runDemo_executesRequestedVirtualThreadTasks() {
        VirtualThreadDemoResponse response = service.runDemo(new VirtualThreadDemoRequest(10));

        assertThat(response.taskCount()).isEqualTo(10);
        assertThat(response.executor()).isEqualTo("newVirtualThreadPerTaskExecutor");
        assertThat(response.elapsedMillis()).isGreaterThanOrEqualTo(0);
    }
}
