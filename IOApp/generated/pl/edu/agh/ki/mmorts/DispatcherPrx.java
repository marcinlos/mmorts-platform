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

public interface DispatcherPrx extends Ice.ObjectPrx
{
    public Response deliver(Message msg);

    public Response deliver(Message msg, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_deliver(Message msg);

    public Ice.AsyncResult begin_deliver(Message msg, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_deliver(Message msg, Ice.Callback __cb);

    public Ice.AsyncResult begin_deliver(Message msg, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_deliver(Message msg, Callback_Dispatcher_deliver __cb);

    public Ice.AsyncResult begin_deliver(Message msg, java.util.Map<String, String> __ctx, Callback_Dispatcher_deliver __cb);

    public Response end_deliver(Ice.AsyncResult __result);
}
