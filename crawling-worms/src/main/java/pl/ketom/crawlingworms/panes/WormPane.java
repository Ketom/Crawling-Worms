package pl.ketom.crawlingworms.panes;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import pl.ketom.crawlingworms.model.Point;
import pl.ketom.crawlingworms.model.Worm;

import java.util.LinkedList;
import java.util.List;

public class WormPane extends Pane {
    private int gridSize;
    private Color color;
    private String name;

    public WormPane(int gridSize, Color color, String name) {
        this.gridSize = gridSize;
        this.color = color;
        this.name = name;

        double margin = 0;

        Rectangle rectangle = new Rectangle(margin, margin, gridSize - 2 * margin, gridSize - 2 * margin);
        rectangle.setFill(Color.DARKGREY);

        getChildren().add(rectangle);
    }

    private Pane createTextPane(String string) {
        Pane pane = new StackPane();
        Text text = new Text(string);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        text.setFill(Color.WHITE);
        pane.getChildren().add(text);
        return pane;
    }

    public void onWormUpdated(Worm worm) {
        List<Node> nodes = new LinkedList<>();
        List<Point> segments = worm.getSegments();

        double margin = 1;
        for (Point point : segments) {
            Rectangle rectangle = new Rectangle(point.getX() * gridSize + margin, point.getY() * gridSize + margin, gridSize - 2 * margin, gridSize - 2 * margin);
            rectangle.setFill(color);
            nodes.add(rectangle);
        }

        Point point;

        Pane pane = createTextPane(name);
        point = segments.get(1);
        pane.setMinSize(gridSize, gridSize);
        pane.setMaxSize(gridSize, gridSize);
        pane.setLayoutX(point.getX() * gridSize + margin);
        pane.setLayoutY(point.getY() * gridSize + margin);
        nodes.add(pane);

        margin = 5;
        point = segments.get(0);
        Rectangle rectangle = new Rectangle(point.getX() * gridSize + margin, point.getY() * gridSize + margin, gridSize - 2 * margin, gridSize - 2 * margin);
        if (worm.isSleeping()) {
            rectangle.setFill(Color.RED.deriveColor(1, 0.5, 1, 1));
        } else {
            rectangle.setFill(Color.LIGHTGREEN);
        }
        nodes.add(rectangle);

        Platform.runLater(() -> {
                    getChildren().clear();
                    getChildren().addAll(nodes);
                }
        );
    }
}
