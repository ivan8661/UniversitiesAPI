package scheadpp.core.Database.Repositories;

import scheadpp.core.Modules.Universities.Entities.University;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends CrudRepository<University, Integer> {
}
