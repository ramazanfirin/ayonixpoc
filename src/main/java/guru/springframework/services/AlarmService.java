package guru.springframework.services;

import java.util.Date;
import java.util.List;

import guru.springframework.domain.Alarm;

public interface AlarmService {
	public void save(Alarm alarm);
    public List<Alarm> getAll();
    public List<Alarm> search(Date startDate,Date endDate);
}
