//
//  NewDatabasesImpl.m
//  KirinKit
//
//  Created by Douglas Hoskins on 04/10/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import "NewDatabasesImpl.h"
#import "fromNative/DatabaseService.h"

@interface NewDatabasesImpl()
@property(strong) id<DatabaseService> kirinModule;
@end

@implementation NewDatabasesImpl

@synthesize kirinModule = kirinModule_;

- (id) init {
    self.serviceName = @"DatabaseService";
    self.kirinModuleProtocol = @protocol(DatabaseService);
    return [super initWithServiceName: self.serviceName];
}

- (void) openOrCreate: (NSString*) filename : (int) version : (NSString*) txId : (NSString*) onOpenedToken : (NSString*) onErrorToken {
    NSLog(@"let's open or create it");
}
@end

