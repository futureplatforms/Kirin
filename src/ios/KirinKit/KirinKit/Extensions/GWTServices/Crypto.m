//
//  Crypto.m
//  KirinKit
//
//  Created by Douglas Hoskins on 11/04/2014.
//  Copyright (c) 2014 Future Platforms. All rights reserved.
//

#import "Crypto.h"
#import <fromNative/CryptoService.h>
#import <CommonCrypto/CommonKeyDerivation.h> 

@interface Crypto() {
    
}
@property(strong) id<CryptoService> kirinModule;
@end

@implementation Crypto

- (id) init {
    self.serviceName = @"CryptoService";
    self.kirinModuleProtocol = @protocol(CryptoService);
    return [super initWithServiceName: self.serviceName];
}

- (void) pbkdf2: (NSString*) cbId : (NSString*) plaintext : (NSString*) salt : (int) iterations : (int) keyLenBytes {
    NSData *saltData = [salt dataUsingEncoding:NSUTF8StringEncoding];
    unsigned char key[keyLenBytes];
    CCKeyDerivationPBKDF(
                         kCCPBKDF2,
                         [plaintext UTF8String],
                         [plaintext lengthOfBytesUsingEncoding: NSUTF8StringEncoding],
                         [saltData bytes],
                         [saltData length],
                         kCCPRFHmacAlgSHA1,
                         iterations,
                         key,
                         sizeof(key));
    
    NSMutableString* res = [[NSMutableString alloc] init];
    for (int i = 0 ; i < sizeof(key) ; ++i)
    {
        [res appendFormat: @"%02x", key[i]];
    }

    [self.kirinModule result:cbId :res];
}
@end
