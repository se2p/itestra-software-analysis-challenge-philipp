package com.itestra.software_analyse_challenge;

public class LineCounter {

    public int readFile(String fileContent) {
        int counter = 0;
        for (final String line : fileContent.split("\n")) {
            CodeState currentState = CodeState.getCodeStateForLine(line);
            if (currentState.isCountLine()) {
                counter++;
            }
        }
        return counter;
    }

    public int readFileBonus() {
        return 0;
    }

}
