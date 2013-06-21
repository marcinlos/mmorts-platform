#ifndef MMORTS_SLICE
#define MMORTS_SLICE

#include <Ice/BuiltinSequences.ice>

// 

module pl {
module edu {
module agh {
module ki {
module mmorts {

    /* Message from the client to the server */
    struct Message {
        int convId;
        Ice::ByteSeq content;
    };
    
    sequence<Message> MessageSeq;
    
    /* Server's response for the client message */
    struct Response {
    
        int version;
    
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
