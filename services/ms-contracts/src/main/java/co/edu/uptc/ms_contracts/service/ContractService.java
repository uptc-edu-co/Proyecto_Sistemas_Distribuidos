package co.edu.uptc.ms_contracts.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.uptc.ms_contracts.client.SupplierClient;
import co.edu.uptc.ms_contracts.dto.ContractResponse;
import co.edu.uptc.ms_contracts.dto.CreateContractRequest;
import co.edu.uptc.ms_contracts.dto.UpdateContractRequest;
import co.edu.uptc.ms_contracts.messaging.ContractEventPublisher;
import co.edu.uptc.ms_contracts.model.Contract;
import co.edu.uptc.ms_contracts.repository.ContractRepository;

@Service
public class ContractService {

    private static final Logger log = LoggerFactory.getLogger(ContractService.class);

    private final ContractRepository repository;
    private final SupplierClient supplierClient;
    private final ContractValidationService validationService;
    private final ContractEventPublisher eventPublisher;
    private final ContractPdfService pdfService;

    public ContractService(ContractRepository repository,
                           SupplierClient supplierClient,
                           ContractValidationService validationService,
                           ContractEventPublisher eventPublisher,
                           ContractPdfService pdfService) {
        this.repository = repository;
        this.supplierClient = supplierClient;
        this.validationService = validationService;
        this.eventPublisher = eventPublisher;
        this.pdfService = pdfService;
    }

    @Transactional
    public ContractResponse createContract(CreateContractRequest req) {
        
        var supplier = supplierClient.getSupplierById(req.getSupplierId());

        validationService.validate(req);

        Contract contract = new Contract();
        contract.setSupplierId(req.getSupplierId());
        contract.setSubject(req.getSubject());
        contract.setStartDate(req.getStartDate());
        contract.setEndDate(req.getEndDate());
        contract.setBudget(req.getBudget());
        contract.setContractNumber(repository.count() + 1);

        contract = repository.save(contract);
        log.info("Contract created id={} uuid={}", contract.getId(), contract.getId());

        eventPublisher.publishContractCreated(contract);

        try {
            pdfService.generateAndStore(contract, supplier);
        } catch (Exception ex) {
            log.warn("PDF generation failed for contract id={}", contract.getId(), ex);
        }

        return ContractResponse.fromModel(contract);
    }

    @Transactional
    public ContractResponse updateContract(UUID id, UpdateContractRequest req) {

        // 1. Buscar contrato
        Contract contract = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

        boolean changed = false;

        // 2. Validar y aplicar cambios solo permitidos

        if (req.getStatus() != null && !req.getStatus().equals(contract.getStatus())) {
            contract.setStatus(req.getStatus());
            changed = true;
        }

        if (req.getBudget() != null && req.getBudget().compareTo(contract.getBudget()) != 0) {
            contract.setBudget(req.getBudget());
            changed = true;
        }

        // 3. Si NO hay cambios → NO aumentar versión
        if (!changed) {
            return ContractResponse.fromModel(contract);
        }

        // 4. Incrementar versión
        contract.setVersion(contract.getVersion() + 1);

        // 5. Guardar
        contract = repository.save(contract);

        // 6. Publicar evento
        eventPublisher.publishContractUpdated(contract);

        return ContractResponse.fromModel(contract);
    }

    
}