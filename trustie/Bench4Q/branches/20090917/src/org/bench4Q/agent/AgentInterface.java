package org.bench4Q.agent;

import org.bench4Q.common.Bench4QException;


public interface AgentInterface {

	void run() throws Bench4QException;

	void shutdown();
}
