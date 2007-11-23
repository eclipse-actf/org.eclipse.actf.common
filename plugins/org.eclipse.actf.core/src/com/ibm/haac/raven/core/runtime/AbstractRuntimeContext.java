/*******************************************************************************
* Copyright (c) 2004, 2007 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*  <a href="mailto:masquill@us.ibm.com>Mike Squillace</a> - initial API and implementation
*******************************************************************************/ 


package com.ibm.haac.raven.core.runtime;

import com.ibm.haac.raven.core.config.IConfiguration;
import com.ibm.haac.raven.core.logging.IErrorLogger;
import com.ibm.haac.raven.core.resources.IResourceLocator;

/**
 * @author <a href="mailto:masquill@us.ibm.com>Mike Squillace</a>
 *
 */
public abstract class AbstractRuntimeContext implements IRuntimeContext
{

	protected IErrorLogger errorLogger;
	protected IConfiguration configuration;
	protected IResourceLocator resourceLocator;
	
	/** {@inheritDoc} */
	public IResourceLocator getResourceLocator() {
		return resourceLocator;
	}

	/** {@inheritDoc} */
	public IErrorLogger getErrorLogger() {
		return errorLogger;
	}

}
