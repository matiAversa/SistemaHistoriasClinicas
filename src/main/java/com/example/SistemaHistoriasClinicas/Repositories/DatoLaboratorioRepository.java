package com.example.SistemaHistoriasClinicas.Repositories;

import com.example.SistemaHistoriasClinicas.Models.DatoLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatoLaboratorioRepository extends JpaRepository<DatoLaboratorio, Integer> {
}
