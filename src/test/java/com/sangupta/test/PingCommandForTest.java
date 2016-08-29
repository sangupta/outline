package com.sangupta.test;

import javax.inject.Inject;

import com.sangupta.outline.annotations.Command;
import com.sangupta.outline.help.OutlineHelp;

@Command(name = "ping", description = "Ping networks")
class PingCommandForTest {
	
	@Inject
	public OutlineHelp helpCommand;
    
	public void run() {
		System.out.println("This is the ping command");
	}

}
