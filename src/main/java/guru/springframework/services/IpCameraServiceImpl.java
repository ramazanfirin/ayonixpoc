package guru.springframework.services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import guru.springframework.domain.IpCamera;
import guru.springframework.repositories.IpCameraRepository;

@Service
public class IpCameraServiceImpl implements IpCameraService {
    private IpCameraRepository ipCameraRepository;

    @Autowired
    public void setIpCameraRepository(IpCameraRepository ipCameraRepository) {
        this.ipCameraRepository = ipCameraRepository;
    }

	@Override
	public List<IpCamera> findByName(String name) {
		return this.ipCameraRepository.findByName(name);
	}

	@Override
	public List<IpCamera> getAll() {
		return (List<IpCamera>)this.ipCameraRepository.findAll();
		//return null;
	}

   
}
