package guru.springframework.swing.components.listener;

import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;

public class IpCameraDiscoveryListener implements WebcamDiscoveryListener{

	@Override
	public void webcamFound(WebcamDiscoveryEvent event) {
		System.out.println("webcam found:"+event.getWebcam().getName());
		
	}

	@Override
	public void webcamGone(WebcamDiscoveryEvent event) {
		System.out.println("webcam gone:"+event.getWebcam().getName());
		
	}

}
