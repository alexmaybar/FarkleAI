package client;

import java.util.ArrayList;
import java.util.Arrays;

import agent.AIPlayer;
import game.Farkle;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The Class ClientDriver.
 */
public class ClientDriver extends Application implements EventHandler<ActionEvent> {
	
	/** The game. */
	private Farkle game;
	
	private AIPlayer ai;

	/** The root pane. */
	private BorderPane root;
	
	/** The scene. */
	private Scene scene;

	/** The title screen. */
	private TitleScreen title;
	
	/** The die pane. */
	private DiePane diePane;
	
	/** The info pane. */
	private InfoPane infoPane;

	/** The scoreboard. */
	private TableView<TurnInfo> scoreboard;
	
	/** The data. */
	private ObservableList<TurnInfo> data;

	/** The start over btn. */
	private Button startOverBtn;

	/**
	 * Start.
	 *
	 * @param primaryStage the primary stage
	 */
	@Override
	public void start(Stage primaryStage) {
		game = new Farkle(); 
		ai = new AIPlayer();
		data = FXCollections.observableList(new ArrayList<>());

		root = new BorderPane();
		scene = new Scene(root, 875, 400);
		scene.getStylesheets().add("styles.css");

		title = new TitleScreen(this);
		root.setCenter(title);

		scoreboard = new TableView<>();
		scoreboard.setEditable(true);
		scoreboard.setId("table");
		scoreboard.setItems(data);

		TableColumn<TurnInfo, String> turnCol = new TableColumn<TurnInfo, String>("Turn #");
		turnCol.setSortable(false);
		turnCol.setCellValueFactory(new PropertyValueFactory<TurnInfo, String>("turnNumber"));
		TableColumn<TurnInfo, String> p1Col = new TableColumn<TurnInfo, String>("Player 1");
		p1Col.setCellValueFactory(new PropertyValueFactory<TurnInfo, String>("p1"));
		scoreboard.getColumns().setAll(Arrays.asList(turnCol, p1Col));
		TableColumn<TurnInfo, String> p2Col = new TableColumn<TurnInfo, String>("Player 2");
		p2Col.setCellValueFactory(new PropertyValueFactory<TurnInfo, String>("p2"));
		scoreboard.getColumns().setAll(Arrays.asList(turnCol, p1Col, p2Col));

		infoPane = new InfoPane(game);

		primaryStage.setTitle("Farkle GUI");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Handler.
	 *
	 * @param evt the evt
	 */
	@Override
	public void handle(ActionEvent evt) {
		if (evt.getSource() == title.getStartBtn() || evt.getSource() == startOverBtn) {
			// System.out.println("Start");
			game = new Farkle();
			data = FXCollections.observableList(new ArrayList<>());

			scoreboard = new TableView<>();
			scoreboard.setEditable(true);
			scoreboard.setId("table");
			scoreboard.setItems(data);

			TableColumn<TurnInfo, String> turnCol = new TableColumn<TurnInfo, String>("Turn #");
			turnCol.setSortable(false);
			turnCol.setCellValueFactory(new PropertyValueFactory<TurnInfo, String>("turnNumber"));
			TableColumn<TurnInfo, String> p1Col = new TableColumn<TurnInfo, String>("Player 1");
			p1Col.setCellValueFactory(new PropertyValueFactory<TurnInfo, String>("p1"));
			scoreboard.getColumns().setAll(Arrays.asList(turnCol, p1Col));
			TableColumn<TurnInfo, String> p2Col = new TableColumn<TurnInfo, String>("Player 2");
			p2Col.setCellValueFactory(new PropertyValueFactory<TurnInfo, String>("p2"));
			scoreboard.getColumns().setAll(Arrays.asList(turnCol, p1Col, p2Col));
			diePane = new DiePane(this);
			diePane.updateState(game, this);
			root.setCenter(diePane);
			root.setRight(scoreboard);
			root.setLeft(infoPane);
			refresh();

		} else if (evt.getSource() == diePane.getRollDieBtn()) {
			// Roll the dice
			game.rollDice();
			diePane.clearSelection();
			if (!game.detectFarkle()) {
				refresh();
			} else {
				// What to do when a player farkles
				String msg = game.getActive_player().getName() + " has farkled!";
				if (game.getActive_player().getFarkles() + 1 == 3) {
					msg += "\nYou Lose 1000 points!";
				}
				if (game.endOfRound()) {
					TurnInfo temp = data.get(data.size() - 1);
					data.remove(data.size() - 1);
					temp.setP2("FARKLE");
					data.add(new TurnInfo(temp.getTurnNumber(), temp.getP1(), temp.getP2()));
				} else {
					data.add(new TurnInfo(Integer.toString(game.getTurn_count()), "FARKLE", ""));
				}
				game.passTurn(true);
				diePane.farkle(msg, this);
			}

		} else if (evt.getSource() == diePane.getScoreDieBtn()) {
			// Active Player Scores a selection
			game.scoreSelection();
			diePane.clearSelection();
			refresh();
		} else if (evt.getSource() == diePane.getPassBtn()) {
			// Active Player passes turn
			if (game.endOfRound()) {
				TurnInfo temp = data.get(data.size() - 1);
				data.remove(data.size() - 1);
				temp.setP2(
						Integer.toString(game.getActive_player().getScore() + game.getActive_player().getTurnScore()));
				data.add(new TurnInfo(temp.getTurnNumber(), temp.getP1(), temp.getP2()));
			} else {
				data.add(new TurnInfo(Integer.toString(game.getTurn_count()),
						Integer.toString(game.getActive_player().getScore() + game.getActive_player().getTurnScore()),
						""));
			}
			game.passTurn(false);
			if (isAIturn()) {
				AIturn();
			} else {
				refresh();
			}
		}
	}

	/**
	 * Refresh the UI and alert the game of a selection change.
	 *
	 * @param i the i
	 */
	public void selectionRefresh(int i) {
		game.setSelection(diePane.getSelection().clone());
		refresh();
	}

	/**
	 * Refresh the state of the UI.
	 */
	public void refresh() {
		if (game.gameOver()) {
			gameOver();
		} else {
			diePane.updateState(game, this);
			infoPane.updateInfo(game);
		}
	}

	/**
	 * Game over screen.
	 */
	public void gameOver() {
		VBox gameOverBox = new VBox();
		gameOverBox.getStyleClass().add("pane");
		gameOverBox.setAlignment(Pos.CENTER);

		Label gameOverLbl = new Label("GAME OVER");
		gameOverLbl.setId("title");

		Label winnerLbl = new Label(game.getActive_player().getName().toUpperCase() + " WON!");
		winnerLbl.setId("big-info-lbl");

		startOverBtn = new Button("Start Over");
		startOverBtn.setOnAction(this);

		gameOverBox.getChildren().addAll(gameOverLbl, winnerLbl, startOverBtn);

		root.setLeft(null);
		root.setCenter(gameOverBox);
	}
	
	public boolean isAIturn() {
		return game.getActive_player() == game.getPlayer_2();
	}
	
	
	public void AIturn() {
		System.out.println("AI Turn");
		int i = 0;
		do {
			game.rollDice();
			System.out.println("Roll Again");
			if(game.detectFarkle()) {
				System.out.println("Farkle");
				game.passTurn(true);
				refresh();
				return;
			} else {
				boolean[] selection = ai.makeSelection(game);
				for(boolean b: selection){
					System.out.println(b);
				}
				game.setSelection(selection);
				System.out.println("Value of Selection: " + game.checkSelectionScore());
				game.scoreSelection();
			}
			i++;
		} while(i < 2);
		System.out.println("AI. I decided to be done.");
		game.passTurn(false);
		refresh();
	}
}