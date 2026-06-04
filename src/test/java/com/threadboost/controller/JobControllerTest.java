package com.threadboost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threadboost.domain.enums.JobStatus;
import com.threadboost.dto.request.CreateJobRequest;
import com.threadboost.dto.response.JobCreatedResponse;
import com.threadboost.dto.response.JobResponse;
import com.threadboost.exception.ResourceNotFoundException;
import com.threadboost.service.JobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = JobController.class,
        excludeAutoConfiguration = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class}
)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JobService jobService;

    @Test
    void createJob_returns201() throws Exception {
        UUID id = UUID.randomUUID();
        when(jobService.createJob(any())).thenReturn(new JobCreatedResponse(id, JobStatus.PENDING));

        CreateJobRequest request = new CreateJobRequest("Generate customer reports");

        mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void createJob_withBlankDescription_returns400() throws Exception {
        CreateJobRequest request = new CreateJobRequest("  ");

        mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getJob_whenNotFound_returns404() throws Exception {
        UUID id = UUID.randomUUID();
        when(jobService.getJob(id)).thenThrow(new ResourceNotFoundException("Job not found"));

        mockMvc.perform(get("/api/jobs/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllJobs_returnsList() throws Exception {
        when(jobService.getAllJobs()).thenReturn(List.of(
                new JobResponse(UUID.randomUUID(), "n", "d", null, null, null, JobStatus.PENDING, 0, null, null, null, null)
        ));

        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void deleteJob_returns204() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(delete("/api/jobs/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteJob_whenNotFound_returns404() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new ResourceNotFoundException("Job not found")).when(jobService).deleteJob(id);

        mockMvc.perform(delete("/api/jobs/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void executeJob_returnsSuccessStatus() throws Exception {
        UUID id = UUID.randomUUID();
        when(jobService.executeJob(id)).thenReturn(
                new JobResponse(id, "n", "d", null, null, null, JobStatus.SUCCESS, 0, null, null, null, null)
        );

        mockMvc.perform(post("/api/jobs/{id}/execute", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }
}
