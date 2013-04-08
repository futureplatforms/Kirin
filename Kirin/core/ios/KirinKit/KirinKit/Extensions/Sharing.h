#import <Foundation/Foundation.h>

/**
 * Unfortunately this needs to be implemented by the application, as putting an implementation in KirinKit that 
 * uses SLComposeViewController would tie it to ios 6+ only.
 */
@protocol Sharing <NSObject>

- (void) share: (NSString*) text withThisLink: (NSString*) linkUrl usingService: (NSString*) serviceNameHint;
- (void) share: (NSString*) text usingService: (NSString*) serviceNameHint;

@end