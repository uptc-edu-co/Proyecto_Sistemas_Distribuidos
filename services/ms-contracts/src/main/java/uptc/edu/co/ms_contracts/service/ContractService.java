package uptc.edu.co.ms_contracts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uptc.edu.co.ms_contracts.client.SupplierClient;
import uptc.edu.co.ms_contracts.dto.ContractResponse;
import uptc.edu.co.ms_contracts.dto.CreateContractRequest;
import uptc.edu.co.ms_contracts.messaging.ContractEventPublisher;
import uptc.edu.co.ms_contracts.model.Contract;
import uptc.edu.co.ms_contracts.repository.ContractRepository;

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
        supplierClient.getSupplierById(req.getIdProveedor());

        // 2. Validaciones de negocio
        validationService.validate(req);

        // 3. Construir y persistir el contrato
        Contract contract = new Contract();
        contract.setIdProveedor(req.getIdProveedor());
        contract.setObjeto(req.getObjeto());
        contract.setFechaInicio(req.getFechaInicio());
        contract.setFechaFin(req.getFechaFin());
        contract.setPresupuesto(req.getPresupuesto());
        contract.setNumContrato(repository.count() + 1);

        contract = repository.save(contract);
        log.info("Contrato creado id={} uuid={}", contract.getId(), contract.getUuid());

        // 4. Emitir evento al broker
        eventPublisher.publishContractCreated(contract.getUuid());

        return ContractResponse.fromModel(contract);
    }
}