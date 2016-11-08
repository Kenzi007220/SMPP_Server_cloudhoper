package mock_smpp_server.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerMain {

    @GetMapping("/send/{client}")
    public    String getting(@PathVariable Integer client)
    {


        return "yes";
    }


}