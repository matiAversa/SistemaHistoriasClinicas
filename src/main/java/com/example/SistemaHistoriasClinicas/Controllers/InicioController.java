package com.example.SistemaHistoriasClinicas.Controllers;

import com.example.SistemaHistoriasClinicas.Models.Paciente;
import com.example.SistemaHistoriasClinicas.Services.PacienteService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class InicioController {

    private final PacienteService pacienteService;
    private final ApplicationContext applicationContext;

    @FXML
    private TextField documentoInput;

    public InicioController(PacienteService pacienteService,ApplicationContext applicationContext) {
        this.pacienteService = pacienteService;
        this.applicationContext = applicationContext;
    }

    @FXML
    public void initialize() {
        TextFormatter<String> soloNumeros = new TextFormatter<>(
                change -> {
                    String texto = change.getControlNewText();
                    return texto.matches("\\d*") ? change : null;});

        documentoInput.setTextFormatter(soloNumeros);
    }

    @FXML
    private void buscarPaciente() {
        String textoDni = documentoInput.getText();

        if (textoDni == null || textoDni.isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING, "DNI requerido", "Ingresá un DNI para buscar un paciente.");
            return;
        }

        Integer dni;

        try {
            dni = Integer.valueOf(textoDni);
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "DNI inválido", "El DNI debe ser un número válido.");
            return;
        }

        List<Paciente> pacientes = pacienteService.buscarPorDocumento(textoDni);

        if (pacientes.isEmpty()) {
            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Paciente no encontrado",
                    "No existe un paciente con el DNI " + dni + "."
            );
            return;
        }

        if (pacientes.size() > 1) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Documento duplicado",
                    "Hay " + pacientes.size() + " pacientes con el DNI " + dni + ". No se puede abrir una sola ficha."
            );
            return;
        }

        abrirInformacionDelPaciente(pacientes.get(0));
    }

    @FXML
    private void nuevoPaciente() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Vistas/NuevoPaciente.fxml")
            );

            loader.setControllerFactory(applicationContext::getBean);

            Parent nuevaVista = loader.load();

            Stage ventanaActual = (Stage) documentoInput
                    .getScene()
                    .getWindow();

            Scene nuevaEscena = new Scene(nuevaVista);

            ventanaActual.setScene(nuevaEscena);
            ventanaActual.setTitle("Nuevo paciente");

            ventanaActual.setWidth(850);
            ventanaActual.setHeight(800);
            ventanaActual.setMinWidth(850);
            ventanaActual.setMinHeight(800);

            ventanaActual.centerOnScreen();
            ventanaActual.show();

        } catch (IOException e) {
            e.printStackTrace();

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo abrir la pantalla de nuevo paciente."
            );
        }
    }
    private void abrirInformacionDelPaciente(Paciente paciente) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Vistas/InformacionPaciente.fxml")
            );

            loader.setControllerFactory(applicationContext::getBean);

            Parent informacionPaciente = loader.load();

            InformacionPacienteController controller = loader.getController();
            controller.setPaciente(paciente);

            Stage ventanaActual = (Stage) documentoInput
                    .getScene()
                    .getWindow();

            ventanaActual.setScene(new Scene(informacionPaciente));
            ventanaActual.setTitle("Información del paciente");
            ventanaActual.setWidth(1220);
            ventanaActual.setHeight(860);
            ventanaActual.setMinWidth(1100);
            ventanaActual.setMinHeight(820);
            ventanaActual.centerOnScreen();
            ventanaActual.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo abrir la pantalla de información del paciente."
            );
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}