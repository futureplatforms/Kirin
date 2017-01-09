//
//  KirinConstants.h
//  KirinKit
//
//  Created by Douglas Hoskins on 09/01/2017.
//  Copyright © 2017 Future Platforms. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <KirinKit/SynthesizeSingleton.h>

@interface KirinConstants : NSObject

@property(nonatomic) BOOL loggingEnabled;
@property(nonatomic) BOOL logJS;

SYNTHESIZE_SINGLETON_HEADER_FOR_CLASS(KirinConstants)

#define KIRINCONSTANTS [KirinConstants sharedKirinConstants]

@end