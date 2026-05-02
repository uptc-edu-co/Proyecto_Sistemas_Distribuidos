package co.edu.uptc.ms_contracts.service;

import co.edu.uptc.ms_contracts.client.dto.SupplierClientResponse;
import co.edu.uptc.ms_contracts.model.Contract;
import co.edu.uptc.ms_contracts.model.ContractPdf;
import co.edu.uptc.ms_contracts.repository.ContractPdfRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContractPdfService {

    private static final String PDF_CONTENT_TYPE = "application/pdf";

    private final ContractPdfRepository repository;
    private final ContractPdfGenerator generator;

    public ContractPdfService(ContractPdfRepository repository, ContractPdfGenerator generator) {
        this.repository = repository;
        this.generator = generator;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void generateAndStore(Contract contract, SupplierClientResponse supplier) throws Exception {
        byte[] bytes = generator.generate(contract, supplier);
        ContractPdf pdf = ContractPdf.builder()
                .contractId(contract.getId())
                .pdfBytes(bytes)
                .contentType(PDF_CONTENT_TYPE)
                .build();
        repository.save(pdf);
    }
}
