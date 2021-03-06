#Databases technical overview

Kirin has a transactional database API, here is a guide on how to use it.

## Code sample:
    DatabaseDelegate dbDel = StaticDependencies.getInstance().getDatabasesDelegate();
    dbDel.open("myDatabase", new DatabaseOpenedCallback() {
    	@Override
		public void onOpened(Database db) {
			// Database opened!  Perform transaction
			db.transaction(new TxRunner() {
				@Override
				public void run(Transaction tx) {
					// Perform the transaction now!
					tx.execUpdate("CREATE TABLE hello (theKey INTEGER PRIMARY KEY AUTOINCREMENT, val STRING)");
					tx.execUpdate("INSERT INTO hello (val) VALUES (?)", new String[] { "Wicked" });
					tx.execUpdate("INSERT INTO hello (val) VALUES (?)", new String[] { "Gaaaah" });
					tx.execQueryWithRowsReturn("SELECT COUNT(val) AS NUM FROM hello", new TxRowsCB() {
					@Override
					public void onError() {
						// This query failed
					}

					@Override
					public void onSuccess(RowSet rowset) {
						// Query success!
					}
				}
				
				@Override
				public void onComplete() {
					// Transaction complete
				}

				@Override
				public void onError() {
					// Transaction error
				}
			}
		}
		@Override
		public void onError() {
			// Database didn't open correctly
		}
    });

##Queries
You should use `Transaction.execUpdate` for statements that have no return value, and `Transaction.execQuery...` for `SELECT`s.  

### Parameters
In your SQL, parameters should be represented as `?` and passed in as a `String[]`.  **All  values are currently presented as strings**.   As SQLite is dynamic, it's completely fine with this.  

If anybody fancies writing a bunch of type detecting and casting code then be my guest.

##Types of query
There are three types of query you can run.
###Batch query
This executes a batch of SQL statements.  This can be loaded from a text file.  Queries must be separated by a semicolon and a newline.

    DROP TABLE IF EXISTS mytable;

    CREATE TABLE mytable (
        article_id       INTEGER PRIMARY KEY AUTOINCREMENT,
        google_id        TEXT NOT NULL UNIQUE,
    
        -- Comments look like this
        source           TEXT,
        image_url        TEXT
    );
    
Files like this should be loaded into a string and executed using `Transaction.execBatchUpdate()`.

For details on loading text files within Kirin, see [RESOURCE-ACCESS](08-RESOURCE-ACCESS.md). 

###Rowset query
This executes a `SELECT` and returns the results to Kirin code as a `RowSet` object.  There is currently ONE native to Kirin method call per row in the set.  

**THIS WILL TAKE A LONG TIME ON GWT PLATFORMS IF YOU HAVE A QUERY RETURNING LOTS OF ROWS.**

But fear not, because you can always use:
###Token query

You don't always need to manipulate your query results in Kirin code, sometimes you just want to display them on the UI.

And pushing a large results set through the Kirin keyhole is impractical.

A token query returns a string token, that you can pass back to native at the appropriate time.  Native code can use this token to retrieve the results set from the "Kirin Dropbox" and do what it likes.

####Using DB tokens
###### iOS
The Kirin dropbox is accessed using `KirinDropbox* dropbox = [KIRIN dropbox]`.  Then, retrieve your database results as an `NSArray` of `NSDictionary` objects (one for each row of your results set) using `[dropbox consumeObjectWithToken: yourToken]`.

##### Android
Retrieve your database results as a `Cursor` using `AndroidDbDropbox.getInstance().getCursor(token)`.

##Native implementation
The iOS version is backed by FMDB.  
On GWT it's separated in to two services, `DatabaseAccessService` and `TransactionService`.