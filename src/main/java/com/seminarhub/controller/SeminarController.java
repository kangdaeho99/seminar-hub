package com.seminarhub.controller;


import com.seminarhub.common.exception.DuplicateSeminarException;
import com.seminarhub.dto.SeminarDTO;
import com.seminarhub.entity.RoleType;
import com.seminarhub.security.annotation.CheckRole;
import com.seminarhub.service.SeminarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/api/v1/seminar")
@RequiredArgsConstructor
@Tag(name = "3. Seminar API")
public class SeminarController {
    private final SeminarService seminarService;

    @PostMapping(value ="")
    @Operation(summary = "1. Register a new seminar")
    public ResponseEntity<Long> register(@RequestBody SeminarDTO seminarDTO) throws DuplicateSeminarException {
        log.info("-----------------register--------------");
        log.info(seminarDTO);

        Long seminar_name = seminarService.register(seminarDTO);
        return new ResponseEntity<>(seminar_name, HttpStatus.OK);
    }


    @CheckRole(roles = {RoleType.USER})
    @GetMapping(value ="/{seminar_name}")
    @Operation(summary = "2. Get seminar information by seminar_name")
    public ResponseEntity<SeminarDTO> read(@PathVariable("seminar_name") String seminar_name){
        log.info("-------------------read----------------------");
        log.info(seminar_name);

        return new ResponseEntity<>(seminarService.get(seminar_name), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{seminar_name}", produces= MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "3. Remove a seminar by seminar_name")
    public ResponseEntity<String> remove(@PathVariable("seminar_name") String seminar_name){
        log.info("-------------------remove---------------------");
        log.info(seminar_name);
        seminarService.remove(seminar_name);
        return new ResponseEntity<>("removed", HttpStatus.OK);
    }

    @PutMapping(value ="", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "4. Modify a seminar")
    public ResponseEntity<String> modify(@RequestBody SeminarDTO seminarDTO){
        log.info("-----------------modify----------------");
        log.info(seminarDTO);

        seminarService.modify(seminarDTO);
        return new ResponseEntity<>("modified", HttpStatus.OK);
    }

}
