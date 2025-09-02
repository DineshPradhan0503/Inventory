package com.inventory.management.controllers;

import com.inventory.management.models.Sale;
import com.inventory.management.payload.response.SalesReport;
import com.inventory.management.payload.response.StockReportItem;
import com.inventory.management.services.ExportService;
import com.inventory.management.services.ReportingService;
import com.inventory.management.repository.AuditLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {com.inventory.management.InventoryManagementApplication.class, com.inventory.management.TestMailConfig.class})
@AutoConfigureMockMvc
class ReportingControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReportingService reportingService;

    @MockBean
    ExportService exportService;

    @MockBean
    AuditLogRepository auditLogRepository;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void stockReport_shouldReturn200() throws Exception {
        when(reportingService.generateStockReport()).thenReturn(List.of());
        mockMvc.perform(get("/api/reports/stock"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void exportStockExcel_shouldReturn200() throws Exception {
        when(reportingService.generateStockReport()).thenReturn(List.of());
        when(exportService.exportStockExcel(List.of())).thenReturn(new byte[]{1,2,3});
        mockMvc.perform(get("/api/reports/stock/export").accept(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk());
    }
}

