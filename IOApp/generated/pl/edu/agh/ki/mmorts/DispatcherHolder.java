// **********************************************************************
//
// Copyright (c) 2003-2013 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.5.0
//
// <auto-generated>
//
// Generated from file `MMORTS.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package pl.edu.agh.ki.mmorts;

public final class DispatcherHolder extends Ice.ObjectHolderBase<Dispatcher>
{
    public
    DispatcherHolder()
    {
    }

    public
    DispatcherHolder(Dispatcher value)
    {
        this.value = value;
    }

    public void
    patch(Ice.Object v)
    {
        if(v == null || v instanceof Dispatcher)
        {
            value = (Dispatcher)v;
        }
        else
        {
            IceInternal.Ex.throwUOE(type(), v);
        }
    }

    public String
    type()
    {
        return _DispatcherDisp.ice_staticId();
    }
}
