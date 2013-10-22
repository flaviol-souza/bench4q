package org.bench4Q.console.ui.transfer;

import org.bench4Q.console.common.ConsoleException;

public interface AgentsCollectionSubject {

	public void registerObserver(AgentInfoObserver o);

	public void removeObserver(AgentInfoObserver o);

	public void notifyObserverAdd(AgentInfo agentInfo) throws ConsoleException;

	public void notifyObserverDel(AgentInfo agentInfo);

}
