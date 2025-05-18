package com.itestra.software_analyse_challenge;

import org.apache.commons.lang3.StringUtils;

public class LineCounter {

    public static int countSourceLinesOfCode(final String fileContent) {
        int counter = 0;
        for (final String line : fileContent.split("\r\n")) {
            CodeState currentState = CodeState.getCodeStateForStartingLine(line);
            if (currentState.isCountLine()) {
                counter++;
            }
        }
        return counter;
    }

    public static int countSourceLinesOfCodeBonus(final String fileContent) {
        boolean insideComment;
        boolean insideBlockComment = false;
        boolean insideString = false;
        boolean counteLine;
        int counterBonus = 0;

        CodeState currentState;

        final String contentWithoutGetters = fileContent
                // Remove getter pattern
                .replaceAll(CodeState.GETTER.getPattern(), "\r\n")
                // Treat multiline string as normal string
                .replaceAll("\"\"\"", "\"");

        for (final String line : contentWithoutGetters.split("\r\n")) {
            counteLine = false;
            insideComment = false;
            currentState = CodeState.getCodeStateForStartingLine(line);

            if (currentState != CodeState.CODE) {
                continue;
            }

            for (int i = 0; i < line.length(); i++) {
                char previousCharacter = i > 0 ? line.charAt(i - 1) : ' ';
                char currentCharacter = line.charAt(i);
                char nextCharacter = i + 1 < line.length() ? line.charAt(i + 1) : ' ';

                // Check if any type of comment occurs and set flag
                if (currentCharacter == '/') {
                    if (nextCharacter == '/') {
                        if (!insideBlockComment && !insideString) {
                            insideComment = true;
                            continue;
                        }
                    } else if (nextCharacter == '*') {
                        if (!insideComment && !insideBlockComment && !insideString) {
                            insideBlockComment = true;
                            continue;
                        }
                    } else if (previousCharacter == '*') {
                        if (!insideComment && insideBlockComment && !insideString) {
                            insideBlockComment = false;
                            continue;
                        }
                    }
                    // comments in strings can be ignored
                } else if (currentCharacter == '\"' && previousCharacter != '\\') {
                    if (!insideComment && !insideBlockComment) {
                        insideString = !insideString;
                    }
                }

                if (StringUtils.isNotBlank(String.valueOf(currentCharacter))) {
                    if (!insideComment && !insideBlockComment) {
                        counteLine = true;
                    }
                }
            }

            if (counteLine) {
                counterBonus++;
            }
        }
        return counterBonus;
    }

}
