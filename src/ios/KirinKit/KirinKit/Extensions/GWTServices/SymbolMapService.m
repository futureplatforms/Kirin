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

- (void) setSymbolMapDetails: (NSString*) moduleName : (NSString*) strongName; {
    DLog(@"SetStrongName %@", strongName);
    
    // We want to load /app/WEB-INF/<app_name>/symbolMaps/<strongName>.symbolMap
    NSString * resourcePath = [[NSBundle mainBundle] resourcePath];
    NSString * pathToWebInf = [NSString stringWithFormat:@"%@/app/WEB-INF", resourcePath];
    
    // WEB-INF contains three folders: classes, lib and the app name.  To save us
    // passing in the app name, look for the entry that is not "classes" or "lib".
    NSFileManager *filemgr =[NSFileManager defaultManager];
    NSArray* filenames = [filemgr contentsOfDirectoryAtPath:pathToWebInf error:nil];
    for (NSString *filename in filenames) {
        if (![filename isEqualToString:@"classes"] && ![filename isEqualToString:@"lib"]) {
            NSString * appName = filename;
            NSString * pathToSymbolMap = [NSString stringWithFormat:@"%@/%@/symbolMaps/%@.symbolMap", pathToWebInf, appName, strongName];
            
            NSMutableData *data = [NSData dataWithContentsOfFile:pathToSymbolMap];
            NSString *symbolMap = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
            [self.kirinModule setSymbolMap:symbolMap];
            break;
        }
    }
}

@end
