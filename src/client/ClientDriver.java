package client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ClientDriver extends Application implements EventHandler<ActionEvent> {

	Button startBtn;
	
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		Scene gameScene = new Scene(root, 600, 400);
		GridPane grid = new GridPane();
		root.setCenter(grid);
		
		StackPane titlePane = new StackPane();
		Scene titleScene = new Scene(titlePane, 600,400);
		titleScene.getStylesheets().add("styles.css");
		titlePane.getStyleClass().add("pane");
		
		startBtn = new Button("Start");
		startBtn.setOnAction(this);
		startBtn.getStyleClass().add("button");
		titlePane.getChildren().add(startBtn);


		primaryStage.setTitle("Farkle GUI");
		primaryStage.setScene(titleScene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void handle(ActionEvent evt) {
		if (evt.getSource()  == startBtn) {
			System.out.println("Start");
		}
	}
}