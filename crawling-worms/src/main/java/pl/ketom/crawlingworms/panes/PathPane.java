package pl.ketom.crawlingworms.panes;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import pl.ketom.crawlingworms.model.Direction;
import pl.ketom.crawlingworms.model.Path;
import pl.ketom.crawlingworms.model.Point;

import java.util.LinkedList;
import java.util.List;

public class PathPane extends Pane {
    private int gridSize;
    private Color color;

    public PathPane(int gridSize, Color color) {
        this.gridSize = gridSize;
        this.color = color;
    }

    private double getRotation(Direction direction) {
        switch (direction) {
            case UP:
                return 0.0;
            case RIGHT:
                return 90.0;
            case DOWN:
                return 180.0;
            case LEFT:
                return 270.0;
        }
        return 0;
    }

    public void onPathUpdated(Path path) {
        path = new Path(path);
        List<Node> nodes = new LinkedList<>();

        path.resetPosition();
        do {
            Point point = path.getPoint();
            Direction direction = path.getDirection();

            Pane containerPane = new Pane();
            containerPane.setLayoutX(point.getX() * gridSize);
            containerPane.setLayoutY(point.getY() * gridSize);
            containerPane.setRotate(getRotation(direction));

            // without this dummy node rotation doesn't work properly
            Rectangle rectangle = new Rectangle(0, 0, gridSize, gridSize);
            rectangle.setFill(Color.TRANSPARENT);
            containerPane.getChildren().add(rectangle);

            double marginX = gridSize * 0.1;
            double marginY = gridSize * 0.25;
            Polygon polygon = new Polygon();
            polygon.getPoints().addAll(
                    (gridSize / 2.0) + (gridSize / 4.0), marginY,
                    gridSize * 1.0 - marginX, gridSize - marginY,
                    (gridSize / 2.0) + marginX, gridSize - marginY);
            polygon.setFill(color);
            containerPane.getChildren().add(polygon);

            nodes.add(containerPane);
            path.nextPosition();
        } while (path.getPosition() != 0);

        Platform.runLater(() -> {
                    getChildren().clear();
                    getChildren().addAll(nodes);
                }
        );
    }
}
