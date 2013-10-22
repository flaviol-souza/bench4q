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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * ListedHashTree is a different implementation of the {@link HashTree}
 * collection class. In the ListedHashTree, the order in which values are added
 * is preserved, which sorts the order of the values using the compare()
 * function). Any listing of nodes or iteration through the list of nodes of a
 * ListedHashTree will be given in the order in which the nodes were added to
 * the tree.
 * 
 * @see HashTree
 */
public class ListedHashTree extends HashTree implements Serializable, Cloneable {
	private List order;

	/**
	 * 
	 */
	public ListedHashTree() {
		super();
		order = new LinkedList();
	}

	public Object clone() {
		ListedHashTree newTree = new ListedHashTree();
		cloneTree(newTree);
		return newTree;
	}

	/**
	 * @param key
	 */
	public ListedHashTree(Object key) {
		super();
		order = new LinkedList();
		data.put(key, new ListedHashTree());
		order.add(key);
	}

	/**
	 * @param keys
	 */
	public ListedHashTree(Collection keys) {
		super();
		order = new LinkedList();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			Object temp = it.next();
			data.put(temp, new ListedHashTree());
			order.add(temp);
		}
	}

	/**
	 * @param keys
	 */
	public ListedHashTree(Object[] keys) {
		super();
		order = new LinkedList();
		for (int x = 0; x < keys.length; x++) {
			data.put(keys[x], new ListedHashTree());
			order.add(keys[x]);
		}
	}

	public void set(Object key, Object value) {
		if (!data.containsKey(key)) {
			order.add(key);
		}
		super.set(key, value);
	}

	public void set(Object key, HashTree t) {
		if (!data.containsKey(key)) {
			order.add(key);
		}
		super.set(key, t);
	}

	public void set(Object key, Object[] values) {
		if (!data.containsKey(key)) {
			order.add(key);
		}
		super.set(key, values);
	}

	public void set(Object key, Collection values) {
		if (!data.containsKey(key)) {
			order.add(key);
		}
		super.set(key, values);
	}

	public void replace(Object currentKey, Object newKey) {
		HashTree tree = getTree(currentKey);
		data.remove(currentKey);
		data.put(newKey, tree);
		order.set(order.indexOf(currentKey), newKey);
	}

	public HashTree createNewTree() {
		return new ListedHashTree();
	}

	public HashTree createNewTree(Object key) {
		return new ListedHashTree(key);
	}

	public HashTree createNewTree(Collection values) {
		return new ListedHashTree(values);
	}

	public HashTree add(Object key) {
		if (!data.containsKey(key)) {
			HashTree newTree = createNewTree();
			data.put(key, newTree);
			order.add(key);
			return newTree;
		}
		return getTree(key);
	}

	public Collection list() {
		return order;
	}

	public Object remove(Object key) {
		order.remove(key);
		return data.remove(key);
	}

	public Object[] getArray() {
		return order.toArray();
	}

	// Make sure the hashCode depends on the order as well
	public int hashCode() {
		int hc = 17;
		hc = hc * 37 + (order == null ? 0 : order.hashCode());
		hc = hc * 37 + super.hashCode();
		return hc;
	}

	public boolean equals(Object o) {
		if (!(o instanceof ListedHashTree)) {
			return false;
		}
		ListedHashTree lht = (ListedHashTree) o;
		return (super.equals(lht) && order.equals(lht.order));
	}

	public Set keySet() {
		return data.keySet();
	}

	public int size() {
		return data.size();
	}

	private void readObject(ObjectInputStream ois)
			throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		super.clear();
		order.clear();
	}
}
