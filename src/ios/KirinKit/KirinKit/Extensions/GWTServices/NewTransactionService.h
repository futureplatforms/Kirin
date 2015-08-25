//
//  NewTransactionService.h
//  KirinKit
//
//  Created by Douglas Hoskins on 27/11/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NSObject+Kirin.h"
#import "KirinGwtServiceStub.h"
#import <toNative/TransactionServiceNative.h>
#import "KirinSynchronousExecute.h"

@interface NewTransactionService : KirinGwtServiceStub<TransactionServiceNative, KirinSynchronousExecute>
- (id) initWithDatabaseAccessService: (id) databaseAccessService;
@end
