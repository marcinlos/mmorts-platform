package pl.edu.agh.ki.mmorts.server.modules.basic.map;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.data.CustomPersistor;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.server.modules.ModuleLogicException;
import pl.edu.agh.ki.mmorts.server.modules.annotations.MessageMapping;
import protocol.mapModule.MapModuleData;
import protocol.mapModule.Requests;
import protocol.mapModule.SimpleMessage;
import protocol.mapModule.helpers.Board;
import protocol.mapModule.helpers.DetailedMessage;
import protocol.mapModule.helpers.FieldContent;

import com.google.inject.Inject;
import com.google.inject.name.Named;

//TODO new interface, no casting;)

public class MapModule extends ModuleBase {

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
    	logger().debug("On init");
    }
	


	@MessageMapping(Requests.CHECK)
	public void checkHandler(Message message, Context ctx) {
		logger().debug(Requests.CHECK + " message got");
		DetailedMessage msg = extractMessage(message, DetailedMessage.class);
		MapModuleData playerData = getFromDatabase(msg.getPlayerName());
		respond(message, Requests.CHECK, isFieldAvailable(msg, playerData));
	}

	@MessageMapping(Requests.FULL_INTERNAL)
	public void fullInternalReceived(Message message, Context ctx) {
		Object returnMapMessage = handleFull(message);
		outputResponse(message,Requests.FULL_INTERNAL,returnMapMessage);
		logger().debug("Responding internal");
		respond(message, Requests.FULL_INTERNAL, returnMapMessage);
	}
	
	@MessageMapping(Requests.FULL_EXTERNAL)
	public void fullExternalReceived(Message message, Context ctx) {
		Object returnMapMessage = handleFull(message);
		logger().debug("Responding external");
		outputResponse(message,Requests.FULL_EXTERNAL,returnMapMessage);
	}

	private MapModuleData handleFull(Message message) {
		logger().debug("full message got");
		MapModuleData returnMapMessage = null;
		SimpleMessage extractedMsg = extractMessage(message,
				SimpleMessage.class);
		try {
			returnMapMessage = getFromDatabase(extractedMsg
					.getPlayerName());
		} catch (IllegalArgumentException e) {
			// TODO think bout it!
			returnMapMessage = new MapModuleData(Board.getFactory().getEasyBoard(defaultRows, defaultColumns));
			persistor.createBinding(name(), extractedMsg.getPlayerName(),
					returnMapMessage);
		}
		return returnMapMessage;
	}
	
	@MessageMapping()
	public void genHandler(Message message, Context ctx) {
		logger().debug("general message got");
	}

	@MessageMapping(Requests.PUT_ON)
	public void putOnHandler(Message message, Context ctx) {
		logger().debug(Requests.PUT_ON + " message got");

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

	@MessageMapping(Requests.REL_AT)
	public void releaseAtHandler(Message message, Context ctx) {
		logger().debug(Requests.REL_AT + " message got");
		final DetailedMessage extractedMsg = extractMessage(message,
				DetailedMessage.class);
		final Board board = (Board) getFromDatabase(
				extractedMsg.getPlayerName()).getBoard();
		board.realeaseAt(extractedMsg.getRow(), extractedMsg.getCol());
		persistor.updateBinding(name(), extractedMsg.getPlayerName(), new MapModuleData(board));
	}

	private boolean isFieldAvailable(DetailedMessage msg,
			MapModuleData playerData) {
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
