#Threading on iOS modules

###UI modules
When creating a Kirin module you should think about which thread you want the callbacks to run on.

Most modules you create for your apps will update your UI directly, and therefore you will want them to run on the main/UI thread.

Your Kirin callbacks will be invoked on the main thread if:

-  Your Kirin module is a `UIViewController`
-  Your Kirin module conforms to the `KirinExtensionOnMainThread` protocol

###Headless modules
If you are creating a module for your app that does not touch the UI, or a new native Kirin module, then you won't want it to run on the UI thread.  Kirin will put your method calls on a dispatch queue, and there are three options available:

####Global concurrent queue
If you simply want your methods executed as quickly as possible, and do not care about the order they call back in (e.g. for a networking module) use the global concurrent queue.  This is the default threading mode for non-UI Kirin modules, and no extra code is needed.

####Serial queue
Kirin can create a serial queue for your module; use this if it is important that your methods are executed in order.  Your module must conform to the `KirinSerialExecute` protocol.

####Custom queue
You can provide your own dispatch queue for your module's method callbacks; perhaps you wish to share your queue with another module (see `NewDatabaseAccessService` and `NewTransactionService` for an example.  Ensure your module implements the `- (dispatch_queue_t) dispatchQueue;` selector.