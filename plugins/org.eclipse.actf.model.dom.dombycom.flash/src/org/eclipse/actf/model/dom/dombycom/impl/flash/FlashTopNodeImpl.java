/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hisashi MIYASHITA - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.dom.dombycom.impl.flash;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.actf.model.dom.dombycom.AnalyzedResult;
import org.eclipse.actf.model.dom.dombycom.IFlashNode;
import org.eclipse.actf.model.dom.dombycom.IFlashMSAANode;
import org.eclipse.actf.model.dom.dombycom.INodeEx;
import org.eclipse.actf.model.dom.dombycom.INodeExSound;
import org.eclipse.actf.model.dom.dombycom.INodeExVideo;
import org.eclipse.actf.model.dom.dombycom.impl.Helper;
import org.eclipse.actf.model.dom.dombycom.impl.ListNodeListImpl;
import org.eclipse.actf.model.dom.dombycom.impl.NodeImpl;
import org.eclipse.actf.model.dom.dombycom.impl.html.ElementImpl;
import org.eclipse.actf.model.flash.FlashModelPlugin;
import org.eclipse.actf.model.flash.IFlashConst;
import org.eclipse.actf.model.flash.as.ASDeserializer;
import org.eclipse.actf.model.flash.as.ASObject;
import org.eclipse.actf.model.flash.as.ASSerializer;
import org.eclipse.actf.model.flash.bridge.IWaXcoding;
import org.eclipse.actf.util.timer.Yield;
import org.eclipse.actf.util.vocab.AbstractTerms;
import org.eclipse.actf.util.win32.comclutch.DispatchException;
import org.eclipse.actf.util.win32.comclutch.IDispatch;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;




class FlashTopNodeImpl extends ElementImpl implements IFlashNode, IFlashConst {
    private static final String CLSID = "CLSID:D27CDB6E-AE6D-11CF-96B8-444553540000";
    private static final String APP_TYPE = "application/x-shockwave-flash";

    private static final IFlashNode[] emptyResult = new IFlashNode[0];

    private final String rootPath;
    private final String requestArgsPath;
    private final String responseValuePath;
    private FlashMSAANodeImpl cachedMSAA;

    private String secret;

    static FlashTopNodeImpl newFlashNode(NodeImpl baseNode, IDispatch inode) {
        String clsid = (String) Helper.get(inode, "classid");
        if (CLSID.equalsIgnoreCase(clsid)) {
            return new FlashTopNodeImpl(PATH_BRIDGELEVEL, baseNode, inode);
        }
        String type = (String) Helper.get((IDispatch) inode.invoke1("getAttributeNode", "type"), "value");
        if (APP_TYPE.equalsIgnoreCase(type)) {
            return new FlashTopNodeImpl(PATH_BRIDGELEVEL, baseNode, inode);
        }
        return null;
    }

    private boolean isReady = false;
    private static final Integer completedReadyState = Integer.valueOf(4);
    private boolean isReady() {
        if (isReady) return true;
        Object r = Helper.get(inode, "readyState");
        if (completedReadyState.equals(r)) {
            isReady = true;
            return true;
        }
        return false;
    }

    private void setVariable(String name, String value) {
        if (!isReady()) return;
        try {
            inode.invoke(PLAYER_SET_VARIABLE, new Object[] { name, value });
        } catch (DispatchException e) {
        }
    }

    private String getVariable(String name) {
        if (!isReady()) return "";
        try {
            return (String) inode.invoke1(PLAYER_GET_VARIABLE, name);
        } catch (DispatchException e) {
            return "";
        }
    }

    private static final long TIMEOUT = 100;
    
    private Object invokeAS(Object[] args) {
        if (secret == null) {
            initSecret();
            if (secret == null)
                return null;
        }
        setVariable(responseValuePath, "");
        String argsStr = ASSerializer.serialize(secret, args);
        setVariable(requestArgsPath, argsStr);
        long endTime = System.currentTimeMillis() + TIMEOUT; 
        while (endTime > System.currentTimeMillis()) {
            String value = getVariable(responseValuePath);
            if (value.length() > 0) {
                ASDeserializer asd = new ASDeserializer(value);
                return asd.deserialize();
            }
            Yield.once();
        }
        return null;
    }

    private Integer marker;

    public Integer getMarker() {
        return marker;
    }

    public Object callMethod(String target, String method) {
        return invokeAS(new Object[] { M_CALL_METHOD, target, method });
    }

    public Object callMethod(Object[] args) {
        Object[] a = new Object[args.length + 1];
        a[0] = M_CALL_METHOD;
        System.arraycopy(args, 0, a, 1, args.length);
        return invokeAS(a);
    }

    public boolean setFocus(String target) {
        Boolean b = (Boolean) invokeAS(new Object[] { "setFocus", target });
        if (b == null)
            return false;
        return b.booleanValue();
    }

