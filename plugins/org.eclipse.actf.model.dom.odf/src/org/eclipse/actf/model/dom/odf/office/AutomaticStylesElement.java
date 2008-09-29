/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tatsuya ISHIHARA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.dom.odf.office;

import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.style.StyleElement;

/**
 * Interface for <office:automatic-styles> element.
 */
public interface AutomaticStylesElement extends ODFElement {
	public StyleElement getStyleElementByName(String name);

	public long getStyleElementSize();

	public StyleElement getStyleElement(long index);

	public void addStyleElement(StyleElement element);
}