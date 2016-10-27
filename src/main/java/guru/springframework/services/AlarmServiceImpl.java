package guru.springframework.services;

import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import guru.springframework.domain.Alarm;
import guru.springframework.repositories.AlarmRepository;

@Service
public class AlarmServiceImpl implements AlarmService {
    private AlarmRepository alarmRepository;

    @Autowired
    public void setAlarmRepository(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

	@Override
	public void save(Alarm alarm) {
		alarmRepository.save(alarm);
		
	}

	@Override
	public List<Alarm> getAll() {
		return (List<Alarm>)this.alarmRepository.findAll();
	}

	@Override
	@Query("SELECT o FROM Alarm o WHERE o.insertDate BETWEEN :from AND :to ")
	public List<Alarm> search( Date startDate, Date endDate) {
		return this.alarmRepository.search(startDate, endDate);
	}

   
}