    public boolean setMarker(Object x, Object y, Object w, Object h) {
        initMarker();
        if (marker == null)
            return false;
        if ((x != null) && (y != null) && (w != null) && (h != null)) {
            invokeAS(new Object[] { M_SET_MARKER, marker, x, y, w, h });
            return true;
        } else {
            return false;
        }
    }

    public boolean unsetMarker() {
        if (marker == null)
            return false;
        invokeAS(new Object[] { "unsetMarker", marker });
        return true;
    }

    private INodeExVideo[] searchVideo(String path) {
        // IDispatch idisp = (IDispatch) inode.method("searchVideo", new Object[] {path});
        Object[] videos = (Object[]) invokeAS(new Object[] { "searchVideo", "_level0", "_global" });
        if (videos == null)
            return null;
        int len = videos.length;
        if (len == 0)
            return null;
        INodeExVideo[] result = new INodeExVideo[len];
        for (int i = 0; i < len; i++) {
            //System.err.println("SVO:" + videos[i]);
            if (videos[i] instanceof ASObject) {
                result[i] = new FlashVideoImpl(this, (ASObject) videos[i]);
            } else {
                result[i] = null;
            }
        }
        return result;
    }

    private INodeExSound[] searchSound(String path) {
        Object[] sounds = (Object[]) invokeAS(new Object[] { "searchSound", PATH_ROOTLEVEL, "_global" });
        if (sounds == null)
            return null;
        int len = sounds.length;
        if (len == 0)
            return null;
        INodeExSound[] result = new INodeExSound[len];
        for (int i = 0; i < len; i++) {
            //System.err.println("SSO:" + sounds[i]);
            if (sounds[i] instanceof ASObject) {
                result[i] = new FlashSoundImpl(this, (ASObject) sounds[i]);
            } else {
                result[i] = null;
            }
        }
        return result;
    }

    private void initSecret() {
        try {
            String id = getVariable(rootPath + PATH_CONTENT_ID);
            if ((id == null) || (id.length() == 0)) return;
            IWaXcoding waxcoding = FlashModelPlugin.getDefault().getIWaXcoding();
            this.secret = waxcoding.getSecret(id, false);
        } catch (DispatchException e) {
        }
    }

    private FlashTopNodeImpl(String rootPath, NodeImpl baseNode, IDispatch idisp) {
        super(baseNode, idisp);
        this.rootPath = rootPath;
        this.requestArgsPath = rootPath + PROP_REQUEST_ARGS;
        this.responseValuePath = rootPath + PROP_RESPONSE_VALUE;
        initMarker();
    }

    private void initMarker() {
        if (this.marker != null)
            return;
        try {
            Integer i = (Integer) invokeAS(new Object[] { M_NEW_MARKER });
            this.marker = i;
        } catch (DispatchException e) {
            this.marker = null;
        }
    }

    public String getTarget() {
        return "";
    }

    public IFlashNode getNodeFromPath(String path) {
        ASObject asObj = (ASObject) invokeAS(new Object[] { M_GET_NODE_FROM_PATH, path });
        if (asObj == null)
            return null;
        return new FlashNodeImpl(this, asObj);
    }

    IFlashNode getNodeAtDepthWithPath(String path, int depth) {
        ASObject asObj = (ASObject) invokeAS(new Object[] { "getNodeAtDepth", path, Integer.valueOf(depth) });
        if (asObj == null)
            return null;
        return new FlashNodeImpl(this, asObj);
    }

    public IFlashNode getNodeAtDepth(int depth) {
        return null;
    }

    public IFlashNode[] getInnerNodes() {
        return emptyResult;
    }

    private IFlashNode[] createFlashNodeArray(Object[] nodeObjs) {
        if (nodeObjs == null) return emptyResult;
        int len = nodeObjs.length;
        if (len == 0) return emptyResult;
        IFlashNode[] result = new IFlashNode[len];
        for (int i = 0; i < len; i++) {
            if (nodeObjs[i] instanceof ASObject) {
                result[i] = new FlashNodeImpl(this, (ASObject) nodeObjs[i]);
            } else {
                result[i] = null;
            }
        }
        return result;
    }

