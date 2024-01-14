package it.unipi.chesspuzzles.client.controllers;

import it.unipi.chesspuzzles.client.App;
import it.unipi.chesspuzzles.client.ChessPuzzlesApi;
import it.unipi.chesspuzzles.client.shared.Favourite;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class FavouritesController {

    private static final Logger logger = LogManager.getLogger(FavouritesController.class);

    private final ObservableList<FavouritesTableItem> observableList = FXCollections.observableArrayList();

    @FXML
    private TableView<FavouritesTableItem> favouritesTable = new TableView<>();

    @FXML
    private Label favouritesLabel;

    @FXML
    private void initialize() {
        favouritesLabel.setText(App.getUsername() + "'s favourite puzzles");
        initializeTable();
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                try {
                    Favourite[] favourites = ChessPuzzlesApi.getFavourites(App.getUserId());
                    for (Favourite favourite : favourites) {
                        observableList.add(new FavouritesTableItem(favourite));
                    }
                } catch (IOException | InterruptedException e) {
                    logger.error("Unable to build favourites table -> {}", e.getMessage());
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void initializeTable() {
        TableColumn<FavouritesTableItem, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<FavouritesTableItem, String> publishTimeCol = new TableColumn<>("Publish time");
        publishTimeCol.setCellValueFactory(new PropertyValueFactory<>("publishTime"));

        TableColumn<FavouritesTableItem, String> addTimeCol = new TableColumn<>("Add time");
        addTimeCol.setCellValueFactory(new PropertyValueFactory<>("addTime"));

        TableColumn<FavouritesTableItem, Boolean> solvedCol = new TableColumn<>("Solved");
        solvedCol.setCellValueFactory(new PropertyValueFactory<>("solved"));

        favouritesTable.getColumns().add(titleCol);
        favouritesTable.getColumns().add(publishTimeCol);
        favouritesTable.getColumns().add(addTimeCol);
        favouritesTable.getColumns().add(solvedCol);
        favouritesTable.setItems(observableList);
    }

    @FXML
    void playMenuItemClick() {
        FavouritesTableItem toPlay = favouritesTable.getSelectionModel().getSelectedItem();

        // User selected an empty row
        if (toPlay == null) {
            return;
        }

        App.setFavouritePuzzle(toPlay.getFavourite().puzzle);
        App.setRoot("play");
    }

    @FXML
    public void deleteMenuItemClick() {
        FavouritesTableItem toDelete = favouritesTable.getSelectionModel().getSelectedItem();

        // User selected an empty row
        if (toDelete == null) {
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                try {
                    ChessPuzzlesApi.deleteFavourite(App.getUserId(), toDelete.getFavourite().id);
                } catch (IOException | InterruptedException e) {
                    logger.warn("Unable to delete favourite puzzle -> {}", e.getMessage());
                }
                return null;
            }
        };
        new Thread(task).start();

        observableList.remove(toDelete);
    }

    @FXML
    public void markAsSolvedMenuItemClick() {
        FavouritesTableItem toEdit = favouritesTable.getSelectionModel().getSelectedItem();

        // toEdit is null if user selected an empty row
        if (toEdit == null || toEdit.getFavourite().solved) {
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                try {
                    ChessPuzzlesApi.markFavouriteAsSolved(App.getUserId(), toEdit.getFavourite().id);
                } catch (IOException | InterruptedException e) {
                    logger.warn("Unable to mark puzzle as solved -> {}", e.getMessage());
                }
                return null;
            }
        };
        new Thread(task).start();

        // Little workaround to easily update the tableView
        observableList.remove(toEdit);
        toEdit.getFavourite().solved = true;
        observableList.add(new FavouritesTableItem(toEdit.getFavourite()));
    }

    @FXML
    public void markAsToSolveMenuItemClick() {
        FavouritesTableItem toEdit = favouritesTable.getSelectionModel().getSelectedItem();

        // toEdit is null if user selected an empty row
        if (toEdit == null || !toEdit.getFavourite().solved) {
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                try {
                    ChessPuzzlesApi.markFavouriteAsToSolve(App.getUserId(), toEdit.getFavourite().id);
                } catch (IOException | InterruptedException e) {
                    logger.warn("Unable to mark puzzle as to solve -> {}", e.getMessage());
                }
                return null;
            }
        };
        new Thread(task).start();

        // Little workaround to easily update the tableView
        observableList.remove(toEdit);
        toEdit.getFavourite().solved = false;
        observableList.add(new FavouritesTableItem(toEdit.getFavourite()));
    }

    @FXML
    private void mainMenuBtnClick() {
        App.setRoot("main");
    }

}