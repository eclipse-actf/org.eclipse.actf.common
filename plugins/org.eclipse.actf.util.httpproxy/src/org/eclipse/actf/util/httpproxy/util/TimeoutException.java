/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hideki TAI - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.util.httpproxy.util;

public class TimeoutException extends Exception {
    private static final long serialVersionUID = 2697832049495326392L;

    public TimeoutException() {
        super();
    }
        
    public TimeoutException(String msg) {
        super(msg);
    }
}