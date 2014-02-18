package com.fhs.clksrv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.fhs.jlattice.Message;
import com.fhs.jlattice.rsc.DestructionException;
import com.fhs.jlattice.rsc.InitializationException;
import com.fhs.jlattice.rsc.UndefinedResourceException;
import com.fhs.jlattice.run.KeyAttachment;
import com.fhs.jlattice.you.impl.MessageHandler;
import com.fhs.jlattice.you.impl.Response;
import com.fhs.niosrv.impl.HandlerTools;
import com.fhs.niosrv.impl.resp.StringResponse;

public class ClickMessageHandler implements MessageHandler {
	Logger logger = LogManager.getLogger();
	public Response<?> messageRecieved(Message msg) {
		// parse message bytes to String
		// CAUTION: will parse whole message - could fail if message contains non-String bytes
        String v = HandlerTools.parseMessage(msg);
        // Parse String to JSON
        // CAUTION: assumes a lack of http headers/etc
        JSONObject jsonMsg = new JSONObject(v);
        String macroName = jsonMsg.getString("call");
        try {
			Clicker rsc = (Clicker) msg.server.getResources().requestResource("clicker", false);
			rsc.doClick(macroName);
			msg.server.getResources().returnResource(rsc, false, false);
			
			return new StringResponse("{ \"code\": \"success\" }"); // success! - rather obviously
			// relatively proper error handling (still prints to console though, since we're using the default ExceptionHandler):
		} catch (InstantiationException exc) {
			msg.server.getExceptionHandler().handleResourceException(msg.server, exc);
		} catch (DestructionException exc) {
			msg.server.getExceptionHandler().handleResourceException(msg.server, exc);
		} catch (IllegalAccessException exc) {
			msg.server.getExceptionHandler().handleResourceException(msg.server, exc);
		} catch (InitializationException exc) {
			msg.server.getExceptionHandler().handleResourceException(msg.server, exc);
		} catch (UndefinedResourceException exc) {
			msg.server.getExceptionHandler().handleResourceException(msg.server, exc);
		}
        
		return new StringResponse("{ \"code\": \"errored out\" }"); // generic error
	}
}
