module org.com.battleship {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;


    requires javafx.base;
    requires org.apache.logging.log4j;
    requires annotations;
    requires java.logging;

    requires datafx;


    opens org.com.battleship to javafx.fxml;
    exports org.com.battleship;
    exports org.com.battleship.controller;
    exports org.com.battleship.model;
}