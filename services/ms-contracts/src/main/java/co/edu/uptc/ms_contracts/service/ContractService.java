package co.edu.uptc.ms_contracts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.uptc.ms_contracts.client.SupplierClient;
import co.edu.uptc.ms_contracts.dto.ContractResponse;
import co.edu.uptc.ms_contracts.dto.CreateContractRequest;
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

    public ContractService(ContractRepository repository,
                           SupplierClient supplierClient,
                           ContractValidationService validationService,
                           ContractEventPublisher eventPublisher) {
        this.repository = repository;
        this.supplierClient = supplierClient;
        this.validationService = validationService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ContractResponse createContract(CreateContractRequest req) {
        // 1. Verificar que el proveedor existe y está activo en ms-suppliers
        //    Si retorna 404/422/503, Feign lanza excepción → @Transactional hace rollback
        supplierClient.getSupplierById(req.getSupplierId());

        // 2. Validaciones de negocio
        validationService.validate(req);

        // 3. Construir y persistir el contrato
        Contract contract = new Contract();
        contract.setSupplierId(req.getSupplierId());
        contract.setSubject(req.getSubject());
        contract.setStartDate(req.getStartDate());
        contract.setEndDate(req.getEndDate());
        contract.setBudget(req.getBudget());
        contract.setContractNumber(repository.count() + 1);

        contract = repository.save(contract);
        log.info("Contrato creado id={} uuid={}", contract.getId(), contract.getUuid());

        // 4. Emitir evento al broker
        eventPublisher.publishContractCreated(contract.getUuid());

        return ContractResponse.fromModel(contract);
    }
}