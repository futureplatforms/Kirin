/* Generated from GwtFacebookServiceNative
 * Do not edit, as this WILL be overwritten
 */
namespace Generated {
public interface GwtFacebookServiceNative {
void getAccessToken(int cbId);
void isLoggedIn(int cbId);
void getAppId(int cbId);
void openSessionWithReadPermissions(string[]  permissions, bool allowUI, int cbId);
void requestPublishPermissions(string[]  permissions, int cbId);
void getCurrentPermissions(int cbId);
void signOut();
void presentShareDialogWithParams(string caption, string description, string link, string name, string picture, string place, string reference, string[]  friends, int cbId);
void presentRequestsDialog(int cbId);
}}