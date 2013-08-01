package pl.edu.agh.ki.mmorts.server.modules.basic.map;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.data.CustomPersistor;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.server.modules.ModuleLogicException;
import pl.edu.agh.ki.mmorts.server.modules.annotations.MessageMapping;
import pl.edu.agh.ki.mmorts.server.modules.basic.map.commons.FieldContent;
import pl.edu.agh.ki.mmorts.server.modules.basic.map.commons.MapModuleData;
import protocol.mapModule.DetailedMessage;
import protocol.mapModule.SimpleMessage;

import com.google.inject.Inject;
import com.google.inject.name.Named;

//TODO new interface, no casting;)

public class MapModule extends ModuleBase {

	private static final Logger logger = Logger.getLogger(MapModule.class);

	@Inject(optional = true)
	@pl.edu.agh.ki.mmorts.server.core.annotations.CustomPersistor
	private CustomPersistor persistor;

	@Inject(optional = true)
	@Named("row.default")
	private int defaultRows;
	
	@Inject(optional = true)
	@Named("column.default")
	private int defaultColumns;
	
	
    @OnInit
    public void init(){
    	System.out.println("=========================================" + persistor);
    }
	

	/* Protocol */
	private static final String CHECK = "check";
	private static final String FULL = "full";
	private static final String PUT_ON = "putOn";
	private static final String REL_AT = "releaseAt";

	@MessageMapping(CHECK)
	public void checkHandler(Message message, Context ctx) {
		logger.debug(CHECK + " message got");
		DetailedMessage msg = extractMessage(message, DetailedMessage.class);
		
		MapModuleData playerData = getFromDatabase(msg.getPlayerName());
		//Message response  = message.response(CHECK, (Object) isFieldAvailable(msg, playerData));
		//output(response);
		respond(message, CHECK, isFieldAvailable(msg, playerData));
	}

	@MessageMapping(FULL)
	public void fullHandler(Message message, Context ctx) {
		logger.debug(FULL + " message got");
		Object returnMapMessage = null;
		SimpleMessage extractedMsg = extractMessage(message,
				SimpleMessage.class);
		try {
			returnMapMessage = (Object) getFromDatabase(extractedMsg
					.getPlayerName());
		} catch (IllegalArgumentException e) {
			// TODO think bout it!
			returnMapMessage = new MapModuleData(Board.getFactory().getEasyBoard(defaultRows, defaultColumns));
			persistor.createBinding(name(), extractedMsg.getPlayerName(),
					returnMapMessage);
		}
		//Message response = message.response(FULL, returnMapMessage);
		//output(response);
		outputResponse(message,FULL,returnMapMessage);
		//respond(message, FULL, returnMapMessage);
	}
	
	@MessageMapping()
	public void genHandler(Message message, Context ctx) {
		logger.debug("general message got");
	}

	@MessageMapping(PUT_ON)
	public void putOnHandler(Message message, Context ctx) {
		logger.debug(PUT_ON + " message got");

		final DetailedMessage extractedMsg = extractMessage(message,
				DetailedMessage.class);
		//Taking from db anytime! Due to this, nothing special must be done on rollback
		//because saving to db(db transaction lvl) is rollbacked automatically from this point of view
		final Board board = (Board) getFromDatabase(
				extractedMsg.getPlayerName()).getBoard();

		if (isFieldAvailable(extractedMsg,
				getFromDatabase(extractedMsg.getPlayerName()))) {
			board.putAt(extractedMsg.getRow(), extractedMsg.getCol());
			
			persistor.updateBinding(name(), extractedMsg.getPlayerName(), new MapModuleData(board));
		} else {
			throw new ModuleLogicException(String.format(
					"Cannot put something at %d, %d", extractedMsg.getRow(),
					extractedMsg.getCol()));
		}

	}

	@MessageMapping(REL_AT)
	public void releaseAtHandler(Message message, Context ctx) {
		logger.debug(REL_AT + " message got");
		final DetailedMessage extractedMsg = extractMessage(message,
				DetailedMessage.class);
		final Board board = (Board) getFromDatabase(
				extractedMsg.getPlayerName()).getBoard();
		board.realeaseAt(extractedMsg.getRow(), extractedMsg.getCol());
		persistor.updateBinding(name(), extractedMsg.getPlayerName(), new MapModuleData(board));

	}

	private boolean isFieldAvailable(DetailedMessage msg,
			MapModuleData playerData) {
		System.out.println("!!!!" + playerData.getBoard());
		return playerData.getBoard().getAt(msg.getRow(), msg.getCol())
				.equals(FieldContent.G);
	}

	private MapModuleData getFromDatabase(String playerName) {
		return persistor
				.receiveBinding(name(), playerName, MapModuleData.class);
	}

	private <T> T extractMessage(Message msg, Class<T> clazz) {
		if(!msg.carries(clazz)){
			throw new IllegalArgumentException("Not valid message got");
		}
		return msg.get(clazz);
	}
}
