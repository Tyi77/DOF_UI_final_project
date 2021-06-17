package dofCalculator;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MenuController {
	
	@FXML
	public void onStartPressed() throws IOException{
		Parent maze = FXMLLoader.load(getClass().getResource("main.fxml"));
		Scene mazeScene = new Scene(maze);
		mazeScene.getRoot().requestFocus();
		Initial.currentStage.setScene(mazeScene);
	}
	
	@FXML
	public void onExitPressed() {
		Initial.currentStage.close();
	}
}
