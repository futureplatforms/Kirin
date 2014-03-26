//
//  KirinUpload.m
//  RaceForLife
//
//  Created by Douglas Hoskins on 04/02/2014.
//  Copyright (c) 2014 Future Platforms. All rights reserved.
//

#import "KirinUpload.h"
#import <fromNative/GwtFileUploadService.h>

@interface KirinUpload() {
    NSMutableData *_responseData;
}
@property(strong) id<GwtFileUploadService> kirinModule;
@end

@implementation KirinUpload

@synthesize kirinModule = kirinModule_;

- (id) init {
    self.serviceName = @"GwtFileUploadService";
    self.kirinModuleProtocol = @protocol(GwtFileUploadService);
    return [super initWithServiceName: self.serviceName];
}

static UIImage *imageToUpload = nil;

+ (void)imageToUpload:(UIImage *)image
{
	imageToUpload = image;
}

- (void) uploadFile: (NSString*) url : (NSArray*) paramKeys : (NSArray*) paramVals : (NSString*) filename : (NSString*) token : (int) cbId {
    // Create the request.
    NSMutableURLRequest *postRequest = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]];
    
    // Set the request's content type to application/x-www-form-urlencoded
    NSString *boundary = @"--3424sfhfh873h3847h87hb_RFL";
    NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@", boundary];
    [postRequest setValue:contentType forHTTPHeaderField:@"Content-Type"];
    
    NSMutableData * body = [NSMutableData data];
    // append key parameter
    [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
    [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"key\"\r\n\r\nshared/%@", filename] dataUsingEncoding:NSUTF8StringEncoding]];
    [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
    
    // now append the supplied key/vals
    for (int i=0; i<[paramKeys count]; i++) {
        NSString *paramKey = [paramKeys objectAtIndex:i];
        NSString *paramVal = [paramVals objectAtIndex:i];
        
        [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"%@\"\r\n\r\n%@", paramKey, paramVal] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
        
    }
    
    // append file parameter
    [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"file\"; filename=\"%@\"\r\n\r\n", filename] dataUsingEncoding:NSUTF8StringEncoding]];
    //[body appendData:[@"Content-Type: image/png\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
    
    [body appendData:[NSData dataWithData:UIImagePNGRepresentation(imageToUpload)]];
    
    [body appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];

    [postRequest setHTTPBody:body];
    [postRequest setHTTPMethod:@"POST"];
    [NSURLConnection sendAsynchronousRequest:postRequest queue:[NSOperationQueue mainQueue] completionHandler:^(NSURLResponse *response, NSData *data, NSError *connectionError) {
        
        if (connectionError) {
            [self.kirinModule fileUploadError:cbId];
        } else {
            if ([response isKindOfClass:[NSHTTPURLResponse class]]) {
                int statusCode = [((NSHTTPURLResponse*) response) statusCode];
                NSString * dataStr = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
                if (statusCode >= 200 && statusCode < 300) {
                    [self.kirinModule fileUploadedOK:@"" :cbId];
                } else {
                    [self.kirinModule fileUploadError:cbId];
                }
            }
        }
    }];
    
}
@end
