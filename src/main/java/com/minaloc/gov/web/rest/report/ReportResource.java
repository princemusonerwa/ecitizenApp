package com.minaloc.gov.web.rest.report;

import com.minaloc.gov.domain.Complain;
import com.minaloc.gov.repository.ComplainRepository;
import com.minaloc.gov.service.dto.ComplainCategoryDTO;
import com.minaloc.gov.service.dto.ComplainPriorityCountDTO;
import com.minaloc.gov.service.dto.ComplainProvinceCountDTO;
import com.minaloc.gov.service.dto.ComplainStatusCountDTO;
import com.minaloc.gov.util.GenerateComplainExcelReport;
import com.minaloc.gov.util.GenerateComplainReport;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReportResource {

    private final ComplainRepository complainRepository;

    public ReportResource(ComplainRepository complainRepository) {
        this.complainRepository = complainRepository;
    }

    @GetMapping("reports/complains/status")
    ResponseEntity<List<ComplainStatusCountDTO>> getCountByStatus() {
        List<ComplainStatusCountDTO> result = complainRepository.getCountByStatus();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("reports/complains/priority")
    ResponseEntity<List<ComplainPriorityCountDTO>> getCountByPriority() {
        List<ComplainPriorityCountDTO> result = complainRepository.getCountByPriority();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("reports/complains/category")
    ResponseEntity<List<ComplainCategoryDTO>> getComplainBasedOnCategory() {
        List<ComplainCategoryDTO> result = complainRepository.getComplainBasedOnCategory();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("reports/complains/provinces")
    ResponseEntity<List<ComplainProvinceCountDTO>> getComplainBasedOnProvince() {
        List<ComplainProvinceCountDTO> result = complainRepository.getComplainBasedOnProvince();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/complains/pdf-report", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> complainsReport() {
        var complains = complainRepository.findAll();
        ByteArrayInputStream bis = GenerateComplainReport.complainsReport(complains);
        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=complainsReport.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bis));
    }

    @GetMapping("/complains/export-to-excel")
    public void exportIntoExcelFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=complain" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<Complain> complains = complainRepository.findAll();
        GenerateComplainExcelReport generator = new GenerateComplainExcelReport(complains);
        generator.generateExcelFile(response);
    }
}
