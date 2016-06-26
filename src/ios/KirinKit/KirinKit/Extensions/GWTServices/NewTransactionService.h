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

@interface NewTransactionService : KirinGwtServiceStub<TransactionServiceNative>
- (id) initWithDatabaseAccessService: (id) databaseAccessService;
- (dispatch_queue_t) dispatchQueue;
@end
