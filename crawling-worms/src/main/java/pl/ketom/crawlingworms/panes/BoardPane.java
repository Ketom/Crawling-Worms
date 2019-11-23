package pl.ketom.crawlingworms.panes;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import pl.ketom.crawlingworms.model.Board;
import pl.ketom.crawlingworms.model.Point;

import java.util.LinkedList;
import java.util.List;

public class BoardPane extends Pane {
    private int gridSize;
    private Color defaultColor;
    private Color ownersColors[];

    public BoardPane(int gridSize, Color defaultColor, Color ownersColors[]) {
        this.gridSize = gridSize;
        this.ownersColors = ownersColors;
        this.defaultColor = defaultColor;
    }

    private Color getOwnerColor(int id) {
        if(id >= 0 && id < ownersColors.length) {
            return ownersColors[id];
        }
        return defaultColor;
    }

    public void onBoardUpdated(Board board) {
        List<Node> nodes = new LinkedList<>();
        double margin = 1;

        // 1 block from each side as padding
        for (int y = -1; y < board.getHeight() + 1; y++) {
            for (int x = -1; x < board.getWidth() + 1; x++) {
                Rectangle rectangle = new Rectangle(x * gridSize + margin, y * gridSize + margin, gridSize - 2 * margin, gridSize - 2 * margin);
                Color color = getOwnerColor(board.getPointOwnerId(new Point(x, y)));
                rectangle.setFill(color);
                nodes.add(rectangle);
            }
        }

        Platform.runLater(() -> {
                    getChildren().clear();
                    getChildren().addAll(nodes);
                }
        );
    }
}
