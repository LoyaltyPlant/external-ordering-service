package com.loyaltyplant.server.services.externalordering.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.CreatePosOrderRequest;
import com.loyaltyplant.server.services.externalordering.application.controller.v1.PosIntegrationControllerV1;
import com.loyaltyplant.server.services.externalordering.application.helper.TestData;
import com.loyaltyplant.server.services.externalordering.application.mock.PosMockService;
import com.loyaltyplant.server.services.externalordering.application.service.WebhookForwarder;
import com.loyaltyplant.server.services.externalordering.application.utils.VendorToDoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PosIntegrationControllerV1.class)
public class PosIntegrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PosMockService posMockService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VendorToDoMapper vendorToDoMapper;

    @MockBean
    private WebhookForwarder webhookForwarder;

    @Test
    public void healthcheck_shouldReturnUpStatus() throws Exception {
        when(posMockService.healthcheck(eq(100), anyString()))
                .thenReturn(TestData.healthcheckResponse(100));

        mockMvc.perform(get("/api/v1/external-ordering/healthcheck")
                        .header("SalesOutletId", "100")
                        .header("AuthorizationToken", "plain-secret"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.services['pos-100']").value("UP"));
    }

    @Test
    public void createOrder_shouldReturnCreatedOrder() throws Exception {
        var req = new CreatePosOrderRequest();

        when(posMockService.createOrder(eq(100), eq("plain-secret"), any(CreatePosOrderRequest.class)))
                .thenReturn(TestData.createOrderResponse("789"));

        mockMvc.perform(post("/api/v1/external-ordering/order")
                        .header("SalesOutletId", "100")
                        .header("AuthorizationToken", "plain-secret")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posId").value("789"));
    }
}
