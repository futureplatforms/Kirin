/* Generated from GwtLocationService
 * Do not edit, as this WILL be overwritten
 */
@protocol GwtLocationService <NSObject>

- (void) updatingLocationCallback: (NSString*) lat : (NSString*) lng : (NSString*) acc : (NSString*) timestamp;

- (void) hasPermissionCallback: (int) cbId : (BOOL) hasPermission;

- (void) locationError: (NSString*) err;

@end