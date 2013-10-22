package org.bench4Q.console.ui.transfer;

import org.bench4Q.console.common.ConsoleException;

/*
 * those classes witch need to know Agent messages implement this interface.
 */
public interface AgentInfoObserver {

	public void addAgent(AgentInfo agentInfo) throws ConsoleException;

	public void removeAgent(AgentInfo agentInfo);

	public void getResult(AgentInfo agentInfo);

	public void restartTest();

}
