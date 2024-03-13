package app.jisaker.core;

import java.util.ArrayList;
import java.util.List;

public class CPUFactory {
    public static CPU getOne() throws Exception {
        if (Public.cpus.size() < 1) {
            throw new IllegalStateException("All CPUs are not loaded in the application.");
        }
        return Public.cpus.get(0);
    }
}
