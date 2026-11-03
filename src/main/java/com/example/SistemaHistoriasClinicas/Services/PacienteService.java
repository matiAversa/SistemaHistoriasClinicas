package com.example.SistemaHistoriasClinicas.Services;

import com.example.SistemaHistoriasClinicas.Models.Paciente;
import com.example.SistemaHistoriasClinicas.Repositories.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class PacienteService {

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
        return pacienteRepository.save(paciente);
    }

    @Transactional
    public void actualizarMedicacionYActividad(Integer pacienteId, String medicacion, String actFisica) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("No existe el paciente a actualizar."));

        paciente.setMedicacion(medicacion);
        paciente.setActFisica(actFisica);
    }

}
