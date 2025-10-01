package com.github.yancap.artech;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;

public class TestScriptExecutor {

    @ConfigProperty(name = "application.script.bash.path")
    String scriptLinuxPath; // Caminho do script (defina isso no application.properties)
    @ConfigProperty(name = "application.script.powershell.path")
    String scriptWindowsPath; // Caminho do script (defina isso no application.properties)

    public void onStart(@Observes StartupEvent ev) {
        try {
            String os = System.getProperty("os.name").toLowerCase(); // Verifica o sistema operacional
            ProcessBuilder processBuilder;

            // Verifica se é Windows ou Linux/Mac
            if (os.contains("win")) {
                processBuilder = new ProcessBuilder("powershell", "-ExecutionPolicy", "Bypass", "-File", scriptWindowsPath);
            } else {
                processBuilder = new ProcessBuilder("bash", scriptLinuxPath);
            }
            Process process = processBuilder.start();
            process.waitFor(); // Aguarda a conclusão da execução do scr
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