    public IFlashNode[] getInnerNodesWithPath(String path) {
        /*
          try {
            IDispatch idisp = (IDispatch) inode.invoke1("getInnerNodes", path);
            Integer lenInt = (Integer) Helper.get(idisp, "length");
            if (lenInt == null) return emptyResult;
            int len = lenInt.intValue();
            IFlashNode[] result = new IFlashNode[len];
            for (int i = 0; i < len; i++) {
                IDispatch cidisp = (IDispatch) Helper.get(idisp, Integer.toString(i));
                if (cidisp == null) {
                    result[i] = null;
                } else {
                    result[i] = new FlashNodeImpl(this, cidisp);
                }
            }
            return result;
        } catch (DispatchException e) {
        }
        */
        Object[] nodeObjs = (Object[]) invokeAS(new Object[] { "getInnerNodes", path });
        if (nodeObjs == null)
            return emptyResult;
        int len = nodeObjs.length;
        if (len == 0)
            return emptyResult;
        IFlashNode[] result = new IFlashNode[len];
        for (int i = 0; i < len; i++) {
            if (nodeObjs[i] instanceof ASObject) {
                result[i] = new FlashNodeImpl(this, (ASObject) nodeObjs[i]);
            } else {
                result[i] = null;
            }
        }
        return result;
    }

    public IFlashNode[] getSWFChildNodesWithPath(String path) {
        Object[] nodeObjs = (Object[]) invokeAS(new Object[] { M_GET_CHILD_NODES, path });
        if (nodeObjs == null)
            return emptyResult;
        int len = nodeObjs.length;
        if (len == 0)
            return emptyResult;
        IFlashNode[] result = new IFlashNode[len];
        for (int i = 0; i < len; i++) {
            if (nodeObjs[i] instanceof ASObject) {
                result[i] = new FlashNodeImpl(this, (ASObject) nodeObjs[i]);
            } else {
                result[i] = null;
            }
        }
        return result;
    }

    public IFlashNode[] translate() {
        try {
            return translateWithPath(PATH_ROOTLEVEL);
        } catch (DispatchException e) {
            return emptyResult;
        }
    }

    public IFlashNode[] translateWithPath(String path) {
        Object[] nodeObjs = (Object[]) invokeAS(new Object[] { "translate", path });
        return createFlashNodeArray(nodeObjs);
    }

    Object getProperty(String path, String prop) {
        return invokeAS(new Object[] { "getProperty", path, prop });
    }

    void setProperty(String path, String prop, Object value) {
        invokeAS(new Object[] { "setProperty", path, prop, value });
    }

    public int getDepth() {
        return INVALID_DEPTH;
    }

    public int getCurrentFrame() {
        return -1;
    }

    public INodeEx getBaseNode() {
        return this;
    }

    // --------------------------------------------------------------------------------
    // Node Overriding Impl.
    // --------------------------------------------------------------------------------

    @Override
    public Node getFirstChild() {
        System.err.println("invalid(getFirstChild).");
        return null;
    }

    @Override
    public Node getLastChild() {
        System.err.println("invalid(getLastChild).");
        return null;
    }

    @Override
    public NodeList getChildNodes() {
        List<Node> l = new ArrayList<Node>(3);
        for (int i = 0; i < 3; i++) {
            String levelxName = "_level" + i;
            IFlashNode levelx = getNodeFromPath(levelxName);
            if (levelx != null) {
                // System.err.println(levelxName + " is found.");
                l.add(levelx);
            }
        }
        
        return new ListNodeListImpl(l);
    }

    private boolean hasMedia = false;
    boolean hasMedia() {
        return hasMedia;
    }
    @Override
    public AnalyzedResult analyze(AnalyzedResult ar) {
        INodeExVideo[] videos = searchVideo(PATH_ROOTLEVEL);
        if ((videos != null) && videos.length > 0) {
            hasMedia = true;
            for (int i = 0; i < videos.length; i++) {
                ar.addVideo(videos[i]);
            }
        }
        INodeExSound[] sounds = searchSound(PATH_ROOTLEVEL);
        if (sounds != null) {
            for (int i = 0; i < sounds.length; i++) {
                ar.addSound(sounds[i]);
            }
        }
        ar.addFlashTopNode(this);
        return ar;
    }

    private boolean REPAIR = false;

    public void repairFlash() {
        if (!REPAIR) {
            REPAIR = true;
            //Object o = 
            invokeAS(new Object[] { "repairFlash", PATH_ROOTLEVEL });
            //System.out.println("" + o);
        }
    }
    
    public IFlashMSAANode getMSAA() {
        if (cachedMSAA == null)
            cachedMSAA = FlashMSAANodeImpl.newMSAANode(this, inode);
        if ((cachedMSAA == null) || (cachedMSAA.getWindow() == 0)) return null;
        // repairFlash();
        updateTarget();
        return cachedMSAA;
    }

    public long getHWND() {
        return FlashMSAANodeImpl.getHWNDFromObject(super.getINode());
    }

    @Override
    public AbstractTerms getTerms() {
        return FlashTerms.getInstance();
    }

    // TODO...
    private boolean updatedTarget = false;
    public void updateTarget() {
        if (updatedTarget) return;
        Object result = invokeAS(new Object[] { "updateTarget", PATH_ROOTLEVEL, 10 });
        if ((result instanceof Boolean) && (((Boolean) result).booleanValue())) {
            updatedTarget = true;
        }
    }
}
