package com.example;
import javafx.application.Application;
import javafx.stage.Stage;
import com.example.view.ViewHandler;

/**
 * A main class used for launching the new graphical user interface built with SceneBuilder.
 * @author hannemsalmi, willeKoodaus, Katanpe, MinaSofi
 */
public class MainView extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		ViewHandler vh = new ViewHandler(primaryStage);
		vh.start();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
