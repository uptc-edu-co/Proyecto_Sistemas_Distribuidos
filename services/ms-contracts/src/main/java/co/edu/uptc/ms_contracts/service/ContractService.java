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
        
        supplierClient.getSupplierById(req.getSupplierId());

        validationService.validate(req);

        Contract contract = new Contract();
        contract.setSupplierId(req.getSupplierId());
        contract.setSubject(req.getSubject());
        contract.setStartDate(req.getStartDate());
        contract.setEndDate(req.getEndDate());
        contract.setBudget(req.getBudget());
        contract.setContractNumber(repository.count() + 1);

        contract = repository.save(contract);
        log.info("Contract created id={} uuid={}", contract.getId(), contract.getUuid());

        eventPublisher.publishContractCreated(contract);

        return ContractResponse.fromModel(contract);
    }
}