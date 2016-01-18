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

- (void) setSymbolMapDetails: (NSString*) moduleName : (NSString*) strongName {    
    // We want to load /app/symbolMaps/<strongName>.symbolMap
    NSString * resourcePath = [[NSBundle mainBundle] resourcePath];
    NSString * pathToApp = [NSString stringWithFormat:@"%@/app", resourcePath];
    
    // WEB-INF contains three folders: classes, lib and the app name.  To save us
    // passing in the app name, look for the entry that is not "classes" or "lib".
    NSFileManager *filemgr =[NSFileManager defaultManager];
    NSArray* filenames = [filemgr contentsOfDirectoryAtPath:pathToApp error:nil];
    for (NSString *filename in filenames) {
        if (![filename isEqualToString:@"classes"] && ![filename isEqualToString:@"lib"]) {
            NSString * pathToSymbolMap = [NSString stringWithFormat:@"%@/symbolMaps/%@.symbolMap", pathToApp, strongName];
            
            NSData *data = [NSData dataWithContentsOfFile:pathToSymbolMap];
            if (data) {
                NSString *symbolMap = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
                [self.kirinModule setSymbolMap:symbolMap];
            }
            break;
        }
    }
}

@end
