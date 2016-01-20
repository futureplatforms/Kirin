//
//  KirinDropbox.m
//  KirinKit
//
//  Created by James Hugman on 23/12/2011.
//  Copyright 2011 Future Platforms. All rights reserved.
//

#import "KirinDropbox.h"
#import <limits.h>

@interface KirinDropbox()
@property (nonatomic, strong)    NSMutableDictionary * dropbox;
@property (nonatomic, assign)    int key;

@end

@implementation KirinDropbox

- (id) init {
    self = [super init];
    
    if (self) {
        self.dropbox = [[NSMutableDictionary alloc] init];
        _key = INT_MIN;
    }
    
    return self;
}

- (NSString *) putObject:(id) object {
    int thisKey = _key;
    _key++;
    
    NSString *strKey = [NSString stringWithFormat:@"%d", thisKey];
    
    [self.dropbox setObject:object forKey:strKey];
    return strKey;
}

- (id) consumeObjectWithToken:(NSString *) token {
    id obj = [self.dropbox objectForKey:token];
    [self.dropbox removeObjectForKey:token];
    return obj;
}

@end
