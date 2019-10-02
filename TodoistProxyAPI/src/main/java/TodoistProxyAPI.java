import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;

@Api(name = "todoist-proxy-api", version = "v1")
public class TodoistProxyAPI {

    @ApiMethod(name = "echo")
    public String echo() {
        return "ok";
    }
}
