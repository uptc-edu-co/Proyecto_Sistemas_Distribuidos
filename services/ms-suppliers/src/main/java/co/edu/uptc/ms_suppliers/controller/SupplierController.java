package co.edu.uptc.ms_suppliers.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uptc.ms_suppliers.dto.SupplierRequest;
import co.edu.uptc.ms_suppliers.dto.SupplierResponse;
import co.edu.uptc.ms_suppliers.dto.UpdateSupplierRequest;
import co.edu.uptc.ms_suppliers.service.SupplierService;
import co.edu.uptc.shared.security.annotations.RequiresScope;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    @RequiresScope("create:supplier")
    @ResponseStatus(HttpStatus.CREATED)
    public SupplierResponse createSupplier(@Valid @RequestBody SupplierRequest request) {
        return supplierService.createSupplier(request);
    }

    @PutMapping("/{id}")
    @RequiresScope("update:supplier")
    @ResponseStatus(HttpStatus.OK)
    public SupplierResponse updateSupplier(@PathVariable UUID id, @Valid @RequestBody UpdateSupplierRequest request) {
        return supplierService.updateSupplier(id, request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SupplierResponse getSupplier(@PathVariable UUID id) {
        return supplierService.getSupplier(id);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        errorResponse.put("status", "BAD_REQUEST");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
