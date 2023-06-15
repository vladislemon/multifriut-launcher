package ntzw.mfl.launch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Launcher {

    private LaunchArguments arguments;
    private String[] javaArgs;
    private String javaPath;

    public Launcher(LaunchArguments arguments, String javaPath, String[] javaArgs) {
        this.arguments = arguments;
        this.javaPath = javaPath;
        this.javaArgs = javaArgs;
    }

    public Launcher(LaunchArguments arguments, String... javaArgs) {
        this(arguments, "java", javaArgs);
    }

    public Process runMinecraft(boolean redirectOutput) throws IOException {
        List<String> runCmd = new ArrayList<>();
        runCmd.add(javaPath);
        if(javaArgs != null) {
            runCmd.addAll(Arrays.asList(javaArgs));
        }
        runCmd.addAll(arguments.asList());
        ProcessBuilder processBuilder = new ProcessBuilder(runCmd);
        processBuilder.directory(new File(arguments.getGameDir()));
        if(redirectOutput) {
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        }

        //DEBUG
        for(String s : processBuilder.command()) {
            System.out.println(s);
        }
        return processBuilder.start();
    }
}
