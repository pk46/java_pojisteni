package cz.rekvalifikace.projekt.repository;

import cz.rekvalifikace.projekt.model.Pojisteni;
import org.springframework.data.repository.CrudRepository;
import java.util.Collection;

public interface PojisteniRepository extends CrudRepository<Pojisteni, Long> {

//    @Query(value = "SELECT id FROM pojisteni WHERE pojistenec_id = ?1", nativeQuery = true)
//    Collection<Pojisteni> findByPojistenecId(long pojistenecId);

    Collection<Pojisteni> findByPojistenecId(long pojistenecId);
}
