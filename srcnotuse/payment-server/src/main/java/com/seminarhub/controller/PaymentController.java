package com.seminarhub.controller;

import com.seminarhub.dto.PaymentRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/payment")
@RequiredArgsConstructor
@Tag(name = "Payment Server")
public class PaymentController {

    @PostMapping(value = "")
    public ResponseEntity<Boolean> paymentCheck(PaymentRequestDTO paymentRequestDTO){
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
