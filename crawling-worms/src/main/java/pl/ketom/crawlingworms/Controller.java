package pl.ketom.crawlingworms;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import pl.ketom.crawlingworms.model.Path;
import pl.ketom.crawlingworms.model.Worm;
import pl.ketom.crawlingworms.panes.BoardPane;
import pl.ketom.crawlingworms.panes.PathPane;
import pl.ketom.crawlingworms.panes.WormPane;

public class Controller {
    @FXML
    private StackPane mainContent;
    @FXML
    private Slider slider0;
    @FXML
    private Slider slider1;
    @FXML
    private Slider slider2;
    @FXML
    private TextField textField0;
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private ToggleButton toggleButton;
    @FXML
    private Button buttonReset;
    private PathPane pathPanes[];
    private WormPane wormPanes[];
    private BoardPane boardPane;
    private TextField textFields[];
    private Slider sliders[];

    private Task task;

    public void onClose() {
        clear();
        Platform.exit();
    }

    private void init() {
        int gridSize = 25;

        pathPanes = new PathPane[3];
        wormPanes = new WormPane[3];

        task = new Task();

        Color colors[] = new Color[3];
        colors[0] = Color.RED.deriveColor(1, 1, 1, 0.5);
        colors[1] = Color.BLUE.deriveColor(1, 1, 1, 0.5);
        colors[2] = Color.GREEN.deriveColor(1, 1, 1, 0.5);

        boardPane = new BoardPane(gridSize, Color.LIGHTGREY, colors);
        wormPanes[0] = new WormPane(gridSize, Color.RED, "W1");
        wormPanes[1] = new WormPane(gridSize, Color.BLUE, "W2");
        wormPanes[2] = new WormPane(gridSize, Color.GREEN, "W3");
        pathPanes[0] = new PathPane(gridSize, Color.RED.deriveColor(1, 1, 1, 0.5));
        pathPanes[1] = new PathPane(gridSize, Color.BLUE.deriveColor(1, 1, 1, 0.5));
        pathPanes[2] = new PathPane(gridSize, Color.GREEN.deriveColor(1, 1, 1, 0.5));

        mainContent.getChildren().add(boardPane);
        mainContent.getChildren().add(pathPanes[0]);
        mainContent.getChildren().add(pathPanes[1]);
        mainContent.getChildren().add(pathPanes[2]);
        mainContent.getChildren().add(wormPanes[0]);
        mainContent.getChildren().add(wormPanes[1]);
        mainContent.getChildren().add(wormPanes[2]);

        task.setOnBoardUpdated(boardPane::onBoardUpdated);
        task.setOnPathUpdated(this::onPathUpdated);
        task.setOnWormUpdated(this::onWormUpdated);

        for (int i = 0; i < sliders.length; i++) {
            int id = i;
            sliders[i].valueProperty().addListener(x -> speedChanged(id));
            sliders[i].setValue(task.getWormSpeed(id));
        }

        if(toggleButton.isSelected()) {
            task.setPaused(false);
        }

        task.start();
    }

    private void clear() {
        task.terminate();
        task = null;

        boardPane = null;
        pathPanes = null;
        wormPanes = null;

        mainContent.getChildren().clear();
    }

    private void speedChanged(int id) {
        double speed = sliders[id].getValue();
        speed = Math.round(speed * 10) / 10.0;
        task.setWormSpeed(id, speed);
    }

    public void onPathUpdated(Path path) {
        if(pathPanes[path.getId()] != null) {
            pathPanes[path.getId()].onPathUpdated(path);
        }
    }

    public void onWormUpdated(Worm worm) {
        if(wormPanes[worm.getId()] != null) {
            wormPanes[worm.getId()].onWormUpdated(worm);
        }

        double speed = worm.getSpeed();
        Platform.runLater(() -> {
                    textFields[worm.getId()].setText(Double.toString(speed));
                }
        );
    }

    private void resetButtonPressed() {
        clear();
        init();
    }

    private void toggleButtonPressed() {
        if(toggleButton.isSelected()) {
            task.setPaused(false);
            toggleButton.setText("Enabled");
        } else {
            task.setPaused(true);
            toggleButton.setText("Disabled");
        }
    }

    public void initialize() {
        textFields = new TextField[3];
        textFields[0] = textField0;
        textFields[1] = textField1;
        textFields[2] = textField2;

        sliders = new Slider[3];
        sliders[0] = slider0;
        sliders[1] = slider1;
        sliders[2] = slider2;

        toggleButton.setOnAction(event -> toggleButtonPressed());
        buttonReset.setOnAction(event -> resetButtonPressed());

        init();
    }
}
