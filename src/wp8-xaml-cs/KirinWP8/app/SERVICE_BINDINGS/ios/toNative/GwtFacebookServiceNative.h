/* Generated from GwtFacebookServiceNative
 * Do not edit, as this WILL be overwritten
 */
@protocol GwtFacebookServiceNative <NSObject>

- (void) getAccessToken: (int) cbId;

- (void) isLoggedIn: (int) cbId;

- (void) getAppId: (int) cbId;

- (void) openSessionWithReadPermissions: (NSArray*) permissions : (BOOL) allowUI : (int) cbId;

- (void) requestPublishPermissions: (NSArray*) permissions : (int) cbId;

- (void) getCurrentPermissions: (int) cbId;

- (void) signOut;

- (void) presentShareDialogWithParams: (NSString*) caption : (NSString*) description : (NSString*) link : (NSString*) name : (NSString*) picture : (NSString*) place : (NSString*) reference : (NSArray*) friends : (int) cbId;

- (void) presentRequestsDialog: (int) cbId;

@end