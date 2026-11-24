package com.example.SistemaHistoriasClinicas.Services;

import com.example.SistemaHistoriasClinicas.Models.Paciente;
import com.example.SistemaHistoriasClinicas.Repositories.PacienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class PacienteService {

    private static final Logger log = LoggerFactory.getLogger(PacienteService.class);

    private PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional(readOnly = true)
    public List<Paciente> buscarPorDocumento(String documento) {
        List<Paciente> pacientes = pacienteRepository.findByDocumento(documento);
        for (Paciente paciente : pacientes) {
            paciente.setConsultas(new ArrayList<>(paciente.getConsultas()));
        }
        return pacientes;
    }

    @Transactional(readOnly = true)
    public boolean existeDocumento(String documento) {
        return pacienteRepository.existsByDocumento(documento);
    }

    @Transactional
    public Paciente almacenarNuevoPaciente(Paciente paciente) {
        try {
            return pacienteRepository.save(paciente);
        } catch (RuntimeException e) {
            log.error("No se pudo guardar el paciente", e);
            throw e;
        }
    }

    @Transactional
    public void actualizarMedicacionYActividad(Integer pacienteId, String medicacion, String actFisica) {
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) {
            log.error("No existe el paciente a actualizar. id={}", pacienteId);
            throw new IllegalArgumentException("No existe el paciente a actualizar.");
        }

        paciente.setMedicacion(medicacion);
        paciente.setActFisica(actFisica);
    }

}
