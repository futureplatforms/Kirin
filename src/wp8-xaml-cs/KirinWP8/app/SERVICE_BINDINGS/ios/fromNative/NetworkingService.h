/* Generated from NetworkingService
 * Do not edit, as this WILL be overwritten
 */
@protocol NetworkingService <NSObject>

- (void) payload: (int) connId : (int) respCode : (NSString*) str : (NSArray*) headerKeys : (NSArray*) headerVals;

- (void) onError: (int) connId;

@end