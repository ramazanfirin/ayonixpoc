package guru.springframework.services;

import java.util.Collection;
import java.util.List;

import guru.springframework.domain.IpCamera;

public interface IpCameraService {
	List<IpCamera> findByName(String name);
	public Collection<IpCamera> getAll();
}
