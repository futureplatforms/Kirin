//
//  NewTransactionStatement.h
//  KirinKit
//
//  Created by Douglas Hoskins on 27/11/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import <Foundation/Foundation.h>
typedef enum sqlOperationType {SQL_rowset, SQL_token, SQL_file} SQLOperationType;

@interface NewTransactionStatement : NSObject
@property (nonatomic, assign) SQLOperationType *type;
@property (nonatomic, assign) int *statementId;
@property (nonatomic, retain) NSString *statement;
@property (nonatomic, retain) NSArray *parameters;

-(id) initWithType :(SQLOperationType) type andId:(int)statementId andStatement:(NSString*) statement andParameters:(NSArray*) parameters;

@end