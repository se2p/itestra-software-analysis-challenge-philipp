package com.itestra.software_analyse_challenge;

public class LineCounter {

    public static int readFile(final String fileContent) {
        int counter = 0;
        for (final String line : fileContent.split("\n")) {
            CodeState currentState = CodeState.getCodeStateForLine(false, line);
            if (currentState.isCountLine()) {
                counter++;
            }
        }
        return counter;
    }

    public static int readFileBonus(final String fileContent) {
        int counterBonus = 0;
        final String contentWithoutGetters = fileContent.replaceAll(CodeState.GETTER.getPattern(), "\n");
        for (final String line : contentWithoutGetters.split("\n")) {
            CodeState currentState = CodeState.getCodeStateForLine(true, line);
            if (currentState.isCountLine()) {
                counterBonus++;
            }
        }
        return counterBonus;
    }

}
