package com.example.SistemaHistoriasClinicas.Models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @Column(length = 9999)
    private String datosDeConsulta;

    private LocalDate fecha;

    @Column(length = 9999)
    private String diagnostico;

    public Consulta() {
    }

    public Consulta(Paciente paciente, String datosDeConsulta, LocalDate fecha, String diagnostico) {
        this.paciente = paciente;
        this.datosDeConsulta = datosDeConsulta;
        this.fecha = fecha;
        this.diagnostico = diagnostico;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getDatosDeConsulta() {
        return datosDeConsulta;
    }

    public void setDatosDeConsulta(String datosDeConsulta) {
        this.datosDeConsulta = datosDeConsulta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
}
