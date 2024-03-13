package app.jisaker.core;

import java.util.ArrayList;
import java.util.List;

public class GPUFactory {

    public static GPU getOne() {
        if (Public.gpus.size() < 1) {
            throw new IllegalStateException("All GPUs are not loaded in the application.");
        }
        return Public.gpus.get(0);
    }
}
