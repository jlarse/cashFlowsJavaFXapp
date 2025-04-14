module com.experiment.firstintelijfxprojectjl {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.experiment.firstintelijfxprojectjl to javafx.fxml;
    exports com.experiment.firstintelijfxprojectjl;
}