/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.ui.util.timer;



public interface WaitExecSyncEventHandler extends Runnable {

    /**
     * Get scheduling interval in sec
     * @return
     */
    public double getInterval();

    /**
     * Check if action can be executed right now 
     * @param elapsed
     * @return
     */
    public boolean canRun(double elapsed);
}