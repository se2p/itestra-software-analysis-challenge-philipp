package com.itestra.software_analyse_challenge;

import java.util.List;

public class LineCounter {

    public int readFile(List<String> fileContent) {
        int counter = 0;
        for (final String line : fileContent) {
            CodeState currentState = CodeState.getCodeStateForLine(line);
            if (currentState.isCountLine()) {
                counter++;
            }
        }
        return counter;
    }

    public int readFileBonus(List<String> fileContent) {
        int counterBonus = 0;
        for (final String line : fileContent) {

        }
        return 0;
    }

}
