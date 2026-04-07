package uptc.edu.co.ms_contracts.controllers;

import co.edu.uptc.shared.security.RoleScopeCatalog;
import co.edu.uptc.shared.security.annotations.RequiresScope;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uptc.edu.co.ms_contracts.dto.ContractResponse;
import uptc.edu.co.ms_contracts.dto.CreateContractRequest;
import uptc.edu.co.ms_contracts.service.ContractService;

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
}