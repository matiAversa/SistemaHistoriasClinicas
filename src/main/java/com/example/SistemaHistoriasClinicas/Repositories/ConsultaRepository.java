package com.example.SistemaHistoriasClinicas.Repositories;

import com.example.SistemaHistoriasClinicas.Models.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {
}
