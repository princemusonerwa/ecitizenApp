package com.minaloc.gov.web.rest.report;

import com.minaloc.gov.repository.ComplainRepository;
import com.minaloc.gov.service.ComplainService;
import com.minaloc.gov.service.dto.ComplainCategoryDTO;
import com.minaloc.gov.service.dto.ComplainPriorityCountDTO;
import com.minaloc.gov.service.dto.ComplainStatusCountDTO;
import java.util.List;
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
}
