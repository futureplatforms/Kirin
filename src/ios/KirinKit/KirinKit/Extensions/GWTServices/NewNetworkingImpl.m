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

- (void) retrieveB64: (int) ref : (NSString*) method : (NSString*) url : (NSString*) payload : (NSArray*) headerKeys : (NSArray*) headerVals {
    [self retrieve:ref
                  :method
                  :url
                  :payload
                  :headerKeys
                  :headerVals
                  :YES];
}

- (void) retrieve: (int) ref : (NSString*) method : (NSString*) url : (NSString*) payload : (NSArray*) headerKeys : (NSArray*) headerVals {
    [self retrieve:ref
                  :method
                  :url
                  :payload
                  :headerKeys
                  :headerVals
                  :NO];
}

- (void) retrieve:(int)ref :(NSString *)method :(NSString *)url :(NSString *)payload :(NSArray *)headerKeys :(NSArray *)headerVals :(BOOL) isB64 {
    NSData *payloadData = [payload dataUsingEncoding:NSUTF8StringEncoding allowLossyConversion:YES];
    
    NSString *payloadLen = [NSString stringWithFormat:@"%lu", (unsigned long)[payloadData length]];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
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
             NSString *respStr;
             if (isB64) {
                 respStr = [NewNetworkingImpl Base64Encode:data];
             } else {
                 respStr = [[NSString alloc] initWithData:data encoding:[NewNetworkingImpl stringEncodingFromString:[response textEncodingName]]];
             }
             
             NSHTTPURLResponse * httpResp = (NSHTTPURLResponse*) response;
             NSInteger respCode = httpResp.statusCode;
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

+(NSStringEncoding)stringEncodingFromString:(NSString *)strEncoding
{
    @try
    {
        // Default to NSUTF8StringEncoding if blank string supplied
        if ([[strEncoding stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] length] == 0)
            return NSUTF8StringEncoding;
        // Try and obtain the correct string encoding from a string
        CFStringRef cfStringRef = (__bridge CFStringRef)strEncoding;
        CFStringEncoding cfStringEnc = CFStringConvertIANACharSetNameToEncoding(cfStringRef);
        NSStringEncoding encoding = CFStringConvertEncodingToNSStringEncoding(cfStringEnc);
        
        return encoding;
    }
    @catch (NSException *exception)
    {
        // In-case the conversion failed
        return NSUTF8StringEncoding;
    }
}


+(NSString *)Base64Encode:(NSData *)data{
    //Point to start of the data and set buffer sizes
    int inLength = [data length];
    int outLength = ((((inLength * 4)/3)/4)*4) + (((inLength * 4)/3)%4 ? 4 : 0);
    const char *inputBuffer = [data bytes];
    char *outputBuffer = malloc(outLength+1);
    outputBuffer[outLength] = 0;
    
    //64 digit code
    static char Encode[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    
    //start the count
    int cycle = 0;
    int inpos = 0;
    int outpos = 0;
    char temp;
    
    //Pad the last to bytes, the outbuffer must always be a multiple of 4
    outputBuffer[outLength-1] = '=';
    outputBuffer[outLength-2] = '=';
    
    /* http://en.wikipedia.org/wiki/Base64
     Text content   M           a           n
     ASCII          77          97          110
     8 Bit pattern  01001101    01100001    01101110
     
     6 Bit pattern  010011  010110  000101  101110
     Index          19      22      5       46
     Base64-encoded T       W       F       u
     */
    
    
    while (inpos < inLength){
        switch (cycle) {
            case 0:
            outputBuffer[outpos++] = Encode[(inputBuffer[inpos]&0xFC)>>2];
            cycle = 1;
            break;
            case 1:
            temp = (inputBuffer[inpos++]&0x03)<<4;
            outputBuffer[outpos] = Encode[temp];
            cycle = 2;
            break;
            case 2:
            outputBuffer[outpos++] = Encode[temp|(inputBuffer[inpos]&0xF0)>> 4];
            temp = (inputBuffer[inpos++]&0x0F)<<2;
            outputBuffer[outpos] = Encode[temp];
            cycle = 3;
            break;
            case 3:
            outputBuffer[outpos++] = Encode[temp|(inputBuffer[inpos]&0xC0)>>6];
            cycle = 4;
            break;
            case 4:
            outputBuffer[outpos++] = Encode[inputBuffer[inpos++]&0x3f];
            cycle = 0;
            break;
            default:
            cycle = 0;
            break;
        }
    }
    NSString *pictemp = [NSString stringWithUTF8String:outputBuffer];
    free(outputBuffer); 
    return pictemp;
}
@end
