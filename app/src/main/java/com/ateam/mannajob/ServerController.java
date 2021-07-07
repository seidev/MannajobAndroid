package com.ateam.mannajob;

import java.util.HashMap;
import java.util.Map;

public interface ServerController {
    public void ServerSend(String cmd, Map<String,String> params);
}
