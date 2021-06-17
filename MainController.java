package dofCalculator;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class MainController implements Initializable{
	
	@FXML
	TextField inputFocalLength;
	@FXML
	TextField inputAperature;
	@FXML
	TextField inputSensorSize;
	@FXML
	TextField inputDistance;
	@FXML
	Slider s1;
	@FXML
	Slider s2;
	@FXML
	Slider s3;
	@FXML
	Slider s4;
	@FXML
	Label resultLabel;
	@FXML
	ImageView iv1;
	@FXML
	Pane p1;
	
	int n = 0;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		s1.valueProperty().addListener((obs, oldval, newVal) -> {
			s1.setValue(newVal.intValue());
			inputFocalLength.setText(String.valueOf(newVal.intValue()));
		});
		inputFocalLength.textProperty().addListener((obs, oldval, newVal) -> {
			if(!newVal.equals("")) {
				s1.setValue(Integer.valueOf(newVal));				
			}
		});
		
		s2.valueProperty().addListener((obs, oldval, newVal) -> {
			s2.setValue(Double.valueOf(new DecimalFormat("#.#").format(newVal)));
			inputAperature.setText(new DecimalFormat("#.#").format(newVal));
		});
		inputAperature.textProperty().addListener((obs, oldval, newVal) -> {
			if(!newVal.equals("")) {
				s2.setValue(Double.valueOf(newVal));				
			}
		});
		
		s3.valueProperty().addListener((obs, oldval, newVal) -> {
			s3.setValue(Double.valueOf(new DecimalFormat("#.##").format(newVal)));
			inputSensorSize.setText(new DecimalFormat("#.##").format(newVal));
		});
		inputSensorSize.textProperty().addListener((obs, oldval, newVal) -> {
			if(!newVal.equals("")) {
				s3.setValue(Double.valueOf(newVal));				
			}
		});
		
		s4.valueProperty().addListener((obs, oldval, newVal) -> {
			s4.setValue(Double.valueOf(new DecimalFormat("#.#").format(newVal)));
			inputDistance.setText(new DecimalFormat("#.#").format(newVal));
		});
		inputDistance.textProperty().addListener((obs, oldval, newVal) -> {
			if(!newVal.equals("")) {
				s4.setValue(Double.valueOf(newVal));				
			}
		});
	
	}
	
	@FXML
	public void onBackPressed() {
		Initial.currentStage.setScene(Initial.menuScene);
	}
	
	@FXML
	public void onSimulate() {
		
		if(!inputFocalLength.getText().equals("") && !inputAperature.getText().equals("") && !inputSensorSize.getText().equals("") && !inputDistance.getText().equals("")) {
			Calculate();
		}
		
		loading();
	}
	
	//This function is to calculate the DOF, Please refactor the part of the "sensersize"
	public void Calculate() {
		
		
		
		String rawFocallength = inputFocalLength.getText();
		double focallength = Double.parseDouble(rawFocallength);
		
		
		
		System.out.println(focallength + "mm");
		
		
		String rawAperature = inputAperature.getText();
		double aperature = Double.parseDouble(rawAperature);
		
		System.out.println("f" +  aperature);
		
		String SensorType = inputSensorSize.getText();
		
		String rawfDistance = inputDistance.getText();
		double distance = Double.parseDouble(rawfDistance);
		
		System.out.println(distance + "m");
		
		
		double coc = selectCircleOfConfusion(SensorType);
		
		System.out.println(coc);
		
		
		double hyperfocal = calculateHyperfocal(focallength,aperature,coc);
		
		System.out.println("hyperfocal: " + hyperfocal);
		
		
		DOFresult result = calculateDOF(hyperfocal, distance, focallength);
		
		
		double NP = result.getNearPoint()/1000;
		double FP = result.getFarPoint()/1000;
		double DOF = result.getDOF()/1000;
		
		String NPStr = String.valueOf(NP);
		String FPStr = String.valueOf(FP);
		String DOFStr = String.valueOf(DOF);
	
		
		System.out.println(NPStr);
		System.out.println(FPStr);
		System.out.println(DOFStr);
		resultLabel.setText("Near Point: " + NPStr + "m \n" +
							"Far Point: " + FPStr + "m \n" +
							"Depth of Field: " + DOFStr + "m \n");
		
	}
	
	
	public static double calculateHyperfocal(double focallength, double aperature, double coc) {
		double hyperfocal = (focallength * focallength)/(aperature * coc);
		return hyperfocal;
	}
	
	
	public static double selectCircleOfConfusion(String sensorSize) {
		double ff = 0.02501;
		double apsc = 0.019948;
		if (sensorSize == "Fullframe") {
			return ff;
		}else if(sensorSize == "APSC" ) {
			return apsc;
			}else {
				//default
				return ff;
			}
	}
	
	
	public static DOFresult calculateDOF(double hyperfocal, double distance, double focallength){
		double nearPoint = (hyperfocal * distance*1000)/(hyperfocal + (distance*1000 - focallength));
		double farPoint = (hyperfocal * distance*1000)/(hyperfocal - (distance*1000 - focallength));
		DOFresult result = new DOFresult(nearPoint,farPoint);
		return result;
	}
	
	public void loading() {
		iv1.setVisible(true);
		p1.setVisible(true);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			  @Override
			  public void run() {
			    iv1.setVisible(false);
			    p1.setVisible(false);
			  }
			}, 3000);
	}
}
