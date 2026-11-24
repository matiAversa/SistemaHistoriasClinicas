package com.example.SistemaHistoriasClinicas.Services;

import com.example.SistemaHistoriasClinicas.Models.Consulta;
import com.example.SistemaHistoriasClinicas.Models.Paciente;
import com.example.SistemaHistoriasClinicas.Models.ValorDatoLaboratorio;
import com.example.SistemaHistoriasClinicas.Repositories.ConsultaRepository;
import com.example.SistemaHistoriasClinicas.Repositories.PacienteRepository;
import com.example.SistemaHistoriasClinicas.Repositories.ValorDatoLaboratorioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsultaService {

    private static final Logger log = LoggerFactory.getLogger(ConsultaService.class);

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final ValorDatoLaboratorioRepository valorDatoLaboratorioRepository;

    public ConsultaService(
            ConsultaRepository consultaRepository,
            PacienteRepository pacienteRepository,
            ValorDatoLaboratorioRepository valorDatoLaboratorioRepository) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.valorDatoLaboratorioRepository = valorDatoLaboratorioRepository;
    }

    @Transactional
    public Consulta almacenarNuevaConsulta(Consulta consulta, List<ValorDatoLaboratorio> valoresLaboratorio) {
        Integer pacienteId = consulta.getPaciente().getId();
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) {
            log.error("No existe el paciente de la consulta. id={}", pacienteId);
            throw new IllegalArgumentException("No existe el paciente de la consulta.");
        }

        consulta.setPaciente(paciente);
        try {
            Consulta guardada = consultaRepository.save(consulta);
            if (valoresLaboratorio != null) {
                for (ValorDatoLaboratorio valor : valoresLaboratorio) {
                    valor.setConsulta(guardada);
                    valorDatoLaboratorioRepository.save(valor);
                }
            }
            return guardada;
        } catch (RuntimeException e) {
            log.error("No se pudo guardar la consulta", e);
            throw e;
        }
    }
}
