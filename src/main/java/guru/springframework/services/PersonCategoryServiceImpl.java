package guru.springframework.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import guru.springframework.repositories.IpCameraRepository;
import guru.springframework.repositories.PersonCategoryRepository;

@Service
public class PersonCategoryServiceImpl implements PersonCategoryService {
    private PersonCategoryRepository personCategoryRepository;

    @Autowired
    public void setPersonCategoryRepository(PersonCategoryRepository personCategoryRepository) {
        this.personCategoryRepository = personCategoryRepository;
    }

   
}
