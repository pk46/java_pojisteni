package cz.rekvalifikace.projekt.repository;

import cz.rekvalifikace.projekt.model.Pojisteni;
import org.springframework.data.repository.CrudRepository;
import java.util.Collection;

public interface PojisteniRepository extends CrudRepository<Pojisteni, Long> {

    Collection<Pojisteni> findByPojistenecId(long pojistenecId);
}
