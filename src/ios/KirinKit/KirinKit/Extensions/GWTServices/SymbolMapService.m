//
//  SymbolMapService.m
//  KirinKit
//
//  Created by Douglas Hoskins on 06/03/2014.
//  Copyright (c) 2014 Future Platforms. All rights reserved.
//

#import "SymbolMapService.h"
#import <fromNative/SymbolMapService.h>

@interface SymbolMapService() {
    
}
@property(strong) id<SymbolMapService> kirinModule;
@end

@implementation SymbolMapService

- (id) init {
    self.serviceName = @"SymbolMapService";
    self.kirinModuleProtocol = @protocol(SymbolMapService);
    return [super initWithServiceName: self.serviceName];
}

- (void) setStrongName: (NSString*) strongName {
    NSLog(@"SetStrongName %@", strongName);
   // NSString *filename = [NSString stringWithFormat:@"/app/WEB-INF/deploy/K"]
   // NSMutableData* data = [NSData dataWithContentsOfFile:strongName];
   // NSString * str = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
    NSFileManager *filemgr =[NSFileManager defaultManager];
    NSArray* filenames = [filemgr contentsOfDirectoryAtPath:@"/" error:nil];
    
    for (NSString *filename in filenames) {
        NSLog(@"filename: %@", filename);
    }
}

@end
