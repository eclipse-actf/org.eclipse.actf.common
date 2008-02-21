/*******************************************************************************
 * Copyright (c) 1998, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Goh KONDOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.dom.html;

import java.io.InputStreamReader;

import org.xml.sax.SAXException;

class EncodingException extends SAXException {
	private InputStreamReader newReader;

	EncodingException(InputStreamReader reader) {
		super("");
		this.newReader = reader;
	}

	InputStreamReader getNewReader() {
		return this.newReader;
	}
}
