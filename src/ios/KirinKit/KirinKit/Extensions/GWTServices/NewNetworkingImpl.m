//
//  NewNetworking.m
//  KirinKit
//
//  Created by Douglas Hoskins on 03/09/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import "NewNetworkingImpl.h"
#import "fromNative/NetworkingService.h"

@interface NewNetworkingImpl()
@property(strong) id<NetworkingService> kirinModule;
@end

@implementation NewNetworkingImpl

@synthesize kirinModule = kirinModule_;

- (id) init {
    self.serviceName = @"NetworkingService";
    self.kirinModuleProtocol = @protocol(NetworkingService);
    return [super initWithServiceName: self.serviceName];
}

- (void) retrieve: (int) ref : (NSString*) method : (NSString*) url : (NSString*) payload : (NSArray*) headerKeys : (NSArray*) headerVals {
    NSData *payloadData = [payload dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
    
    NSString *payloadLen = [NSString stringWithFormat:@"%d", [payloadData length]];
    
    NSMutableURLRequest *request = [[[NSMutableURLRequest alloc] init] autorelease];
    [request setURL:[NSURL URLWithString:url]];
    [request setHTTPMethod:method];
    [request setValue:payloadLen forHTTPHeaderField:@"Content-Length"];
    for (int i=0; i<headerKeys.count; i++) {
        NSString * key = headerKeys[i];
        NSString * val = headerVals[i];
        [request setValue:val forHTTPHeaderField:key];
    }
    [request setHTTPBody:payloadData];
    
    NSOperationQueue *queue = [[NSOperationQueue alloc] init];

    [NSURLConnection sendAsynchronousRequest:request
                                       queue:queue
                           completionHandler:
     ^(NSURLResponse *response, NSData *data, NSError *connectionError) {
         if (connectionError) {
             [self.kirinModule onError:ref];
         } else {
             NSString *respStr = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
             NSHTTPURLResponse * httpResp = (NSHTTPURLResponse*) response;
             int respCode = httpResp.statusCode;
             NSDictionary* headerDict = [httpResp allHeaderFields];
             NSMutableArray *keyArr = [[NSMutableArray alloc] init];
             NSEnumerator *keyEnum = [headerDict keyEnumerator];
             NSString *nextKey;
             while (nextKey = [keyEnum nextObject]) {
                 [keyArr addObject:nextKey];
             }
             
             NSMutableArray *valArr = [[NSMutableArray alloc] init];
             for (int i=0; i<keyArr.count; i++) {
                 NSString * val = [headerDict objectForKey:keyArr[i]];
                 [valArr addObject:val];
             }
             
             [self.kirinModule payload:ref :respCode :respStr :keyArr :valArr];
         }
     }];
}
@end
