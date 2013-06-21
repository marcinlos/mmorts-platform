
/**
 * Simple idea: use JGroups to manage a cluster-wide array of servers, redirect
 * messages based on e.g. hash of the client's identifier mod size of the array.
 * This way we achieve:
 * <ul>
 * <li>high scalability - state changes only when the group view changes - not
 * very often, each server can have its' local copy
 * <li>transparency - only the outermost message channel needs to know about
 * this design
 * <li>automatic statistical load-balancing
 * </ul>
 * 
 * Request for comment.
 * 
 * @author los
 */
package pl.edu.agh.ki.mmorts.server.communication.cluster;
