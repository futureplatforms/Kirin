//
//  KirinSuperDevMode.h
//  KirinKit
//
//  Created by Douglas Hoskins on 19/08/2015.
//  Copyright (c) 2015 Future Platforms. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <KirinKit/SynthesizeSingleton.h>

@interface KirinSuperDevMode : NSObject

@property(nonatomic) BOOL superDevMode;

SYNTHESIZE_SINGLETON_HEADER_FOR_CLASS(KirinSuperDevMode)

#define KIRINDEV [KirinSuperDevMode sharedKirinSuperDevMode]

@end
