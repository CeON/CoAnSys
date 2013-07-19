/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.langident;

import org.slf4j.helpers.MessageFormatter;

public abstract class BusinessException extends RuntimeException {

	public BusinessException() {
		super();
	}

	public BusinessException(Throwable cause, String messagePattern, Object... args) {
		super(MessageFormatter.arrayFormat(messagePattern, args).getMessage(), cause);
	}

	public BusinessException(String messagePattern, Object... args) {
		super(MessageFormatter.arrayFormat(messagePattern, args).getMessage());
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

}
