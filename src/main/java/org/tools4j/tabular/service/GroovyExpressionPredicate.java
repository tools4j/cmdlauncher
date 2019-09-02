package org.tools4j.tabular.service;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.log4j.Logger;
import org.tools4j.tabular.util.PropertiesRepo;
import org.tools4j.tabular.util.ResolvedString;

import java.util.function.Predicate;

/**
 * User: ben
 * Date: 8/11/17
 * Time: 5:14 PM
 */
public class GroovyExpressionPredicate implements Predicate<Row> {
    private final static Logger LOG = Logger.getLogger(GroovyExpressionPredicate.class);
    private final String groovyExpression;
    private final PropertiesRepo propertiesRepo;

    public GroovyExpressionPredicate(final String groovyExpression, final PropertiesRepo propertiesRepo) {
        this.propertiesRepo = propertiesRepo;
        this.groovyExpression = groovyExpression;
    }

    @Override
    public boolean test(Row row) {
        //Test for simple true, or empty string
        if(groovyExpression.trim().equalsIgnoreCase("true") || groovyExpression.trim().length() == 0){
            return true;
        }

        final Object evaluatedResult = new GroovyExpression(groovyExpression, propertiesRepo, Boolean.class).resolveExpression(row);
        return (Boolean) evaluatedResult;
    }
}
