package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

import java.util.Arrays;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.annotations.MessageMapping;

final class MappingEntry {
    public final MessageMapping desc;
    public final Handler handler;

    MappingEntry(MessageMapping desc, Handler handler) {
        this.desc = desc;
        this.handler = handler;
    }

    void handle(Object target, Message msg, Context ctx) {
        handler.handle(target, msg, ctx);
    }

    int matches(Message message) {
        return new MatchEvaluator(message).run().value();
    }

    static <T> boolean contains(T[] array, T elem) {
        return Arrays.asList(array).contains(elem);
    }

    class MatchEvaluator {

        int match = 0;
        Message message;

        MatchEvaluator(Message message) {
            this.message = message;
        }

        public MatchEvaluator run() {
            if (checkRequest() && checkMode() && checkGroup() && checkSource()
                    && checkType()) {
                // empty
            }
            return this;
        }

        public int value() {
            return match;
        }

        boolean checkType() {
            if (desc.type().length == 0) {
                return true;
            } else {
                Class<?> clazz = message.get(Object.class).getClass();
                for (Class<?> supported : desc.type()) {
                    if (supported.isAssignableFrom(clazz)) {
                        ++match;
                        return true;
                    }
                }
                match = -1;
                return false;
            }
        }

        boolean checkMode() {
            return containsOrEmpty(desc.mode(), message.mode);
        }

        boolean checkRequest() {
            return containsOrEmpty(desc.value(), message.request);
        }

        boolean checkGroup() {
            String addr = message.target;
            String[] array = message.isUnicast() ? desc.address() : desc
                    .group();
            return containsOrEmpty(array, addr);
        }

        boolean checkSource() {
            return containsOrEmpty(desc.source(), message.source);
        }

        <T> boolean containsOrEmpty(T[] array, T elem) {
            if (array.length == 0) {
                return true;
            } else {
                if (contains(array, elem)) {
                    ++match;
                    return true;
                } else {
                    match = -1;
                    return false;
                }
            }
        }

    }

    /**
     * Compares by the annotation only!
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof MappingEntry) {
            MappingEntry e = (MappingEntry) o;
            return e.desc.equals(desc);
        } else {
            return false;
        }
    }

    /**
     * Uses the hash code of an annotation!
     */
    @Override
    public int hashCode() {
        return desc.hashCode();
    }
}