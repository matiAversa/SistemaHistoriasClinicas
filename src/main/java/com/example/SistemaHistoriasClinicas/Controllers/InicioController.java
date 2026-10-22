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
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

        Paciente paciente = pacienteService.getByDocumento(dni);

        if (paciente == null) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Paciente no encontrado", "No existe un paciente con el DNI " + dni + ".");
            return;
        }

        mostrarAlerta(
                Alert.AlertType.INFORMATION, "Paciente encontrado", "Paciente: " + paciente.getNombre() + " " + paciente.getApellido());

        // Si querés, también podés abrir la ventana con los datos encontrados:
        abrirInformacionDelPaciente(paciente);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/informacionDelPaciente.fxml"));

            loader.setControllerFactory(applicationContext::getBean);

            Parent root = loader.load();

            //InformacionDelPacienteController controller =loader.getController();
            // Este metodo debo crearlo en este mismo controller, no crear otro

            //controller.setPaciente(paciente);

            Stage nuevaVentana = new Stage();
            nuevaVentana.setTitle(paciente == null ? "Nuevo paciente" : "Información del paciente");
            nuevaVentana.setScene(new Scene(root));
            nuevaVentana.initModality(Modality.APPLICATION_MODAL);
            nuevaVentana.showAndWait();

        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR,"Error","No se pudo abrir la ventana del paciente.");
            e.printStackTrace();
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