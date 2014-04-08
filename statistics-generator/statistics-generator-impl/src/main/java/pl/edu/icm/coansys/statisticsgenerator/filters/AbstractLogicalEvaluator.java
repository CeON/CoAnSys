/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.edu.icm.coansys.statisticsgenerator.filters;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
import com.fathzer.soft.javaluator.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractLogicalEvaluator extends AbstractEvaluator<String> {

    final static Operator AND = new Operator("and", 2, Operator.Associativity.LEFT, 2);
    final static Operator OR = new Operator("or", 2, Operator.Associativity.LEFT, 1);
    final static Operator NOT = new Operator("not", 1, Operator.Associativity.LEFT, 3);
    private static final Parameters PARAMETERS;

    static {
        PARAMETERS = new Parameters();
        PARAMETERS.add(AND);
        PARAMETERS.add(OR);
        PARAMETERS.add(NOT);
        PARAMETERS.addExpressionBracket(BracketPair.PARENTHESES);
    }

    public AbstractLogicalEvaluator() {
        super(PARAMETERS);
    }

    @Override
    protected String toValue(String literal, Object evaluationContext) {
        return literal;
    }

    private boolean getValue(String literal) {
        if (literal.endsWith("=true")) {
            return true;
        }
        if (literal.endsWith("=false")) {
            return false;
        }
        return evaluateLiteral(literal);
    }

    public abstract boolean evaluateLiteral(String literal);

    @Override
    protected String evaluate(Operator operator, Iterator<String> operands,
            Object evaluationContext) {
        List<String> tree = (List<String>) evaluationContext;
        String[] o = new String[operator.getOperandCount()];
        for (int i = 0; i < operator.getOperandCount(); i++) {
            o[i] = operands.next();
        }

        Boolean result;
        if (operator == OR) {
            result = getValue(o[0]) || getValue(o[1]);
        } else if (operator == AND) {
            result = getValue(o[0]) && getValue(o[1]);
        } else if (operator == NOT) {
            result = !getValue(o[0]);
        } else {
            throw new IllegalArgumentException();
        }

        String eval;
        if (operator.getOperandCount() == 2) {
            eval = "(" + o[0] + " " + operator.getSymbol() + " " + o[1] + ")=" + result;
        } else {
            eval = "(" + operator.getSymbol() + " " + o[0] + ")=" + result;
        }
        tree.add(eval);
        return eval;
    }

    public boolean evaluateExpression(String expression) {
        if (expression != null && !expression.trim().isEmpty()) {
            String evaluate = evaluate(expression, new ArrayList<String>());
            return evaluate.endsWith("=true");
        } else {
            return true;
        }
    }
}