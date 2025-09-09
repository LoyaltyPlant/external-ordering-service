package com.loyaltyplant.server.services.externalordering.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.CreatePosOrderRequest;
import com.loyaltyplant.server.services.externalordering.application.helper.TestData;
import com.loyaltyplant.server.services.externalordering.application.mock.PosMockService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PosIntegrationController.class)
public class PosIntegrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PosMockService posMockService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void healthcheck_shouldReturnUpStatus() throws Exception {
        when(posMockService.healthcheck(eq(100)))
                .thenReturn(TestData.healthcheckResponse(100));

        mockMvc.perform(get("/api/v1/external-ordering/health/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.services['pos-100']").value("UP"));
    }

    @Test
    public void createOrder_shouldReturnCreatedOrder() throws Exception {
        var request = new com.loyaltyplant.common.integration.protocol.digitalordering.request.CreatePosOrderRequest();
        when(posMockService.createOrder(eq(100), any(CreatePosOrderRequest.class)))
                .thenReturn(TestData.createOrderResponse("789"));

        mockMvc.perform(post("/api/v1/external-ordering/order/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posId").value("789"));
    }
}
