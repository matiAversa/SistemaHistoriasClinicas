package com.example.SistemaHistoriasClinicas.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Paciente {
    @Id
    private Integer id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String documento;
    private String afiliado;
    @Column(length = 9999)
    private String actFisica;
    @Column(length = 9999)
    private String medicacion;
    private Double pesoMax;
    private Double pesoMin;
    private String mes;
    private String dia;
    private String ano;

    //private Integer idOS;


    public Paciente() {
    }

    public Paciente(String nombre, String apellido, String telefono, String documento, String afiliado, LocalDate fechaNacimiento, String medicacion, String actFisica) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.documento = documento;
        this.afiliado = afiliado;
        Integer diaInt = fechaNacimiento.getDayOfMonth();
        this.dia = diaInt.toString();
        Integer mesInt = fechaNacimiento.getMonthValue();
        this.mes = mesInt.toString();
        Integer anoInt = fechaNacimiento.getYear();
        this.ano = anoInt.toString();
        this.medicacion = medicacion;
        this.actFisica = actFisica;
        this.pesoMax = 0.0;
        this.pesoMin = 0.0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getAfiliado() {
        return afiliado;
    }

    public void setAfiliado(String afiliado) {
        this.afiliado = afiliado;
    }

    public String getActFisica() {
        return actFisica;
    }

    public void setActFisica(String actFisica) {
        this.actFisica = actFisica;
    }

    public String getMedicacion() {
        return medicacion;
    }

    public void setMedicacion(String medicacion) {
        this.medicacion = medicacion;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
