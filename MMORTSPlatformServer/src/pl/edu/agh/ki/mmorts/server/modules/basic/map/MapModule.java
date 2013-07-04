package pl.edu.agh.ki.mmorts.server.modules.basic.map;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionListener;
import pl.edu.agh.ki.mmorts.server.data.CustomPersistor;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.server.modules.ModuleLogicException;
import pl.edu.agh.ki.mmorts.server.modules.annotations.MessageMapping;
import pl.edu.agh.ki.mmorts.server.modules.basic.map.commons.FieldContent;
import pl.edu.agh.ki.mmorts.server.modules.basic.map.commons.MapModuleData;
import pl.edu.agh.ki.mmorts.server.modules.basic.map.protocol.DetailedMessage;
import pl.edu.agh.ki.mmorts.server.modules.basic.map.protocol.SimpleMessage;

import com.google.inject.Inject;
import com.google.inject.name.Named;

//TODO new interface, no casting;)

public class MapModule extends ModuleBase {

	private static final Logger logger = Logger.getLogger(MapModule.class);

	@Inject(optional = true)
	private CustomPersistor persistor;

	@Inject(optional = true)
	@Named("row.default")
	private int defaultRows;
	
	@Inject(optional = true)
	@Named("column.default")
	private int defaultColumns;
	
	

	/* Protocol */
	private static final String CHECK = "check";
	private static final String FULL = "full";
	private static final String PUT_ON = "putOn";
	private static final String REL_AT = "realeaseAt";

	@MessageMapping(CHECK)
	void checkHandler(Message message, Context ctx) {
		logger.debug(CHECK + " message got");
		DetailedMessage msg = extractMessage(message, DetailedMessage.class);
		MapModuleData playerData = getFromDatabase(msg.getPlayerName());
		message.response(CHECK, (Object) isFieldAvailable(msg, playerData));
	}

	@MessageMapping(FULL)
	void fullHandler(Message message, Context ctx) {
		logger.debug(FULL + " message got");
		Object returnMapMessage = null;
		SimpleMessage extractedMsg = extractMessage(message,
				SimpleMessage.class);
		try {
			returnMapMessage = (Object) getFromDatabase(extractedMsg
					.getPlayerName());
		} catch (IllegalArgumentException e) {
			// TODO think bout it!
			returnMapMessage = Board.getFactory().getTypicalBoard(defaultRows, defaultColumns);
			persistor.createBinding(name(), extractedMsg.getPlayerName(),
					returnMapMessage);
		}
		message.response(FULL, returnMapMessage);
	}

	@MessageMapping(PUT_ON)
	void putOnHandler(Message message, Context ctx) {
		logger.debug(PUT_ON + " message got");

		final DetailedMessage extractedMsg = extractMessage(message,
				DetailedMessage.class);
		final Board board = (Board) getFromDatabase(
				extractedMsg.getPlayerName()).getBoard();
		transaction().addListener(new TransactionListener() {

			@Override
			public void rollback() {
				logger.debug("Putting on rolled back");
				board.realeaseAt(extractedMsg.getRow(), extractedMsg.getCol());
			}

			@Override
			public void commit() {
				logger.debug("Putting on commited");
			}
		});

		if (isFieldAvailable(extractedMsg,
				getFromDatabase(extractedMsg.getPlayerName()))) {
			board.putAt(extractedMsg.getRow(), extractedMsg.getCol());
		} else {
			throw new ModuleLogicException(String.format(
					"Cannot put something at %d, %d", extractedMsg.getRow(),
					extractedMsg.getCol()));
		}

	}

	@MessageMapping(REL_AT)
	void releaseAtHandler(Message message, Context ctx) {
		logger.debug(REL_AT + " message got");
		final DetailedMessage extractedMsg = extractMessage(message,
				DetailedMessage.class);
		final Board board = (Board) getFromDatabase(
				extractedMsg.getPlayerName()).getBoard();

		transaction().addListener(new TransactionListener() {
			@Override
			public void rollback() {
				logger.debug("Releasing rolled back");
				board.putAt(extractedMsg.getRow(), extractedMsg.getCol());
			}

			@Override
			public void commit() {
				logger.debug("Releasing commited");
			}
		});

		board.realeaseAt(extractedMsg.getRow(), extractedMsg.getCol());

	}

	private boolean isFieldAvailable(DetailedMessage msg,
			MapModuleData playerData) {
		return playerData.getBoard().getAt(msg.getRow(), msg.getCol())
				.equals(FieldContent.GRASS);
	}

	private MapModuleData getFromDatabase(String playerName) {
		return persistor
				.receiveBinding(name(), playerName, MapModuleData.class);
	}

	private <T> T extractMessage(Message msg, Class<T> clazz) {
		return msg.get(clazz);
	}
}
