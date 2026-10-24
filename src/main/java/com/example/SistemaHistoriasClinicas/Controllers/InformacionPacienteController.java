package com.example.SistemaHistoriasClinicas.Controllers;

import com.example.SistemaHistoriasClinicas.Models.Paciente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class InformacionPacienteController {

    private final ApplicationContext applicationContext;

    @FXML
    private Label nombreValor;

    @FXML
    private Label apellidoValor;

    @FXML
    private Label telefonoValor;

    @FXML
    private Label documentoValor;

    @FXML
    private Label afiliadoValor;

    @FXML
    private Label fechaNacimientoValor;

    @FXML
    private Label medicacionValor;

    @FXML
    private Label actividadFisicaValor;

    public InformacionPacienteController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setPaciente(Paciente paciente) {
        nombreValor.setText(textoOVacio(paciente.getNombre()));
        apellidoValor.setText(textoOVacio(paciente.getApellido()));
        telefonoValor.setText(textoOVacio(paciente.getTelefono()));
        documentoValor.setText(textoOVacio(paciente.getDocumento()));
        afiliadoValor.setText(textoOVacio(paciente.getAfiliado()));
        fechaNacimientoValor.setText(formatearFecha(
                paciente.getDia(),
                paciente.getMes(),
                paciente.getAno()
        ));
        medicacionValor.setText(textoOVacio(paciente.getMedicacion()));
        actividadFisicaValor.setText(textoOVacio(paciente.getActFisica()));
    }

    @FXML
    private void volverAlInicio() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Vistas/Inicio.fxml")
            );

            loader.setControllerFactory(applicationContext::getBean);

            Parent inicio = loader.load();

            Stage ventanaActual = (Stage) nombreValor
                    .getScene()
                    .getWindow();

            ventanaActual.setScene(new Scene(inicio, 600, 500));
            ventanaActual.setTitle("Sistema de Historias Clínicas");
            ventanaActual.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String textoOVacio(String valor) {
        return valor == null ? "" : valor;
    }

    private String formatearFecha(String dia, String mes, String ano) {
        if (estaVacio(dia) && estaVacio(mes) && estaVacio(ano)) {
            return "";
        }

        return textoOVacio(dia) + "/" + textoOVacio(mes) + "/" + textoOVacio(ano);
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
