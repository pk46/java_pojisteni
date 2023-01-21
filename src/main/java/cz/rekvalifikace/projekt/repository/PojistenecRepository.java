package cz.rekvalifikace.projekt.repository;

import cz.rekvalifikace.projekt.model.Pojistenec;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PojistenecRepository extends PagingAndSortingRepository<Pojistenec, Long> {

    Pojistenec findByEmail(String email);
    Pojistenec findByPojisteniId(Long id);
}
