package guru.springframework.repositories;

import org.springframework.data.repository.CrudRepository;

import guru.springframework.domain.Person;

public interface PersonRepository extends CrudRepository<Person, Integer>{
}
