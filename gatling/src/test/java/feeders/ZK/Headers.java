package feeders.ZK;

import java.util.HashMap;
import java.util.Map;

public class Headers {
    public static Map<String, String> getCommonHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("rq-uuid", "#{rqUuid}");
        headers.put("source-system-id", "GATLING");
        headers.put("destination-system-id", "ZK");
        return headers;
    }
}
