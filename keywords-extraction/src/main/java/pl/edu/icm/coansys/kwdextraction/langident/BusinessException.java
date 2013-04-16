package pl.edu.icm.coansys.kwdextraction.langident;

/*
 * #%L
 * synat-application-commons
 * %%
 * Copyright (C) 2010 - 2013 ICM, Warsaw University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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
