package com.loyaltyplant.server.services.externalordering.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.CreatePosOrderRequest;
import com.loyaltyplant.server.services.externalordering.controller.PosIntegrationControllerV2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = {
        TestsConfiguration.class,
        PosIntegrationControllerV2.class
})
public class ApiTests {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void healthcheck_shouldReturnUpStatus() throws Exception {
        mockMvc.perform(get("/api/v2/healthcheck")
                        .header("SalesOutletId", "100")
                        .header("AuthorizationToken", "plain-secret"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.services['pos-100']").value("UP"));
    }

    @Test
    public void createOrder_shouldReturnCreatedOrder() throws Exception {
        var req = new CreatePosOrderRequest();

        mockMvc.perform(post("/api/v2/order")
                        .header("SalesOutletId", "100")
                        .header("AuthorizationToken", "plain-secret")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posId").value(containsString("POS-")));
    }


}
