package com.sgar.SGARventaAPI.repositorios;

import com.sgar.SGARventaAPI.modelos.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    
    // Buscar empresas por nombre (contiene)
    Page<Empresa> findByNombreEmpresaContainingIgnoreCase(String nombreEmpresa, Pageable pageable);
    
    // Buscar empresas por asociado ID
    Page<Empresa> findByAsociadoId(Long asociadoId, Pageable pageable);
}
