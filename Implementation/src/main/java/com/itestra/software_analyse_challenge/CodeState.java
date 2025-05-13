package com.itestra.software_analyse_challenge;

import org.apache.commons.lang3.StringUtils;

public enum CodeState {

    COMMENT(false), CODE(true), EMPTY_LINE(false);

    private CodeState(final boolean countLine) {
        this.countLine = countLine;
    }

    private boolean countLine;

    public static CodeState getCodeStateForLine(final String line) {
        if(line == null) {
            return EMPTY_LINE;
        }

        final String formattedLine = line.replaceAll("\n", "").replaceAll(" ", "");
        if(StringUtils.isBlank(formattedLine)) {
            return EMPTY_LINE;
        }

        if(formattedLine.startsWith("//")) {
            return COMMENT;
        }

        return CODE;
    }

    public boolean isCountLine() {
        return countLine;
    }
}
