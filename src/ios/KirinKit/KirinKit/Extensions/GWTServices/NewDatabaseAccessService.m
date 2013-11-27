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

@synthesize kirinModule = kirinModule_;

- (id) init {
    self.serviceName = @"DatabaseAccessService";
    self.kirinModuleProtocol = @protocol(DatabaseAccessService);
    self.DbForId = [[NSMutableDictionary alloc] init];
    self.NewTransactionService = [[NewTransactionService alloc] initWithDatabaseAccessService:self];
    return [super initWithServiceName: self.serviceName];
}

// gets called by Kirin
- (void) open: (NSString*) filename : (int) dbId {
    NSString *docsPath = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES)[0];
    NSString *dbPath   = [docsPath stringByAppendingPathComponent:filename];
    FMDatabaseQueue *queue = [FMDatabaseQueue databaseQueueWithPath:dbPath];
    if (queue != nil) {
        [self.DbForId setObject:queue forKey:@(dbId)];
        [self.kirinModule databaseOpenedSuccess:dbId];
    } else {
        [self.kirinModule databaseOpenedFailure:dbId];
    }
}

// gets called by Kirin
- (void) close: (int) dbId {
    [[self.DbForId objectForKey:@(dbId)] close];
    [self.DbForId removeObjectForKey:@(dbId)];
}

@end

