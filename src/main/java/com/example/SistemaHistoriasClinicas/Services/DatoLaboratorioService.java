package com.example.SistemaHistoriasClinicas.Services;

import com.example.SistemaHistoriasClinicas.Models.DatoLaboratorio;
import com.example.SistemaHistoriasClinicas.Repositories.DatoLaboratorioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class DatoLaboratorioService {

    private final DatoLaboratorioRepository datoLaboratorioRepository;

    public DatoLaboratorioService(DatoLaboratorioRepository datoLaboratorioRepository) {
        this.datoLaboratorioRepository = datoLaboratorioRepository;
    }

    @Transactional
    public void cargarDatosInicialesSiLaTablaEstaVacia() {
        if (datoLaboratorioRepository.count() > 0) {
            return;
        }

        List<String> nombres = List.of(
                "Hematocrito",
                "Hemoglobina",
                "Leucositos",
                "Plaquetas",
                "Colesterol",
                "Colesterol HDL",
                "Colesterol LDL",
                "Trigliceridos",
                "Urea",
                "Creatinina",
                "TGO",
                "TGP",
                "Birirubina",
                "Vitamina D",
                "Vitamina B12",
                "Orina"
        );

        for (String nombre : nombres) {
            datoLaboratorioRepository.save(new DatoLaboratorio(nombre));
        }
    }

    @Transactional(readOnly = true)
    public List<DatoLaboratorio> listarTodos() {
        List<DatoLaboratorio> datos = datoLaboratorioRepository.findAll();
        datos.sort(Comparator.comparing(
                DatoLaboratorio::getNombre,
                Comparator.nullsLast(String::compareToIgnoreCase)
        ));
        return datos;
    }
}
