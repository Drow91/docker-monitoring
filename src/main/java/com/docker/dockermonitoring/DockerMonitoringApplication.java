package com.docker.dockermonitoring;
import com.docker.dockermonitoring.model.DockerContainersMonitoringService;
import com.docker.dockermonitoring.model.DockerMonitoringInterface;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@EnableAsync
@SpringBootApplication

public class DockerMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerMonitoringApplication.class, args);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<?> future = executor.submit(() -> {
            DockerMonitoringInterface monitoringInterface = new DockerMonitoringInterface();
            monitoringInterface.run();
        });
        executor.submit(() -> {
            DockerContainersMonitoringService updateStatus = new DockerContainersMonitoringService();
            while (!future.isDone()) {
                updateStatus.runService();
            }
        });



        executor.shutdown();

    }
}
