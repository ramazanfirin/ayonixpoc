package guru.springframework.services;

import java.util.Collection;
import java.util.List;

import guru.springframework.domain.Afid;
import guru.springframework.domain.Person;

public interface AfidService {
    public void deleteAll(Collection<Afid> collection);
    public List<Afid> getAll();
}
