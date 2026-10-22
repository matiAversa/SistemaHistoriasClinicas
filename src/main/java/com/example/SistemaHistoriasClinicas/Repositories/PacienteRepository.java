package com.example.SistemaHistoriasClinicas.Repositories;

import com.example.SistemaHistoriasClinicas.Models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    public Paciente getByDocumento (String dni);

}
