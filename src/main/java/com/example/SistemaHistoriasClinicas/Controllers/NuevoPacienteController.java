package com.example.SistemaHistoriasClinicas.Controllers;

import com.example.SistemaHistoriasClinicas.Alertas.AlertClass;
import com.example.SistemaHistoriasClinicas.Models.Paciente;
import com.example.SistemaHistoriasClinicas.Services.PacienteService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

@Component
public class NuevoPacienteController {

    private static final int MAXIMO_CARACTERES_TEXTO = 9999;

    private final PacienteService pacienteService;

    @FXML
    private TextField nombreInput;

    @FXML
    private TextField apellidoInput;

    @FXML
    private TextField telefonoInput;

    @FXML
    private TextField documentoInput;

    @FXML
    private TextField numeroAfiliadoInput;

    @FXML
    private TextField diaInput;

    @FXML
    private TextField mesInput;

    @FXML
    private TextField anoInput;

    @FXML
    private TextField edadInput;

    @FXML
    private TextArea medicacionInput;

    @FXML
    private TextArea actividadFisicaInput;

    private final ApplicationContext applicationContext;

    public NuevoPacienteController(
            PacienteService pacienteService,
            ApplicationContext applicationContext) {

        this.pacienteService = pacienteService;
        this.applicationContext = applicationContext;
    }

    @FXML
    private void initialize() {
        configurarCampoNumerico(diaInput);
        configurarCampoNumerico(mesInput);
        configurarCampoNumerico(anoInput);

        configurarLimiteDeCaracteres(medicacionInput);
        configurarLimiteDeCaracteres(actividadFisicaInput);

        diaInput.textProperty().addListener((observable, anterior, nuevo) -> calcularEdad());
        mesInput.textProperty().addListener((observable, anterior, nuevo) -> calcularEdad());
        anoInput.textProperty().addListener((observable, anterior, nuevo) -> calcularEdad());
    }

    private void configurarCampoNumerico(TextField campo) {
        campo.setTextFormatter(new TextFormatter<String>(
                change -> change.getControlNewText().matches("\\d*")
                        ? change
                        : null
        ));
    }

    private void configurarLimiteDeCaracteres(TextArea campo) {
        campo.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= MAXIMO_CARACTERES_TEXTO
                        ? change
                        : null
        ));
    }

    private void calcularEdad() {
        if (diaInput.getText().isBlank()
                || mesInput.getText().isBlank()
                || anoInput.getText().isBlank()) {

            edadInput.clear();
            return;
        }

        try {
            int dia = Integer.parseInt(diaInput.getText());
            int mes = Integer.parseInt(mesInput.getText());
            int ano = Integer.parseInt(anoInput.getText());

            LocalDate nacimiento = LocalDate.of(ano, mes, dia);
            LocalDate hoy = LocalDate.now();

            if (nacimiento.isAfter(hoy)) {
                edadInput.clear();
                return;
            }

            int edad = Period.between(nacimiento, hoy).getYears();
            edadInput.setText(String.valueOf(edad));

        } catch (NumberFormatException | DateTimeParseException e) {
            edadInput.clear();
        }
    }

    @FXML
    private void AlmacenarNuevoPaciente() {
        if (!validarCampos()) {
            AlertClass.mostrarMensaje("Fallo la validacion de datos.", "Resvise los datos.");
            return;
        }

        int dia = Integer.parseInt(diaInput.getText());
        int mes = Integer.parseInt(mesInput.getText());
        int ano = Integer.parseInt(anoInput.getText());


        LocalDate fechaNacimiento = LocalDate.of(ano, mes, dia);

        Paciente paciente = new Paciente(
                nombreInput.getText().trim(),
                apellidoInput.getText().trim(),
                telefonoInput.getText().trim(),
                documentoInput.getText().trim(),
                numeroAfiliadoInput.getText().trim(),
                fechaNacimiento,
                medicacionInput.getText().trim(),
                actividadFisicaInput.getText().trim()
        );

        pacienteService.almacenarNuevoPaciente(paciente);



        volverAlInicio();
    }

    private boolean validarCampos() {
        if (nombreInput.getText().isBlank()
                || apellidoInput.getText().isBlank()
                || telefonoInput.getText().isBlank()
                || documentoInput.getText().isBlank()
                || numeroAfiliadoInput.getText().isBlank()
                || diaInput.getText().isBlank()
                || mesInput.getText().isBlank()
                || anoInput.getText().isBlank()) {

            AlertClass.mostrarMensaje(
                    "Campos incompletos",
                    "Completá todos los campos obligatorios."
            );
            return false;
        }

        if (!documentoInput.getText().matches("\\d+")) {
            AlertClass.mostrarMensaje(
                    "Documento inválido",
                    "El documento debe contener solo números."
            );
            return false;
        }

        if (!telefonoInput.getText().matches("\\d+")) {
            AlertClass.mostrarMensaje(
                    "Teléfono inválido",
                    "El teléfono debe contener solo números."
            );
            return false;
        }

        try {

            LocalDate nacimiento = LocalDate.of(
                    Integer.parseInt(anoInput.getText()),
                    Integer.parseInt(mesInput.getText()),
                    Integer.parseInt(diaInput.getText())
            );

            if (nacimiento.isAfter(LocalDate.now())) {
                AlertClass.mostrarMensaje(
                        "Fecha inválida",
                        "La fecha de nacimiento no puede ser futura."
                );

                return false;
            }

        } catch (NumberFormatException | DateTimeParseException e) {
            AlertClass.mostrarMensaje(
                    "Fecha inválida",
                    "Ingresá una fecha de nacimiento válida."
            );

            return false;
        }

        return true;
    }


    @FXML
    private void volverAlInicio() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Vistas/Inicio.fxml")
            );

            loader.setControllerFactory(
                    applicationContext::getBean
            );

            Parent inicio = loader.load();

            Stage ventanaActual = (Stage) nombreInput
                    .getScene()
                    .getWindow();

            ventanaActual.setScene(new Scene(inicio, 600, 500));
            ventanaActual.setTitle("Sistema de Historias Clínicas");
            ventanaActual.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}