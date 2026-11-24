package com.example.SistemaHistoriasClinicas.Repositories;

import com.example.SistemaHistoriasClinicas.Models.ValorDatoLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValorDatoLaboratorioRepository extends JpaRepository<ValorDatoLaboratorio, Integer> {

    @Query("""
            SELECT valor
            FROM ValorDatoLaboratorio valor
            JOIN FETCH valor.datoLaboratorio
            WHERE valor.consulta.id = :consultaId
            ORDER BY valor.datoLaboratorio.nombre
            """)
    List<ValorDatoLaboratorio> findByConsultaId(@Param("consultaId") Integer consultaId);
}
