package com.example.SistemaHistoriasClinicas.Services;

import com.example.SistemaHistoriasClinicas.Models.ValorDatoLaboratorio;
import com.example.SistemaHistoriasClinicas.Repositories.ValorDatoLaboratorioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ValorDatoLaboratorioService {

    private final ValorDatoLaboratorioRepository valorDatoLaboratorioRepository;

    public ValorDatoLaboratorioService(ValorDatoLaboratorioRepository valorDatoLaboratorioRepository) {
        this.valorDatoLaboratorioRepository = valorDatoLaboratorioRepository;
    }

    @Transactional(readOnly = true)
    public List<ValorDatoLaboratorio> listarPorConsulta(Integer consultaId) {
        if (consultaId == null) {
            return List.of();
        }
        return valorDatoLaboratorioRepository.findByConsultaId(consultaId);
    }
}