package com.example.SistemaHistoriasClinicas.Services;

import com.example.SistemaHistoriasClinicas.Models.Consulta;
import com.example.SistemaHistoriasClinicas.Models.Paciente;
import com.example.SistemaHistoriasClinicas.Repositories.ConsultaRepository;
import com.example.SistemaHistoriasClinicas.Repositories.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;

    public ConsultaService(ConsultaRepository consultaRepository, PacienteRepository pacienteRepository) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public Consulta almacenarNuevaConsulta(Consulta consulta) {
        Integer pacienteId = consulta.getPaciente().getId();
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("No existe el paciente de la consulta."));

        consulta.setPaciente(paciente);
        return consultaRepository.save(consulta);
    }
}
