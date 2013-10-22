/**
 * =========================================================================
 * 					Bench4Q version 1.2.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at http://forge.ow2.org/projects/jaspte
 * You can find latest version there.  
 * 
 * Distributed according to the GNU Lesser General Public Licence. 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by   
 * the Free Software Foundation; either version 2.1 of the License, or any
 * later version.
 * 
 * SEE Copyright.txt FOR FULL COPYRIGHT INFORMATION.
 * 
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 *
 * This version is a based on the implementation of TPC-W from University of Wisconsin. 
 * This version used some source code of The Grinder.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 *  * Initial developer(s): Zhiquan Duan.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package org.bench4Q.console.ui;

/**
 * By implementing this interface, a class can easily traverse a HashTree
 * object, and be notified via callbacks of certain events. There are three such
 * events:
 * <ol>
 * <li>When a node is first encountered, the traverser's
 * {@link #addNode(Object,HashTree)} method is called. It is handed the object
 * at that node, and the entire sub-tree of the node.</li>
 * <li>When a leaf node is encountered, the traverser is notified that a full
 * path has been finished via the {@link #processPath()} method. It is the
 * traversing class's responsibility to know the path that has just finished
 * (this can be done by keeping a simple stack of all added nodes).</li>
 * <li>When a node is retraced, the traverser's {@link #subtractNode()} is
 * called. Again, it is the traverser's responsibility to know which node has
 * been retraced.</li>
 * </ol>
 * To summarize, as the traversal goes down a tree path, nodes are added. When
 * the end of the path is reached, the {@link #processPath()} call is sent. As
 * the traversal backs up, nodes are subtracted.
 * <p>
 * The traversal is a depth-first traversal.
 */
public interface HashTreeTraverser {
	/**
	 * The tree traverses itself depth-first, calling addNode for each object it
	 * encounters as it goes. This is a callback method, and should not be
	 * called except by a HashTree during traversal.
	 * 
	 * @param node
	 *            the node currently encountered
	 * @param subTree
	 *            the HashTree under the node encountered
	 */
	public void addNode(Object node, HashTree subTree);

	/**
	 * Indicates traversal has moved up a step, and the visitor should remove
	 * the top node from its stack structure. This is a callback method, and
	 * should not be called except by a HashTree during traversal.
	 */
	public void subtractNode();

	/**
	 * Process path is called when a leaf is reached. If a visitor wishes to
	 * generate Lists of path elements to each leaf, it should keep a Stack data
	 * structure of nodes passed to it with addNode, and removing top items for
	 * every {@link #subtractNode()} call. This is a callback method, and should
	 * not be called except by a HashTree during traversal.
	 */
	public void processPath();
}
