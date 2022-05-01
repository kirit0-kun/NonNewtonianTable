module com.flowapp.NonNewtonianTable {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires org.jetbrains.annotations;
    requires DateTimeRCryptor;

    exports com.flowapp.NonNewtonianTable;
    exports com.flowapp.NonNewtonianTable.Models;
    exports com.flowapp.NonNewtonianTable.Controllers to javafx.fxml;
    opens com.flowapp.NonNewtonianTable;
    opens com.flowapp.NonNewtonianTable.Controllers to javafx.fxml;
}