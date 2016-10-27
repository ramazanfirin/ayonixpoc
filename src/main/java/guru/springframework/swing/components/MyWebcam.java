package guru.springframework.swing.components;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDevice;

public class MyWebcam extends Webcam {

	protected Boolean startFaceIdentification;	
	
	protected MyWebcam(WebcamDevice device) {
		super(device);
		// TODO Auto-generated constructor stub
	}

	public Boolean getStartFaceIdentification() {
		return startFaceIdentification;
	}

	public void setStartFaceIdentification(Boolean startFaceIdentification) {
		this.startFaceIdentification = startFaceIdentification;
	}

}
