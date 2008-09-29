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
package org.eclipse.actf.model.dom.odf.style;

import org.eclipse.actf.model.dom.odf.base.ODFElement;

/**
 * Interface for <style:map> element.
 */
public interface MapElement extends ODFElement {
	public String getAttrStyleCondition();

	public String getAttrStyleApplyStyleName();

	public String getAttrStyleBaseCellAddress();
}