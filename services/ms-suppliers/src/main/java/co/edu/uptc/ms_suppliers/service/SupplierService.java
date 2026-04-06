package co.edu.uptc.ms_suppliers.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import co.edu.uptc.ms_suppliers.dto.SupplierRequest;
import co.edu.uptc.ms_suppliers.dto.SupplierResponse;
import co.edu.uptc.ms_suppliers.dto.UpdateSupplierRequest;
import co.edu.uptc.ms_suppliers.model.Supplier;
import co.edu.uptc.ms_suppliers.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private static final Logger log = LoggerFactory.getLogger(SupplierService.class);

    private final SupplierRepository supplierRepository;
    private final SupplierEventPublisher eventPublisher;

    public SupplierResponse createSupplier(SupplierRequest request) {
        if (supplierRepository.existsByNit(request.getNit())) {
            throw new IllegalArgumentException("El NIT ya existe: " + request.getNit());
        }

        Supplier saved = supplierRepository.save(request.toModel());
        publishAuditEvent("CREATE_SUPPLIER", saved);

        return SupplierResponse.fromModel(saved);
    }

    public SupplierResponse updateSupplier(UUID id, UpdateSupplierRequest request) {
        Supplier existing = findByIdOrThrow(id);
        Supplier updates = request.toModel();

        validateNitUnchanged(existing, updates);
        applyUpdates(existing, updates);

        Supplier saved = supplierRepository.save(existing);
        return SupplierResponse.fromModel(saved);
    }

    public SupplierResponse getSupplier(UUID id) {
        return SupplierResponse.fromModel(findByIdOrThrow(id));
    }

    private Supplier findByIdOrThrow(UUID id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proveedor no encontrado con id: " + id));
    }

    private void validateNitUnchanged(Supplier existing, Supplier updates) {
        if (updates.getNit() != null && !existing.getNit().equals(updates.getNit())) {
            throw new IllegalArgumentException("No se permite modificar el NIT");
        }
    }

    private void applyUpdates(Supplier target, Supplier source) {
        if (StringUtils.hasText(source.getName())) {
            target.setName(source.getName());
        }
        if (StringUtils.hasText(source.getEmail())) {
            target.setEmail(source.getEmail());
        }
        if (StringUtils.hasText(source.getPhone())) {
            target.setPhone(source.getPhone());
        }
        if (source.getIsActive() != null) {
            target.setIsActive(source.getIsActive());
        }
    }

    private void publishAuditEvent(String eventType, Supplier supplier) {
        try {
            eventPublisher.sendAuditLog(eventType, supplier);
            log.info("Evento de auditoría '{}' enviado para proveedor id={}", eventType, supplier.getId());
        } catch (Exception e) {
            log.warn("Kafka no disponible al enviar '{}' para id={}: {}", eventType, supplier.getId(), e.getMessage());
        }
    }
}