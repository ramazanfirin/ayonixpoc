package guru.springframework.services;

import java.util.List;

import guru.springframework.domain.Person;

public interface PersonService {
    public void save(Person person);
    public List<Person> getAll();
    public void delete(Person person);

}
