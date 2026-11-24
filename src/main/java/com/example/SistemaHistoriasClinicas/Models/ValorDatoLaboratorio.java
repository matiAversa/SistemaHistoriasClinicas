package com.example.SistemaHistoriasClinicas.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id_consulta", "id_dato_laboratorio"}))
public class ValorDatoLaboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_consulta", nullable = false)
    private Consulta consulta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_dato_laboratorio", nullable = false)
    private DatoLaboratorio datoLaboratorio;

    private Double valor;

    @Transient
    private String valorIngresado;

    public ValorDatoLaboratorio() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public DatoLaboratorio getDatoLaboratorio() {
        return datoLaboratorio;
    }

    public void setDatoLaboratorio(DatoLaboratorio datoLaboratorio) {
        this.datoLaboratorio = datoLaboratorio;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getValorIngresado() {
        return valorIngresado;
    }

    public void setValorIngresado(String valorIngresado) {
        this.valorIngresado = valorIngresado;
    }
}
