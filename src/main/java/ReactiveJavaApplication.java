import generator.AnimalGenerator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Animal;
import model.enums.AnimalType;
import reactor.core.Disposable;
import service.AnimalFxService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReactiveJavaApplication extends Application {

    private static final AnimalFxService animalService = new AnimalFxService(new AnimalGenerator());

    private TableView<Animal> tableView = new TableView<>();
    private TextField searchField = new TextField();
    private List<Animal> allAnimals = new ArrayList<>();
    private Disposable subscription;

    @Override
    public void start(Stage primaryStage) {
        TableColumn<Animal, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Animal, Double> weightCol = new TableColumn<>("Weight");
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

        TableColumn<Animal, Double> heightCol = new TableColumn<>("Height");
        heightCol.setCellValueFactory(new PropertyValueFactory<>("height"));

        TableColumn<Animal, Double> pulseCol = new TableColumn<>("Pulse");
        pulseCol.setCellValueFactory(new PropertyValueFactory<>("pulse"));

        TableColumn<Animal, LocalDate> birthdayCol = new TableColumn<>("Birthday");
        birthdayCol.setCellValueFactory(new PropertyValueFactory<>("birthday"));

        TableColumn<Animal, AnimalType> animalTypeCol = new TableColumn<>("animalType");
        animalTypeCol.setCellValueFactory(new PropertyValueFactory<>("animalType"));

        tableView.getColumns().addAll(nameCol, weightCol, heightCol, pulseCol, birthdayCol, animalTypeCol);

        searchField.setPromptText("Filter by name...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterTable(newValue));

        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");
        Button clearButton = new Button("Clear");

        startButton.setOnAction(event -> startGeneration());
        stopButton.setOnAction(event -> stopGeneration());
        clearButton.setOnAction(event -> clearData());

        HBox buttonBox = new HBox(10, startButton, stopButton, clearButton);

        VBox vbox = new VBox(10, searchField, buttonBox, tableView);
        Scene scene = new Scene(vbox, 600, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Zoo Animal Tracker");
        primaryStage.show();
    }

    private void startGeneration() {
        if (subscription != null && !subscription.isDisposed()) {
            return;
        }
        subscription = animalService.getAnimalData()
                .subscribe(animal -> {
                    Platform.runLater(() -> {
                        allAnimals.add(animal);
                        if (matchesFilter(animal, searchField.getText())) {
                            tableView.getItems().add(animal);
                        }
                    });
                });
    }

    private void stopGeneration() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    private void clearData() {
        allAnimals.clear();
        tableView.getItems().clear();
    }

    private void filterTable(String query) {
        // Если поле пустое, показываем всех
        if (query == null || query.trim().isEmpty()) {
            tableView.getItems().setAll(allAnimals);
            return;
        }

        String lowerQuery = query.toLowerCase();
        // Фильтруем
        List<Animal> filtered = allAnimals.stream()
                .filter(animal -> matchesFilter(animal, lowerQuery))
                .collect(Collectors.toList());

        tableView.getItems().setAll(filtered);
    }

    private boolean matchesFilter(Animal animal, String lowerQuery) {
        if (lowerQuery == null || lowerQuery.isEmpty()) {
            return true;
        }
        return animal.getName() != null
                && animal.getName().toLowerCase().contains(lowerQuery);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
