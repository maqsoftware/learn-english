package com.maqautocognita.constant;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public enum SentenceClauseType {

    SUBJECT("subject"), ACTION("action"), OBJECT("object");

    private String clause;

    SentenceClauseType(String clause) {
        this.clause = clause;
    }

    public boolean isEquals(String givenClause) {
        return clause.equalsIgnoreCase(givenClause);
    }

}
