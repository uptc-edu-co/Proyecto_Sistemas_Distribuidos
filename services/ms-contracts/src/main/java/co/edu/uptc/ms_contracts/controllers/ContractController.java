package co.edu.uptc.ms_contracts.controllers;

import co.edu.uptc.shared.security.RoleScopeCatalog;
import co.edu.uptc.shared.security.annotations.RequiresScope;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.uptc.ms_contracts.dto.ContractResponse;
import co.edu.uptc.ms_contracts.dto.CreateContractRequest;
import co.edu.uptc.ms_contracts.dto.UpdateContractRequest;
import co.edu.uptc.ms_contracts.service.ContractService;

import java.util.UUID;

@RestController
@RequestMapping("/contracts")
public class ContractController {
    @Autowired
    private ContractService service;

    @PostMapping
    @RequiresScope(RoleScopeCatalog.CREATE_CONTRACT)
    public ResponseEntity<ContractResponse> create(@Valid @RequestBody CreateContractRequest req) {
        ContractResponse response = service.createContract(req);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @RequiresScope(RoleScopeCatalog.UPDATE_CONTRACT)
    public ResponseEntity<ContractResponse> updateContract(
            @PathVariable UUID id,
            @RequestBody UpdateContractRequest req) {
        
        ContractResponse response = service.updateContract(id, req);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}