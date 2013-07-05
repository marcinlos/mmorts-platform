package pl.edu.agh.ki.mmorts.client.modules.annotations.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.app.ioapp.modules.Context;

import pl.edu.agh.ki.mmorts.client.modules.annotations.MessageMapping;
import pl.edu.agh.ki.mmorts.client.util.reflection.Methods;
import pl.edu.agh.ki.mmorts.common.message.Message;

/**
 * 
 * @author los
 */
public class CallDispatcher implements Handler {

    private Set<MappingEntry> emptyReq;
    private Map<String, Set<MappingEntry>> byRequest;
    private ArgMapperFactory mapperFactory;
    
    class MatchFinder {
        MappingEntry best = null;
        int bestMatch = -1;
        boolean found = false;
        Message message;
        
        public MatchFinder(Message message) {
            this.message = message;
        }
        
        void process(Iterable<MappingEntry> entries) {
            for (MappingEntry e : entries) {
                int match = e.matches(message);
                if (match >= 0) {
                    if (best == null || bestMatch < match) {
                        best = e;
                        bestMatch = match;
                        found = true;
                    } else if (bestMatch == match) {
                        found = false;
                    }
                }
            }
        }
        
        MappingEntry find(Message msg) {
            process(emptyReq);
            Set<MappingEntry> set = byRequest.get(msg.request);
            if (set != null) {
                process(set);
            }
            if (found) {
                return best;
            } else {
                throw new AmbiguousMappingException("Ambiguous match");
            }
        }
    }

    public CallDispatcher(Class<?> clazz, ArgMapperFactory mapperFactory) {
        this.mapperFactory = mapperFactory;
        emptyReq = new HashSet<MappingEntry>();
        byRequest = new HashMap<String, Set<MappingEntry>>();
        
        processClass(clazz);
    }
    
    void processClass(Class<?> clazz) {
        for (Method m : Methods.annotated(clazz, MessageMapping.class)) {
            processMethod(m);
        }
    }

    void addToMap(String req, MappingEntry e) {
        Set<MappingEntry> set = byRequest.get(req);
        if (set == null) {
            set = new HashSet<MappingEntry>();
            byRequest.put(req, set);
        }
        set.add(e);
    }

    void processMethod(Method m) {
        MessageMapping ann = m.getAnnotation(MessageMapping.class);
        Handler handler = new HandlerImpl(m, mapperFactory.newMapper(m));
        MappingEntry entry = new MappingEntry(ann, handler);
        if (ann.value().length == 0) {
            emptyReq.add(entry);
        } else {
            for (String req : ann.value()) {
                addToMap(req, entry);
            }
        }
    }

    @Override
    public void handle(Object o, Message msg, Context ctx) {
        MappingEntry best = new MatchFinder(msg).find(msg);
        if (best != null) {
            best.handle(o, msg, ctx);
        } else {
            throw new AmbiguousMappingException();
        }
    }

}
