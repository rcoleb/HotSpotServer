package com.fhs.clksrv;

import java.io.IOException;

import javax.swing.JOptionPane;

import com.fhs.jlattice.LatticeServer;
import com.fhs.jlattice.rsc.InitializationException;
import com.fhs.niosrv.impl.motd.MOTDResource;

public class RunServer {
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, InitializationException {
		// could allow first argument to be file location, thereby allowing console launching
        String fileLoc = JOptionPane.showInputDialog("Please Enter HotSpot File location:");
        
		LatticeServer server = new LatticeServer();
		server.setMessageHandler(new ClickMessageHandler());
        server.getResources().defineResource("clicker", Clicker.class, true, fileLoc);
		server.init(12345);
		server.run();
		server.enableConsole();
	}
}
