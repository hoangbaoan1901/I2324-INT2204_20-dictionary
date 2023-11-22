package DictionaryApplication.Alerts;

import javafx.scene.control.Alert;

public class Alerts {
    private Alert createAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert;
    }

    public void showAlertInfo(String title, String content) {
        createAlert(Alert.AlertType.INFORMATION, title, content).showAndWait();
    }

    public void showAlertWarning(String title, String content) {
        createAlert(Alert.AlertType.WARNING, title, content).showAndWait();
    }

    public Alert alertConfirmation(String title, String content) {
        return createAlert(Alert.AlertType.CONFIRMATION, title, content);
    }

    public Alert alertWarning(String title, String content) {
        return createAlert(Alert.AlertType.WARNING, title, content);
    }
}
