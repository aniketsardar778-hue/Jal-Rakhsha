package com.demo.jalrakhsa.controller;


import com.demo.jalrakhsa.Dto.ResponseDto;
import com.demo.jalrakhsa.service.AssessmentService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessments-History")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AssessmentHistoryController {

@Autowired
    private  AssessmentService assessmentService;


    // =====================================================
    // USER ASSESSMENT HISTORY
    // =====================================================

    @GetMapping("/user/{username}")
    public ResponseEntity<List<ResponseDto>>
    getUserAssessments(
            @PathVariable String username) {


        List<ResponseDto> reports =
                assessmentService
                        .getUserAssessmentHistory(
                                username
                        );


        return ResponseEntity.ok(reports);
    }
}

