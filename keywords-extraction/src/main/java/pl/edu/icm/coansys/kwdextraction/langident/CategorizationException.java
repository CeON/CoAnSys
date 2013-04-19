/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.langident;

/**
*
* @author Gra <Gołębiewski Radosław A.> r.golebiewski@icm.edu.pl
*
*/
public class CategorizationException extends BusinessException {
    private static final long serialVersionUID = 967878452720671176L;

    public CategorizationException() {
        super();
    }

    public CategorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CategorizationException(String message) {
        super(message);
    }

    public CategorizationException(Throwable cause) {
        super(cause);
    }
}
