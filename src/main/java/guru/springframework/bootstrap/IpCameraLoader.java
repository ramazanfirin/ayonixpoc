package guru.springframework.bootstrap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import guru.springframework.domain.IpCamera;
import guru.springframework.repositories.IpCameraRepository;

@Component
public class IpCameraLoader implements ApplicationListener<ContextRefreshedEvent> {

    private IpCameraRepository ipCameraRepository;

    private Logger log = Logger.getLogger(IpCameraLoader.class);

    @Autowired
    public void setIpCameraRepository(IpCameraRepository productRepository) {
        this.ipCameraRepository = productRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    	if(ipCameraRepository.findByName("Edimax").size()==0){
        IpCamera ipCamera = new IpCamera();
        ipCamera.setAlias("Edimax");
        ipCamera.setConnectionURL("http://192.168.1.108/axis-cgi/mjpg/video.cgi?camera=2");
        ipCamera.setName("Edimax");
        ipCamera.setType("MJPEG");
        ipCamera.setUsername("admin");
        ipCamera.setPassword("1234");
        ipCameraRepository.save(ipCamera);
    	}
        
    	if(ipCameraRepository.findByName("Dahua").size()==0){
    	IpCamera ipCamera2 = new IpCamera();
        ipCamera2.setAlias("Dahua");
        ipCamera2.setConnectionURL("http://192.168.1.108/axis-cgi/mjpg/video.cgi?camera=2");
        ipCamera2.setName("Dahua");
        ipCamera2.setType("MJPEG");
        ipCamera2.setUsername("admin");
        ipCamera2.setPassword("admin");
        
        ipCameraRepository.save(ipCamera2);
    	}
    }
}
