package guru.springframework.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import guru.springframework.domain.Person;
import guru.springframework.repositories.PersonRepository;

@Service
public class PersonServiceImpl implements PersonService {
    private PersonRepository personRepository;

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

	@Override
	public void save(Person person) {
		this.personRepository.save(person);
		
	}

	@Override
	public List<Person> getAll() {
		return (List<Person>)this.personRepository.findAll();
	}

	@Override
	public void delete(Person person) {
		this.personRepository.delete(person);
		
	}

   
}
