package application;

import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SystemController {

	@FXML
	private Pane pane;
	@FXML
	private Pane paneTest;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private Text resText;
	@FXML
	private Text testSign;

	@FXML
	private void handleTestBtn() {
		progressBar.setProgress(0.70);
		Timer timer = new Timer();

		timer.scheduleAtFixedRate(new TimerTask() {
			double count = 0.0;

			@Override
			public void run() {
				// System.out.println("timer");
				count = count + 0.25;
				progressBar.setProgress(count);
				if (count == 1.0) {
					timer.cancel();
					timer.purge();
					int rnd = 50 + (int) (Math.random() * ((151 - 80) + 1));
					String res = rnd + " W";
					resText.setText(res);
					if (rnd > 85 && rnd < 140) {
						System.out.println("test passed");
						testSign.setText("\u2713");
						testSign.setFill(Color.GREEN);
					} else {
						System.out.println("test failed");
						testSign.setText("\u2718");
						testSign.setFill(Color.RED);
					}
					return;
				}
			}
		}, 0, 1000);

	}

	@FXML
	private void handleSetBtn() {
		System.out.println("Set Production Values");
		String nameRowLabel = "";
		String resText = "";
		boolean allGreen = true;
		for (Node node : pane.getChildren()) {
			// System.out.println("Node data: " + " " + node.getTypeSelector() + " " +
			// node.getUserData());
			if (node instanceof Label) {
				nameRowLabel = ((Label) node).getText();
			}
			// System.out.println(nameRowLabel);
			if (node instanceof TextField) {
				if (nameRowLabel.compareTo("Serial number (0-100)") == 0) {
					resText = verifySerialNumber(((TextField) node).getText());
				} else if (nameRowLabel.compareTo("Software version (0.0-10.0)") == 0) {
					resText = verifySoftwareVersion(((TextField) node).getText());
				} else if (nameRowLabel.compareTo("Sensor serial nr. (12 ASCII)") == 0) {
					resText = verifySensorSerialNumber(((TextField) node).getText());
				} else if (nameRowLabel.compareTo("Motor serial nr. (0-30)") == 0
						|| nameRowLabel.compareTo("Board serial nr. (0-30)") == 0) {
					resText = verifyMotorOrBoardSerialNumber(((TextField) node).getText());
				}
			}
			if (node instanceof Text) {
				if (resText.compareTo("") != 0) {
					if (resText.compareTo("\u2713") == 0) {
						((Text) node).setText(resText);
						((Text) node).setFill(Color.GREEN);
					} else {
						((Text) node).setText(resText);
						((Text) node).setFill(Color.RED);
						allGreen = false;
					}
					resText = "";
				}
			}
		}
		if (allGreen) {
			System.out.println("all green");
			fadeIn.setNode(paneTest);
			fadeIn.setFromValue(0.0);
			fadeIn.setToValue(1.0);
			fadeIn.setCycleCount(1);
			fadeIn.setAutoReverse(false);
			paneTest.setVisible(true);
			fadeIn.playFromStart();
		}
	}

	private FadeTransition fadeIn = new FadeTransition(Duration.millis(3000));

	private String verifySerialNumber(String text) {
		String res = "";
		if (text.matches("-?[1-9]\\d*|0")) {
			long number = Long.parseLong(text);
			if (number >= 0) {
				if (number < 100) {
					res = "\u2713";
				} else {
					res = "Number must be smaller than 100";
				}
			} else {
				res = "Number must be positive";
			}
		} else {
			res = "Must be an integer number";
		}
		return res;
	}

	private String verifySoftwareVersion(String text) {
		String res = "";
		if (text.matches("-?[0-9]*\\.?[0-9]+?")) {
			double number = Double.parseDouble(text);
			if (number >= 0) {
				if (number < 10.0) {
					res = "\u2713";
				} else {
					res = "Number must be smaller than 10.0";
				}

			} else {
				res = "Number must be positive";
			}
		} else {
			res = "Must be a number of type float or integer";
		}
		return res;
	}

	private String verifyMotorOrBoardSerialNumber(String text) {
		String res = "";
		if (text.matches("-?[1-9]\\d*|0")) {
			long number = Long.parseLong(text);
			if (number >= 0) {
				if (number < 30) {
					res = "\u2713";
				} else {
					res = "Number must be smaller than 30";
				}
			} else {
				res = "Number must be positive";
			}
		} else {
			res = "Must be an integer number";
		}
		return res;
	}

	private String verifySensorSerialNumber(String text) {
		String res = "";
		if (Charset.forName("ASCII").newEncoder().canEncode(text)) {
			if (text.length() < 12) {
				res = "\u2713";
			} else {
				res = "Serial must have maximum 12 ASCII digits";
			}
		} else {
			res = "Must contain ASCII digits";
		}
		return res;
	}

}
