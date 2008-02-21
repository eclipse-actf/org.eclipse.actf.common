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

package org.eclipse.actf.model.dom.html.impl;

import org.w3c.dom.html.HTMLBaseElement;

public class SHBaseElement extends SHElement implements HTMLBaseElement {
	protected SHBaseElement(String tagName, SHDocument doc) {
		super(tagName, doc);
	}

	public String getHref() {
		return getAttribute("href");
	}

	public void setHref(String href) {
		setAttribute("href", href);
	}

	public String getTarget() {
		return getAttribute("target");
	}

	public void setTarget(String target) {
		setAttribute("target", target);
	}
}
