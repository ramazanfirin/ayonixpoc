package guru.springframework.services;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import guru.springframework.domain.Afid;
import guru.springframework.repositories.AfidRepository;

@Service
public class AfidServiceImpl implements AfidService {
    private AfidRepository afidRepository;

    @Autowired
    public void setAfidRepository(AfidRepository afidRepository) {
        this.afidRepository = afidRepository;
    }

	@Override
	public void deleteAll(Collection<Afid> list) {
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Afid afid = (Afid) iterator.next();
			afidRepository.delete(afid);
		}
		
	}

	@Override
	public List<Afid> getAll() {
		return (List<Afid>)afidRepository.findAll();
	}

   
}
