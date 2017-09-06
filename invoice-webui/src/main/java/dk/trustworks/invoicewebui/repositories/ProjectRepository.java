package dk.trustworks.invoicewebui.repositories;

/**
 * Created by hans on 27/06/2017.
 */

import dk.trustworks.invoicewebui.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "projects",path="projects")
public interface ProjectRepository extends CrudRepository<Project, String> {
    @Override @RestResource(exported = false) void delete(String id);
    @Override @RestResource(exported = false) void delete(Project entity);
}