package pl.edu.agh.ki.mmorts.server.modules.builtin;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.data.PlayerData;
import pl.edu.agh.ki.mmorts.server.data.PlayerDataImpl;
import pl.edu.agh.ki.mmorts.server.data.PlayersPersistor;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.server.modules.annotations.MessageMapping;
import protocol.loginModule.LoginMessage;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Module responsible for receiving login messages, authenticating players and
 * sending back player state when necessary.
 * 
 * @author los
 */
public class LoginModule extends ModuleBase {

    /** Need players manager for data retrieval */
    @Inject(optional = true)
    private PlayersPersistor players;

    @Inject(optional = true)
    @Named("login.number")
    private int number;
    

    
    @MessageMapping("auth")
    public void handleAuth(Message message, Context ctx) {
        logger().debug("Got auth message");
        if(!message.carries(LoginMessage.class)){
        	throw new IllegalArgumentException("Bad message received!");
        }
        LoginMessage loginMessage = message.get(LoginMessage.class);
		String playerRequest = loginMessage.getLogin();
		String passwordRequest = loginMessage.getPassword();
		
		PlayerData databasePlayerData = null;
		databasePlayerData = players.receivePlayer(playerRequest);
		if(databasePlayerData == null){
			//new player created
			players.createPlayer(new PlayerDataImpl(playerRequest, passwordRequest, playerRequest));
	        outputResponse(message, "auth-success");
	        return;
		}
		
		if(databasePlayerData.getPasswordHash().equals(passwordRequest)){
	        outputResponse(message, "auth-success");
		}else{
	        outputResponse(message, "auth-failed");
		}
    }
    
    @MessageMapping
    public void general(Message message, Context ctx) {
        logger().debug("=================general()====================");
    }
    
    
    
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void receive(final Message message, final Context ctx) {
//        logger().debug("Message received");
//        logger().debug(message);
//        super.receive(message, ctx);
//
//        if (message.request.equals("auth")) {
//            call(_if(val(3).is(eq(3))).then(new Cont() {
//                public void execute(Control c) {
//                    logger().debug("3 == 3");
//                }
//            })._else(new Cont() {
//                public void execute(Control c) {
//                    logger().debug("3 != 3");
//                }
//            }));
//
//            transaction().addListener(new TransactionListener() {
//                @Override
//                public void rollback() {
//                    logger().debug("Rolled back :(");
//                    outputResponse(message, "info-fail", (Object) ":(");
//                }
//
//                @Override
//                public void commit() {
//                    logger().debug("Commited \\o/");
//                    outputResponse(message, "info-success",
//                            (Object) "Weeeeee :D");
//                }
//            });
//
//            ctx.put("n", 1);
//            Cont c = _while(map("n", ctx, Integer.class).is(neq(10)))._do(
//                    seq(new Cont() {
//                        public void execute(Control c) {
//                            int n = ctx.get("n", Integer.class);
//                            logger().debug("Here goes " + n);
//                            Random rand = new Random();
//                            outputResponse(message, ":|",
//                                    (Object) ("So far so good, " + n));
//                            if (rand.nextInt(10) == 7) {
//                                //throw new RuntimeException("Evul exception!");
//                            }
//                            send("inc_mod", "increment", (Object) "n");
//    

}
