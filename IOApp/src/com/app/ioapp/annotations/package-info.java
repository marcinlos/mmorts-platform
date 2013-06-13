
/**
 * Annotations used for convenience and to avoid cluttering interfaces with
 * unnecessary resource management details. Using a bit of java magic we get
 * clean interfaces with no methods which are not supposed to be called by the
 * clients.
 * 
 * <p>
 * Methods annotated with {@linkplain OnInit} / {@linkplain OnShutdown} will be
 * invoked by the application after creation / before server shutdown,
 * respectively. {@code OnInit} can be used to initialize object using
 * dependencies not injected in the constructor.
 * 
 * <p>
 * <b>Note:</b> Multiple methods may be annotated, but using this feature is
 * discouraged, as there is no way to specify invocation order, and failure in
 * one invocation ends the invocation sequence.
 */
package com.app.ioapp.annotations;

