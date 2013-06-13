#ifndef MMORTS_SLICE
#define MMORTS_SLICE

#include <Ice/BuiltinSequences.ice>

// 

module pl {
module agh {
module edu {
module ki {
module mmorts {

    /* Message from the client to the server */
    struct Message {
        int conversationId;
        Ice::ByteSeq content;
    };
    
    sequence<Message> MessageSeq;
    
    /* Server's response for the client message */
    struct Response {
    
        /* Many modules may respond, the responses are bundled */ 
        MessageSeq messages;
    };

    /* Server dispatcher interface */
    ["amd"]
    interface Dispatcher {
        Response deliver(Message msg);
    };

};
};
};
};
};

#endif /* MMORTS_SLICE */
