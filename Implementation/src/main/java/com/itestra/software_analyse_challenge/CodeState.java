package com.itestra.software_analyse_challenge;

import org.apache.commons.lang3.StringUtils;

public enum CodeState {

    COMMENT(false, "//"),
    CODE(true, null),
    EMPTY_LINE(false, ""),
    BLOCK_COMMENT_START(false, "/*"),
    BLOCK_COMMENT(false, "*"),
    BLOCK_COMMENT_END(false, "*/"),
    GETTER(false, "public\\s+.+\\s+get[A-Z]\\w*\\s*\\(\\)\\s*\\{\\s*return\\s+(this\\.)?.+;\\s*\\}");

    private final boolean countLine;

    private final String pattern;

    CodeState(final boolean countLine, final String pattern) {
        this.countLine = countLine;
        this.pattern = pattern;
    }

    public static CodeState getCodeStateForLine(final boolean bonus, final String line) {
        if (line == null) {
            return EMPTY_LINE;
        }

        final String formattedLine = line.replaceAll("\n", "").replaceAll(" ", "");
        if (StringUtils.isBlank(formattedLine)) {
            return EMPTY_LINE;
        }

        if (formattedLine.startsWith(COMMENT.pattern)) {
            return COMMENT;
        }

        if (bonus && (formattedLine.startsWith(BLOCK_COMMENT.getPattern())
                || formattedLine.startsWith(BLOCK_COMMENT_START.getPattern())
                || formattedLine.startsWith(BLOCK_COMMENT_END.getPattern()))) {
            return BLOCK_COMMENT;
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
