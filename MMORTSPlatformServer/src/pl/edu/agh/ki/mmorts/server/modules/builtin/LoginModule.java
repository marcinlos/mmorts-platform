package pl.edu.agh.ki.mmorts.server.modules.builtin;

import static pl.edu.agh.ki.mmorts.server.modules.dsl.DSL._if;
import static pl.edu.agh.ki.mmorts.server.modules.dsl.DSL._while;
import static pl.edu.agh.ki.mmorts.server.modules.dsl.DSL.eq;
import static pl.edu.agh.ki.mmorts.server.modules.dsl.DSL.map;
import static pl.edu.agh.ki.mmorts.server.modules.dsl.DSL.neq;
import static pl.edu.agh.ki.mmorts.server.modules.dsl.DSL.seq;
import static pl.edu.agh.ki.mmorts.server.modules.dsl.DSL.val;
import static pl.edu.agh.ki.mmorts.server.modules.dsl.DSL.with;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.config.Config;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.server.data.PlayersPersistor;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.server.modules.dsl.Cont;
import pl.edu.agh.ki.mmorts.server.modules.dsl.Control;

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
    public void receive(Message message, final Context ctx) {
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
        with(control, _if(val(3).is(eq(3))).then(new Cont() {
            public void execute(Control c) {
                logger.debug("3 == 3");
            }
        })._else(new Cont() {
            public void execute(Control c) {
                logger.debug("3 != 3");
            }
        }));

        ctx.put("n", 1);
        Cont c = _while(map("n", ctx, Integer.class).is(neq(10)))
        ._do(seq(new Cont() {
            public void execute(Control c) {
                int n = ctx.get("n", Integer.class);
                logger.debug("Here goes " + n);
                ctx.put("n", n + 1);
            }
        }));
        with(control, c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        logger.debug("Shutting down");
    }

}
