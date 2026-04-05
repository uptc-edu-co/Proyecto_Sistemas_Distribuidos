package uptc.edu.co.ms_suppliers.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import uptc.edu.co.ms_suppliers.model.Supplier;
import uptc.edu.co.ms_suppliers.repository.SupplierRepository;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierEventPublisher eventPublisher;


    // Constructor con ambos parámetros para que Spring los inyecte automáticamente
    public SupplierService(SupplierRepository supplierRepository, SupplierEventPublisher eventPublisher) {
        this.supplierRepository = supplierRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Crea un nuevo proveedor y envía evento a Kafka.
     * Cumple con DP-28 (Persistencia) y DP-30 (Auditoría).
     */
    public Supplier createSupplier(Supplier supplier) {
        // 3. Ahora sí, guardamos en la base de datos
        Supplier saved = supplierRepository.save(supplier);
        
        try {
            // Intentamos enviar a Kafka
            eventPublisher.sendAuditLog("CREATE_SUPPLIER", saved);
            System.out.println("Kafka: Evento de auditoría enviado.");
        } catch (Exception e) {
            // Si Kafka falla, no importa, el proveedor ya se guardó
            System.err.println("Kafka no disponible, pero proveedor guardado: " + e.getMessage());
        }
        
        return saved;
    }

    /**
     * Actualiza un proveedor existente.
     * Cumple con DP-29 (No permite cambiar el NIT).
     */
    public Supplier updateSupplier(UUID id, Supplier newData) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found"));

        // Validar que no se intente cambiar el NIT (DP-29)
        if (!existingSupplier.getNit().equals(newData.getNit())) {
            throw new IllegalArgumentException("No se permite modificar el NIT");
        }

        // Actualizar solo los campos permitidos
        if (newData.getNombre() != null && !newData.getNombre().isEmpty()) {
            existingSupplier.setNombre(newData.getNombre());
        }

        if (newData.getEmail() != null && !newData.getEmail().isEmpty()) {
            existingSupplier.setEmail(newData.getEmail());
        }

        if (newData.getTelefono() != null && !newData.getTelefono().isEmpty()) {
            existingSupplier.setTelefono(newData.getTelefono());
        }

        if (newData.getEstado() != null) {
            existingSupplier.setEstado(newData.getEstado());
        }

        return supplierRepository.save(existingSupplier);
    }

    /**
     * Obtiene un proveedor por su UUID.
     */
    public Supplier getSupplier(UUID id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found"));
    }
}