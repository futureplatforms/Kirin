//
//  NewTransactionStatement.m
//  KirinKit
//
//  Created by Douglas Hoskins on 27/11/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import "NewTransactionStatement.h"

@implementation NewTransactionStatement
-(id) initWithType :(SQLOperationType) type andId:(int) statementId andStatement:(NSString*) statement andParameters:(NSArray*) parameters {
    self = [super init];
    if (self) {
        self.type = type;
        self.statementId = statementId;
        self.statement = statement;
        self.parameters = parameters;
        self.hasId = YES;
    }
    return self;
}
-(id) initWithType :(SQLOperationType) type andStatement:(NSString*) statement andParameters:(NSArray*) parameters {
    self = [super init];
    if (self) {
        self.type = type;
        self.statement = statement;
        self.parameters = parameters;
        self.hasId = NO;
    }
    return self;
}
@end
