package com.example.SistemaHistoriasClinicas.Services;

import com.example.SistemaHistoriasClinicas.Models.Paciente;
import com.example.SistemaHistoriasClinicas.Repositories.PacienteRepository;
import org.springframework.stereotype.Service;


@Service
public class PacienteService {

    private PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente getByDocumento (Integer dni){
        return this.pacienteRepository.getByDocumento(dni.toString());
    }

    public Paciente almacenarNuevoPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

}
