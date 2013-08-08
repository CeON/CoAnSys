/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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
