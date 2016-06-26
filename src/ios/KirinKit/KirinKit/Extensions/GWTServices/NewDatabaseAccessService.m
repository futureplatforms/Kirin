//
//  NewDatabasesImpl.m
//  KirinKit
//
//  Created by Douglas Hoskins on 04/10/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import "NewDatabaseAccessService.h"
#import "fromNative/DatabaseAccessService.h"
#import "FMDatabaseQueue.h"
#import "NewTransactionService.h"

@interface NewDatabaseAccessService() {
}
@property(strong) id<DatabaseAccessService> kirinModule;
@end

@implementation NewDatabaseAccessService

static dispatch_queue_t serialDispatchQueue = nil;

@synthesize kirinModule = kirinModule_;

- (id) init {
    self.serviceName = @"DatabaseAccessService";
    self.kirinModuleProtocol = @protocol(DatabaseAccessService);
    self.DbForFilename = [[NSMutableDictionary alloc] init];
    self.NewTransactionService = [[NewTransactionService alloc] initWithDatabaseAccessService:self];
    return [super initWithServiceName: self.serviceName];
}

// gets called by Kirin
- (void) open: (NSString*) filename {
    NSString *docsPath = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES)[0];
    NSString *dbPath   = [docsPath stringByAppendingPathComponent:filename];
    FMDatabaseQueue *queue = [FMDatabaseQueue databaseQueueWithPath:dbPath];
    if (queue != nil) {
        [self.DbForFilename setObject:queue forKey:filename];
        [self.kirinModule databaseOpenedSuccess:filename];
    } else {
        [self.kirinModule databaseOpenedFailure:filename];
    }
}

+ (dispatch_queue_t) getDatabaseDispatchQueue {
    if (!serialDispatchQueue) {
        serialDispatchQueue = dispatch_queue_create("com.futureplatforms.kirin.databasesqueue", DISPATCH_QUEUE_SERIAL);
    }
    return serialDispatchQueue;
}

- (dispatch_queue_t) dispatchQueue {
    return [NewDatabaseAccessService getDatabaseDispatchQueue];
}

@end

