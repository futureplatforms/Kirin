/* Generated from GwtFacebookService
 * Do not edit, as this WILL be overwritten
 */
@protocol GwtFacebookService <NSObject>

- (void) openSessionSuccess: (int) fbId;

- (void) openSessionCancel: (int) fbId;

- (void) openSessionError: (int) fbId;

- (void) openSessionAuthenticationFailed: (int) fbId;

- (void) openSessionErrorWithUserMessage: (int) fbId : (NSString*) msg;

- (void) requestPublishSuccess: (int) fbId;

- (void) requestPublishCancel: (int) fbId;

- (void) requestPublishError: (int) fbId;

- (void) requestPublishAuthenticationFailed: (int) fbId;

- (void) shareSuccess: (int) cbId : (NSString*) res;

- (void) shareCancel: (int) cbId;

- (void) shareErr: (int) cbId;

- (void) setAccessToken: (int) cbId : (NSString*) accessToken : (NSString*) expirationDate;

- (void) setIsLoggedIn: (int) cbId : (BOOL) isLoggedIn;

- (void) setAppId: (int) cbId : (NSString*) appId;

- (void) setCurrentPermissions: (int) cbId : (NSArray*) currentPermissions;

- (void) requestsDialogSuccess: (int) cbId : (NSString*) respQuery;

- (void) requestsDialogCancel: (int) cbId;

- (void) requestsDialogFail: (int) cbId;

@end