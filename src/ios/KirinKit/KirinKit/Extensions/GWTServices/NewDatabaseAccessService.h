//
//  NewDatabasesImpl.h
//  KirinKit
//
//  Created by Douglas Hoskins on 04/10/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NSObject+Kirin.h"
#import "KirinGwtServiceStub.h"
#import "NewTransactionService.h"
#import <toNative/DatabaseAccessServiceNative.h>

@interface NewDatabaseAccessService : KirinGwtServiceStub<DatabaseAccessServiceNative>

@property (strong, nonatomic) NSMutableDictionary * DbForId;
@property (strong, nonatomic) NewTransactionService * NewTransactionService;
@end
