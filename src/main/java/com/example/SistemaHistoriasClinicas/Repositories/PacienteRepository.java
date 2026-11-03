package com.example.SistemaHistoriasClinicas.Repositories;

import com.example.SistemaHistoriasClinicas.Models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    List<Paciente> findByDocumento(String documento);

    boolean existsByDocumento(String documento);

}
