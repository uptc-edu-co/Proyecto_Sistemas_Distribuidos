package co.edu.uptc.ms_contracts.controllers;

import co.edu.uptc.shared.security.RoleScopeCatalog;
import co.edu.uptc.shared.security.annotations.RequiresScope;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.uptc.ms_contracts.dto.ContractResponse;
import co.edu.uptc.ms_contracts.dto.CreateContractRequest;
import co.edu.uptc.ms_contracts.dto.UpdateContractRequest;
import co.edu.uptc.ms_contracts.service.ContractService;

import java.net.URI;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final ContractService service;

    public ContractController(ContractService service) {
        this.service = service;
    }

    @PostMapping
    @RequiresScope(RoleScopeCatalog.CREATE_CONTRACT)
    public ResponseEntity<ContractResponse> create(@Valid @RequestBody CreateContractRequest req) {
        ContractResponse response = service.createContract(req);
        URI location = URI.create("/contracts/" + response.getUuid());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ContractResponse updateContract(
            @PathVariable Long id,
            @RequestBody UpdateContractRequest req) {

        return contractService.updateContract(id, req);
    }
}