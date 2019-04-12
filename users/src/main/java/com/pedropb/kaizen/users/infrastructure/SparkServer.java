package com.pedropb.kaizen.users.infrastructure;

import java.util.Arrays;

public class SparkServer {
    public void register(Resource... resources) {
        Arrays.stream(resources)
              .forEach(Resource::configure);
    }
}
