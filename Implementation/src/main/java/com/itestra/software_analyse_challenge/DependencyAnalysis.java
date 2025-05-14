package com.itestra.software_analyse_challenge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DependencyAnalysis {

    private final static List<String> ALLOWED_PROJECTS = List.of("cronutils", "fig", "spark");
    private final static String ALLOWED_PATTERN = "import\\s+(cronutils|fig|spark)\\.(?!.*\\.\\*\\s*;).*;";
    private final Map<String, Set<String>> fileToDependencies = new HashMap<>(); // Files already evaluated do not need to be evaluated again

    public Set<String> getProjectDependenciesForPath(final Path inputDirectory, Path filePath) throws IOException {
        final Set<String> dependenciesForPath = evaluateDependenciesForPath(inputDirectory, filePath);
        return filterProjectName(inputDirectory, dependenciesForPath);
    }

    private Set<String> evaluateDependenciesForPath(final Path inputDirectory, final Path filePath) throws IOException {
        if (filePath == null) {
            return Set.of();
        }

        if (fileToDependencies.containsKey(filePath.toString())) {
            return fileToDependencies.get(filePath.toString());
        }

        final Set<String> dependencies = new HashSet<>();

        // First time analysing file fill map
        // so that other files having this file as a dependency
        // do not run in an infinite loop
        fileToDependencies.put(filePath.toString(), dependencies);

        final String content = Files.readString(filePath);
        final Matcher importMatcher = Pattern
                .compile(ALLOWED_PATTERN)
                .matcher(content);

        while (importMatcher.find()) {
            final Path dependency = convertImportToPath(inputDirectory, importMatcher.group());
            // current dependency
            dependencies.add(dependency.toString());
            // dependencies of all indirect dependencies
            dependencies.addAll(evaluateDependenciesForPath(inputDirectory, dependency));
        }

        fileToDependencies.put(filePath.toString(), dependencies);

        return dependencies;
    }

    private Set<String> filterProjectName(final Path inputDirectory, Set<String> fileNames) {
        final Set<String> result = new HashSet<>();
        for (String file : fileNames) {
            String[] split = file.replace(inputDirectory.toString() + "\\", "").split("\\\\");
            if (split.length > 0 && ALLOWED_PROJECTS.contains(split[0])) {
                result.add(split[0]);
            }
        }
        return result;
    }

    private Path convertImportToPath(final Path inputDirectory, final String importStatement) {
        final String formattedPath = importStatement
                .replaceAll("import", "")
                .replaceAll("\\.", "/")
                .replaceAll(";", "")
                .replaceAll(" ", "");

        return Paths.get(inputDirectory.toString() + "\\" + formattedPath + ".java");
    }
}
