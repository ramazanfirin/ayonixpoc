package guru.springframework.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import guru.springframework.domain.Alarm;

public interface AlarmRepository extends CrudRepository<Alarm, Integer>{

	@Query("SELECT o FROM Alarm o WHERE o.insertDate BETWEEN :from AND :to order by o.id desc")	
	 public List<Alarm> search(	@Param("from")  Date startDate, @Param("to")  Date endDate) ;
	
}
