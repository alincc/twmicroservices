package dk.trustworks.invoicewebui.repositories;

/**
 * Created by hans on 27/06/2017.
 */

import dk.trustworks.invoicewebui.model.Task;
import dk.trustworks.invoicewebui.model.Taskworkerconstraint;
import dk.trustworks.invoicewebui.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "taskworkerconstraints", path="taskworkerconstraints")
public interface TaskworkerconstraintRepository extends CrudRepository<Taskworkerconstraint, String> {

    List<Taskworkerconstraint> findByTask(Task task);

    List<Taskworkerconstraint> findByTaskAndUser(Task task, User user);

    @Override @RestResource(exported = false) void delete(String id);
    @Override @RestResource(exported = false) void delete(Taskworkerconstraint entity);
}
