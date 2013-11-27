#Kirin GWT services

It is possible to write Kirin services for GWT platforms in Java.

The pattern for creating these is similar to creating regular Kirin modules, except services should extend `KirinService` and their native counterparts should implement `IKirinNativeService`.

Bindings are generated as for modules, and are output in the `SERVICE-BINDINGS` folder.

## THE BACK DOOR

When GWT platforms instantiate, they create a native class for each service they require, and each native class instantiates its Kirin counterpart object.  On iOS, this happens in the method `[KirinExtensions coreExtensions]`.

These have been Javascript objects until now, and can now also be GWT/Java objects.

When Kirin code needs to use a service, it must communicate with **the same object** that the native class talks to.
    
    +------------------+                   +------------------+
    |                  | B A C K D O O R  \|                  |
    |   Kirin Module   |- - - - - - - - - -|  Kirin Service   |
    |                  |                  /|                  |
    +------------------+                   +------------------+
         /|\      |                             /|\      |   
    ======|=======|==============================|=======|=====
          |      \|/                             |      \|/
                          native code
                          
Usually Kirin modules cannot see instances of other modules.  So we need to create a special portal.  When the service is created, we know it must have been created by the Kirin service, so we hang on to it in the constructor, and make it available statically:

    private static DatabaseAccessService _Instance;
    
    @NoBind
    @NoExport
    public static DatabaseAccessService BACKDOOR() { return _Instance; }
    
    public DatabaseAccessService() {
        super(GWT.<DatabaseAccessServiceNative>create(DatabaseAccessServiceNative.class));
        _Instance = this;
    }
    
We then need to define an interface for this service that a module will use.  This is not exported and so can have any signature you like.  But make sure you add the `@NoBind` and `@NoExport` annotations!

    @NoBind
    @NoExport
    public void _openDatabase(String filename, DatabaseOpenedCallback cb) {
    	getNativeObject().performNativeOperation();
    }

