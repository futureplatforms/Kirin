//
//  NewDatabasesImpl.m
//  KirinKit
//
//  Created by Douglas Hoskins on 04/10/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import "NewDatabasesImpl.h"
#import "fromNative/DatabaseOpenService.h"

@interface NewDatabasesImpl()
@property(strong) id<DatabaseOpenService> kirinModule;
@end

@implementation NewDatabasesImpl

@synthesize kirinModule = kirinModule_;

- (id) init {
    self.serviceName = @"DatabaseOpenService";
    self.kirinModuleProtocol = @protocol(DatabaseOpenService);
    return [super initWithServiceName: self.serviceName];
}

- (void) openOrCreate: (NSString*) filename : (int) version : (NSString*) txId : (NSString*) onOpenedToken : (NSString*) onErrorToken {
    NSLog(@"let's open or create it");
}
@end

