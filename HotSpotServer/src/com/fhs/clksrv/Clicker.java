package com.fhs.clksrv;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fhs.jlattice.LatticeServer;
import com.fhs.jlattice.rsc.DestructionException;
import com.fhs.jlattice.rsc.InitializationException;
import com.fhs.jlattice.you.impl.Resource;

public class Clicker implements Resource {
	
	public enum Button {
		LEFT(InputEvent.BUTTON1_DOWN_MASK),
		MIDDLE(InputEvent.BUTTON3_DOWN_MASK),
		RIGHT(InputEvent.BUTTON2_DOWN_MASK);
		Button(int mask) {
			this.buttonMask = mask;
		}
		int buttonMask;
	}
	
	@Override
	public String getName() {
		return "clicker";
	}
	
	@Override
	public void init(LatticeServer server) throws InitializationException { /**/ }
	@Override
	public void init(LatticeServer server, String[] args) throws InitializationException {
		try {
			this.spots = HotSpot.loadHotSpots(args[0]);
		} catch (IOException exc) {
			InitializationException excp = new InitializationException();
			excp.initCause(exc);
			throw excp;
		}
	}
	@Override
	public void destroy(LatticeServer server) throws DestructionException { /**/ }
	
	Map<String, HotSpot> spots;
	Robot robot;
	Logger logger = LogManager.getLogger();
	
	public Clicker() throws InitializationException {
		this.spots = new HashMap<>();
		try {
			this.robot = new Robot();
			this.robot.setAutoDelay(0);
			this.robot.setAutoWaitForIdle(true);
		} catch (AWTException exc) {
			InitializationException excp = new InitializationException();
			excp.initCause(exc);
			throw excp;
		}
	}
	
	public Clicker(String hotspotFile) throws InitializationException {
		this();
		try {
			HotSpot.loadHotSpots(hotspotFile);
		} catch (IOException exc) {
			InitializationException excp = new InitializationException();
			excp.initCause(exc);
			throw excp;
		}
	}
	
	public synchronized void doClick(String name) {
		HotSpot hs = this.spots.get(name);
		if(hs == null) {
			this.logger.info("Unknown HotSpot attempt: " + name);
			return;
		}
		this.logger.info("Executing HotSpot " + name);
		Point pt = MouseInfo.getPointerInfo().getLocation();
		int mx = pt.x;
		int my = pt.y;
		this.robot.mouseMove(hs.x, hs.y);
		for (int i = 0; i < hs.clicks; i++) {
			this.robot.mousePress(hs.button.buttonMask);
			this.robot.mouseRelease(hs.button.buttonMask);
		}
		this.robot.mouseMove(mx, my);
	}
	
}

class UndefinedHotSpotException extends RuntimeException {
	public UndefinedHotSpotException(String name) {
		super("Not HotSpot defined with a name of " + name);
	}
}
