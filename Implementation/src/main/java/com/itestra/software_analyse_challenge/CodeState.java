package com.itestra.software_analyse_challenge;

import org.apache.commons.lang3.StringUtils;

public enum CodeState {

    LINE_COMMENT(false, "//"),
    CODE(true, null),
    EMPTY_LINE(false, ""),
    GETTER(false, "public\\s+.+\\s+get[A-Z]\\w*\\s*\\(\\)\\s*\\{\\s*return\\s+(this\\.)?.+;\\s*\\}");

    private final boolean countLine;

    private final String pattern;

    CodeState(final boolean countLine, final String pattern) {
        this.countLine = countLine;
        this.pattern = pattern;
    }

    public static CodeState getCodeStateForStartingLine(final String line) {
        if (line == null) {
            return EMPTY_LINE;
        }

        final String formattedLine = line.replaceAll(" ", "");
        if (StringUtils.isBlank(formattedLine)) {
            return EMPTY_LINE;
        }

        if (formattedLine.startsWith(LINE_COMMENT.pattern)) {
            return LINE_COMMENT;
        }

        return CODE;
    }

    public boolean isCountLine() {
        return countLine;
    }

    public String getPattern() {
        return pattern;
    }
}
