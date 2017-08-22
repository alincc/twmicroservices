package dk.trustworks.invoicewebui.network.clients;

import dk.trustworks.invoicewebui.network.dto.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient("user-service")
public interface UserClient {

    @RequestMapping(method = GET, value = "/users")
    Resources<Resource<User>> findAllUsers();

    @RequestMapping(method = GET, value = "/users/search/findByActiveTrue")
    Resources<Resource<User>> findAllActiveUsers();

}
