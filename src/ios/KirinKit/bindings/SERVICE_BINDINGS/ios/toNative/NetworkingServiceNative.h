/* Generated from NetworkingServiceNative
 * Do not edit, as this WILL be overwritten
 */
@protocol NetworkingServiceNative <NSObject>

- (void) retrieve: (int) connId : (NSString*) method : (NSString*) url : (NSString*) postData : (NSArray*) headerKeys : (NSArray*) headerVals;

- (void) retrieveB64: (int) connId : (NSString*) method : (NSString*) url : (NSString*) postData : (NSArray*) headerKeys : (NSArray*) headerVals;

- (void) retrieveToken: (int) connId : (NSString*) method : (NSString*) url : (NSString*) postData : (NSArray*) headerKeys : (NSArray*) headerVals;

@end