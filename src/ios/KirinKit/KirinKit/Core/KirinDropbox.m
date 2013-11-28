//
//  KirinDropbox.m
//  KirinKit
//
//  Created by James Hugman on 23/12/2011.
//  Copyright 2011 Future Platforms. All rights reserved.
//

#import "KirinDropbox.h"
#import <limits.h>

@interface KirinDropbox() {
    NSMutableDictionary * dropbox;
    int key;
}

@end

@implementation KirinDropbox

- (id) init {
    self = [super init];
    
    if (self) {
        dropbox = [[NSMutableDictionary alloc] init];
        key = INT_MIN;
    }
    
    return self;
}

- (NSString *) putObject:(id) object {
    int thisKey = key;
    key++;
    
    NSLog(@"thisKey: %d", thisKey);
    
    NSString *strKey = [NSString stringWithFormat:@"%d", thisKey];
    
    NSLog(@"strKey: %@", strKey);
    
    [dropbox setObject:object forKey:strKey];
    return strKey;
}

- (NSDictionary *) consumeObjectWithToken:(NSString *) token {
    id obj = [dropbox objectForKey:token];
    [dropbox removeObjectForKey:token];
    return obj;
}

@end
