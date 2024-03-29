package scheadpp.core.Database.Repositories;

import scheadpp.core.Database.Entities.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<AppUser, String> {

    Boolean existsByVkId(Integer vkId);

    AppUser findByVkId(Integer vkId);

    Optional<AppUser> findById(String id);

    boolean existsByLogin(String login);

    AppUser findByLogin(String login);

    AppUser findByExternalIdAndUniversityId(String externalId, String universityId);
}
