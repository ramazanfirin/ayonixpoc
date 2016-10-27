package guru.springframework.repositories;

import org.springframework.data.repository.CrudRepository;

import guru.springframework.domain.PersonCategory;

public interface PersonCategoryRepository extends CrudRepository<PersonCategory, Integer>{
}
