package co.edu.uptc.ms_contracts.controllers;

import co.edu.uptc.ms_contracts.model.ContractPdf;
import co.edu.uptc.ms_contracts.repository.ContractPdfRepository;
import co.edu.uptc.shared.security.RoleScopeCatalog;
import co.edu.uptc.shared.security.annotations.RequiresScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/contracts")
public class ContractPdfController {

    private final ContractPdfRepository repository;

    public ContractPdfController(ContractPdfRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}/pdf")
    @RequiresScope(RoleScopeCatalog.VIEW_CONTRACTS_BY_ID)
    public ResponseEntity<byte[]> getPdf(@PathVariable("id") UUID id) {
        Optional<ContractPdf> pdf = repository.findById(id);
        if (pdf.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ContractPdf data = pdf.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "contract-" + id + ".pdf");

        return new ResponseEntity<>(data.getPdfBytes(), headers, HttpStatus.OK);
    }
}
