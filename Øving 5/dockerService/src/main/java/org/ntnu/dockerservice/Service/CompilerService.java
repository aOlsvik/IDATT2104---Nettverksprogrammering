package org.ntnu.dockerservice.Service;

import org.ntnu.dockerservice.Model.UserCode;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class CompilerService {
    public UserCode compileAndRun(UserCode code) throws IOException {
        // Bugfix where double quotes would cause error in compiling
        String codeString = code.getCode().replace("\"", "'");

        // the command to run
        String[] command = {"docker", "run", "--rm", "python:latest", "python", "-c", codeString};

        // Run the docker command given above
        Process process = Runtime.getRuntime().exec(command);

        // Get potential error messages
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder errorOutput = new StringBuilder();
        String errorLine;
        while ((errorLine = errorReader.readLine()) != null) {
            errorOutput.append(errorLine).append("\n");
        }

        // Get the code output
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        // Append the error message if there is any
        if (errorOutput.length() != 0) {
            output.append(errorOutput);
        }

        // Wait for the process to finish
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Assign the output to the code object and return it
        code.setOutput(output.toString());
        return code;
    }
}
