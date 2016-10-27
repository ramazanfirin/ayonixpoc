package guru.springframework.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import guru.springframework.domain.IpCamera;

public interface IpCameraRepository extends CrudRepository<IpCamera, Integer>{

	  List<IpCamera> findByName(String name);
}
