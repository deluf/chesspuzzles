package it.unipi.chesspuzzles.client.controllers;

import it.unipi.chesspuzzles.client.App;
import it.unipi.chesspuzzles.client.shared.Attemp;
import it.unipi.chesspuzzles.client.ChessPuzzlesApi;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class MainController {

    private static final Logger logger = LogManager.getLogger(MainController.class);

    @FXML
    private StackedBarChart<String, Integer> performanceChart;

    @FXML
    private Label welcomeBackLabel;

    @FXML
    private void initialize() {
        welcomeBackLabel.setText("Welcome back, " + App.getUsername());

        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                try {
                    LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
                    LocalDateTime lastMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                    Attemp[] attemps = ChessPuzzlesApi.getAttempsAfter(App.getUserId(), lastMonday);

                    Platform.runLater(() -> {
                        updateChart(attemps);
                        installChartTooltips();
                    });
                } catch (IOException | InterruptedException e) {
                    logger.error("Unable to build performance chart -> {}", e.getMessage());
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void updateChart(Attemp[] attemps) {
        XYChart.Series<String, Integer> correctSeries = new XYChart.Series<>();
        correctSeries.setName("Correct");
        int[] correct = new int[7];

        XYChart.Series<String, Integer> hintsSeries = new XYChart.Series<>();
        hintsSeries.setName("Hints");
        int[] hints = new int[7];

        XYChart.Series<String, Integer> wrongSeries = new XYChart.Series<>();
        wrongSeries.setName("Wrong");
        int[] wrong = new int[7];

        for (Attemp attemp : attemps) {
            int dayOfWeek = attemp.getParsedTimestamp().getDayOfWeek().getValue() - 1;
            switch (attemp.type) {
                case SOLVE:
                    correct[dayOfWeek]++;
                    break;
                case HINT:
                    hints[dayOfWeek]++;
                    break;
                case FAIL:
                    wrong[dayOfWeek]++;
                    break;
            }
        }

        for (int i = 0; i < 7; i++) {
            correctSeries.getData().add(new XYChart.Data<>(indexToDayOfWeek(i), correct[i]));
            hintsSeries.getData().add(new XYChart.Data<>(indexToDayOfWeek(i), hints[i]));
            wrongSeries.getData().add(new XYChart.Data<>(indexToDayOfWeek(i), wrong[i]));
        }

        performanceChart.getData().add(correctSeries);
        performanceChart.getData().add(hintsSeries);
        performanceChart.getData().add(wrongSeries);
    }

    private static String indexToDayOfWeek(int index) {
        switch (index) {
            case 0:
                return "Monday";
            case 1:
                return "Tuesday";
            case 2:
                return "Wednesday";
            case 3:
                return "Thursday";
            case 4:
                return "Friday";
            case 5:
                return "Saturday";
            case 6:
                return "Sunday";
            default:
                return "";
        }
    }

    private void installChartTooltips() {
        for (XYChart.Series<String, Integer> series : performanceChart.getData()) {
            for (XYChart.Data<String, Integer> data : series.getData()) {
                Tooltip tooltip = new Tooltip();
                tooltip.setText(series.getName() + ": " + data.getYValue().toString());
                Tooltip.install(data.getNode(), tooltip);
                tooltip.setShowDelay(Duration.ZERO);
                tooltip.getStyleClass().add("chart-tooltip");
            }
        }
    }

    @FXML
    private void playBtnClick() {
        App.setRoot("play");
    }

    @FXML
    private void openFavouritesBtnClick() {
        App.setRoot("favourites");
    }

    @FXML
    private void logoutBtnClick() {
        App.setRoot("authentication");
    }

}