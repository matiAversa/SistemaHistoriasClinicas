package com.example.SistemaHistoriasClinicas.Alertas;

import javafx.scene.control.Alert;

public class AlertClass {
    public static void mostrarMensaje(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
