package pl.edu.agh.ki.mmorts.server.modules.builtin;

import org.apache.log4j.Logger;

import static pl.edu.agh.ki.mmorts.server.modules.dsl.DSL.*;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.config.Config;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.server.data.PlayersPersistor;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.server.modules.dsl.Cont;
import pl.edu.agh.ki.mmorts.server.modules.dsl.ContChain;
import pl.edu.agh.ki.mmorts.server.modules.dsl.Control;
import pl.edu.agh.ki.mmorts.server.modules.dsl.IfThenElse;

import com.google.inject.Inject;

/**
 * Module responsible for receiving login messages, authenticating players and
 * sending back player state when necessary.
 */
public class LoginModule extends ModuleBase {

    private static final Logger logger = Logger.getLogger(LoginModule.class);

    @Inject(optional = true)
    private Config config;

    /** Need players manager for data retrieval */
    @Inject(optional = true)
    private PlayersPersistor players;

    @Inject(optional = true)
    private TransactionManager tm;

    public LoginModule(/* PlayersManager players, Gateway gateway */) {
        logger.debug("Initializing");
        // this.players = players;
        // this.gateway = gateway;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void started() {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receive(Message message, Context ctx) {
        logger.debug("Message received");
        logger.debug(message);
        /*
         * gateway.later(new ContChain(gateway, new ContAdapter() {
         * 
         * @Override public void execute(Context context) {
         * logger.debug("First time sth!"); } }, new ContAdapter() {
         * 
         * @Override public void execute(Context context) {
         * logger.debug("Another time!"); } }));
         */
        with(control, 
        _if(val(3).is(eq(3)))
        .then(new Cont() {
            public void execute(Control c) {
                logger.debug("3 == 3");
            }
        })._else(new Cont() {
            public void execute(Control c) {
                logger.debug("3 != 3");
            }
        }));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        logger.debug("Shutting down");
    }

}
