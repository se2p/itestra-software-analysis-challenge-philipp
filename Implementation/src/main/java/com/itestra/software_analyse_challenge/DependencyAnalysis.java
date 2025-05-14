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

    public Set<String> getProjectDependenciesForPath(Path path) throws IOException {
        return filterProjectName(evaluateDependenciesForPath(path));
    }

    private Set<String> evaluateDependenciesForPath(final Path path) throws IOException {
        if (path == null) {
            return Set.of();
        }

        if (fileToDependencies.containsKey(path.toString())) {
            return fileToDependencies.get(path.toString());
        }

        final Set<String> dependencies = new HashSet<>();

        // First time analysing file fill map
        // so that other files having this file as a dependency
        // do not run in an infinite loop
        fileToDependencies.put(path.toString(), dependencies);

        final String content = Files.readString(path);
        final Matcher importMatcher = Pattern
                .compile(ALLOWED_PATTERN)
                .matcher(content);

        while (importMatcher.find()) {
            final Path dependency = convertImportToPath(importMatcher.group());
            // current dependency
            dependencies.add(dependency.toString());
            // dependencies of all indirect dependencies
            dependencies.addAll(evaluateDependenciesForPath(dependency));
        }

        fileToDependencies.put(path.toString(), dependencies);

        return dependencies;
    }

    private Set<String> filterProjectName(Set<String> fileNames) {
        final Set<String> result = new HashSet<>();
        for (String file : fileNames) {
            String[] split = file.replace(SourceCodeAnalyser.DEFAULT_INPUT_DIR, "").split("\\\\");
            if (split.length > 0 && ALLOWED_PROJECTS.contains(split[0])) {
                result.add(split[0]);
            }
        }
        return result;
    }

    private Path convertImportToPath(final String importStatement) {
        final String formattedPath = importStatement
                .replaceAll("import", "")
                .replaceAll("\\.", "/")
                .replaceAll(";", "")
                .replaceAll(" ", "");

        return Paths.get(SourceCodeAnalyser.DEFAULT_INPUT_DIR + formattedPath + ".java");
    }
}
