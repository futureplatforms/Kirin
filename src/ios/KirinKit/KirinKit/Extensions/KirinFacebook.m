//
//  KirinFacebook.m
//  RaceForLife
//
//  Created by Douglas Hoskins on 24/01/2014.
//  Copyright (c) 2014 Future Platforms. All rights reserved.
//

#import "KirinFacebook.h"
#import "fromNative/GwtFacebookService.h"
#import <FacebookSDK/FacebookSDK.h>
#import <Social/Social.h>

@interface KirinFacebook() {
}
@property(strong) id<GwtFacebookService> kirinModule;
@end

@implementation KirinFacebook

@synthesize kirinModule = kirinModule_;

- (id) init {
    self.serviceName = @"GwtFacebookService";
    self.kirinModuleProtocol = @protocol(GwtFacebookService);
    
    [FBLoginView class];
    
    // Whenever a person opens the app, check for a cached session
    if (FBSession.activeSession.state == FBSessionStateCreatedTokenLoaded) {
        
        // If there's one, just open the session silently, without showing the user the login UI
        [FBSession openActiveSessionWithReadPermissions:@[@"basic_info"]
                                           allowLoginUI:NO
                                      completionHandler:^(FBSession *session, FBSessionState state, NSError *error) {
                                          // Handler for session state changes
                                          // This method will be called EACH time the session state changes,
                                          // also for intermediate states and NOT just when the session open
                                          [self sessionStateChanged:session state:state error:error];
                                      }];
    }
    
    return [super initWithServiceName: self.serviceName];
}

+ (void) handleDidBecomeActive {
    [FBSession.activeSession handleDidBecomeActive];
}

+ (BOOL) handleOpenUrl:(NSURL *)url {
    return [FBSession.activeSession handleOpenURL:url];
}

// This method will handle ALL the session state changes in the app
- (void)sessionStateChanged:(FBSession *)session state:(FBSessionState) state error:(NSError *)error
{
    // If the session was opened successfully
    if (!error && state == FBSessionStateOpen){
        // Show the user the logged-in UI
       // [self userLoggedIn];
        return;
    }
    if (state == FBSessionStateClosed || state == FBSessionStateClosedLoginFailed){
        // If the session is closed
       // NSLog(@"Session closed");
        // Show the user the logged-out UI
     //   [self userLoggedOut];
    }
    
    // Handle errors
    if (error){
       // NSLog(@"Error");
        NSString *alertText;
        NSString *alertTitle;
        // If the error requires people using an app to make an action outside of the app in order to recover
        if ([FBErrorUtility shouldNotifyUserForError:error] == YES){
            alertTitle = @"Something went wrong";
            alertText = [FBErrorUtility userMessageForError:error];
          //  [self showMessage:alertText withTitle:alertTitle];
        } else {
            
            // If the user cancelled login, do nothing
            if ([FBErrorUtility errorCategoryForError:error] == FBErrorCategoryUserCancelled) {
                
                // Handle session closures that happen outside of the app
            } else if ([FBErrorUtility errorCategoryForError:error] == FBErrorCategoryAuthenticationReopenSession){
                alertTitle = @"Session Error";
                alertText = @"Your current session is no longer valid. Please log in again.";
                //[self showMessage:alertText withTitle:alertTitle];
                
                // Here we will handle all other errors with a generic error message.
                // We recommend you check our Handling Errors guide for more information
                // https://developers.facebook.com/docs/ios/errors/
            } else {
                //Get more error information from the error
                NSDictionary *errorInformation = [[[error.userInfo objectForKey:@"com.facebook.sdk:ParsedJSONResponseKey"] objectForKey:@"body"] objectForKey:@"error"];
                
                // Show the user an error message
                alertTitle = @"Something went wrong";
                alertText = [NSString stringWithFormat:@"Please retry. \n\n If the problem persists contact us and mention this error code: %@", [errorInformation objectForKey:@"message"]];
                //[self showMessage:alertText withTitle:alertTitle];
            }
        }
        // Clear this token
        //[FBSession.activeSession closeAndClearTokenInformation];
        // Show the user the logged-out UI
     //   [self userLoggedOut];
    }
}

- (void) getAccessToken: (int) cbId {
    NSString *fbAccessToken = [FBSession activeSession].accessTokenData.accessToken;
    NSDate *fbAccessTokenExpDate = [FBSession activeSession].accessTokenData.expirationDate;
    NSDateFormatter *isoFormat = [[NSDateFormatter alloc] init];
    //2014-06-29T17:45:02.481+0100
    [isoFormat setDateFormat:@"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"];
    NSString *createdDateStr = [isoFormat stringFromDate:fbAccessTokenExpDate];

    [self.kirinModule setAccessToken: cbId :fbAccessToken :createdDateStr];
}

- (void) getAppId:(int) cbId {
    NSString *appId = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"FacebookAppID"];
    [self.kirinModule setAppId: cbId: appId];
}

- (void) isLoggedIn:(int) cbId {
    if (FBSession.activeSession.state == FBSessionStateOpen ||
        FBSession.activeSession.state == FBSessionStateOpenTokenExtended) {
        [self.kirinModule setIsLoggedIn: cbId :YES];
    } else {
        [self.kirinModule setIsLoggedIn: cbId : NO];
    }
}

- (void) openSessionWithReadPermissions: (NSArray*) permissions : (BOOL) allowUI : (int) cbId {
    dispatch_async(dispatch_get_main_queue(), ^{
        [self _openSessionWithReadPermissions:permissions :allowUI :cbId];
    });
}
- (void) _openSessionWithReadPermissions: (NSArray*) permissions : (BOOL) allowUI : (int) cbId {
    // If the session state is any of the two "open" states when the button is clicked
    if (FBSession.activeSession.state != FBSessionStateOpen &&
        FBSession.activeSession.state != FBSessionStateOpenTokenExtended) {
        // Open a session showing the user the login UI
        // You must ALWAYS ask for basic_info permissions when opening a session
       // [FBSession activeSession]
        __block BOOL workaroundOneTimeRunFlag = NO;
        __block int _cbId = cbId;
        BOOL ret = [FBSession openActiveSessionWithReadPermissions:permissions
                                                      allowLoginUI:allowUI
                                                 completionHandler:
                    ^(FBSession *session, FBSessionState state, NSError *error) {
                        if (!workaroundOneTimeRunFlag) {
                            workaroundOneTimeRunFlag = YES;
                        
                            if (error) {
                                if ([FBErrorUtility errorCategoryForError:error] == FBErrorCategoryUserCancelled) {
                                    [self.kirinModule openSessionCancel:_cbId];
                                } else if ([FBErrorUtility errorCategoryForError:error] == FBErrorCategoryAuthenticationReopenSession) {
                                    [self.kirinModule openSessionAuthenticationFailed:_cbId];
                                } else {
                                    [self.kirinModule openSessionError:_cbId];
                                }
                            } else {
                                [self.kirinModule openSessionSuccess:_cbId];
                            }
                        }
                    }];
    } else {
        [self.kirinModule openSessionSuccess:cbId];
    }
}

- (void) getCurrentPermissions: (int) cbId {
    NSMutableArray *arr;
    if ([FBSession.activeSession.permissions count] > 0) {
        arr = [[NSMutableArray alloc] initWithArray:FBSession.activeSession.permissions];
    } else {
        arr = [[NSMutableArray alloc] init];
    }
    [self.kirinModule setCurrentPermissions: cbId:arr];
}

- (void) requestPublishPermissions: (NSArray*) permissions : (int) cbId {
    __block BOOL workaroundOneTimeRunFlag = NO;
    __block int _cbId = cbId;
    [FBSession.activeSession requestNewPublishPermissions:permissions
                                          defaultAudience:FBSessionDefaultAudienceFriends
                                        completionHandler:^(FBSession *session, NSError *error) {
                                            if (!workaroundOneTimeRunFlag) {
                                                workaroundOneTimeRunFlag = YES;
                                                if (!error) {
                                                    for (NSString * permission in permissions) {
                                                        if ([FBSession.activeSession.permissions indexOfObject:permission] == NSNotFound) {
                                                            [self.kirinModule requestPublishCancel:_cbId];
                                                            return;
                                                        }
                                                    }
                                                
                                                    [self.kirinModule requestPublishSuccess:_cbId];
                                                } else {
                                                    [self.kirinModule requestPublishError: _cbId];
                                                }
                                            }
                                        }];
}

-(void) signOut {
    [[FBSession activeSession] closeAndClearTokenInformation];
}

- (void) presentShareDialogWithParams: (NSString*) caption : (NSString*) description : (NSString*) link : (NSString*) name : (NSString*) picture : (NSString*) place : (NSString*) ref : (int) cbId {
    FBShareDialogParams *shareParams = [[FBShareDialogParams alloc] init];
    NSMutableDictionary *feedParams = [[NSMutableDictionary alloc] init];
    if (caption != nil) {
        shareParams.caption = caption;
        [feedParams setObject:caption forKey:@"caption"];
    }
    if (description != nil) {
        shareParams.description = description;
        [feedParams setObject:description forKey:@"description"];
    }
    if (link != nil) {
        shareParams.link = [NSURL URLWithString:link];
        [feedParams setObject:link forKey:@"link"];
    }
    if (name != nil) {
        shareParams.name = name;
        [feedParams setObject:name forKey:@"name"];
    }
    if (picture != nil) {
        shareParams.picture = [NSURL URLWithString:picture];
        [feedParams setObject:picture forKey:@"picture"];
    }
    if (place != nil) {
        shareParams.place = place;
        [feedParams setObject:place forKey:@"place"];
    }
    if (ref != nil) {
        shareParams.ref = ref;
        [feedParams setObject:ref forKey:@"ref"];
    }
    
    if ([FBDialogs canPresentShareDialogWithParams:shareParams]) {
        [FBDialogs presentShareDialogWithParams:shareParams
                                    clientState:nil
                                        handler:^(FBAppCall *call, NSDictionary *results, NSError *error) {
                                            if(error) {
                                                // An error occurred, we need to handle the error
                                                // See: https://developers.facebook.com/docs/ios/errors
                                                [self.kirinModule shareErr:cbId];
                                            } else {
                                                // Success
                                                if ([@"cancel" isEqualToString:[results valueForKey:@"completionGesture"]]) {
                                                    [self.kirinModule shareErr:cbId];
                                                } else {
                                                    [self.kirinModule shareSuccess:cbId :@"win"];
                                                }
                                            }
                                        }];
    } else {
        [FBWebDialogs presentFeedDialogModallyWithSession:FBSession.activeSession
                                               parameters:feedParams
                                                  handler:^(FBWebDialogResult result, NSURL *resultURL, NSError *error) {
                                                      if (error) {
                                                          [self.kirinModule shareErr:cbId];
                                                      } else {
                                                          if (result == FBWebDialogResultDialogCompleted) {
                                                              // If we shared OK the result URL looks like:
                                                              // fbconnect://success?post_id=100007574855460_1392017554394060
                                                              // If not, it just looks like:
                                                              // fbconnect://success
                                                              // Test if the query begins with post_id
                                                              
                                                              NSString *query = [resultURL query];
                                                              
                                                              if ([query hasPrefix:@"post_id"]) {
                                                                  [self.kirinModule shareSuccess:cbId :[resultURL absoluteString]];
                                                              } else {
                                                                  [self.kirinModule shareCancel:cbId];
                                                              }
                                                          } else {
                                                              [self.kirinModule shareCancel:cbId];
                                                          }
                                                      }
                                                  }];
    }
}

- (void) presentRequestsDialog: (int) cbId {
    NSLog(@"About to present requests dialog");
    [FBWebDialogs presentRequestsDialogModallyWithSession:nil
                                                  message:@"request"
                                                    title:@"request"
                                               parameters:nil
                                                  handler:
    ^(FBWebDialogResult result, NSURL *resultURL, NSError *error) {
        NSLog(@"Result! %@", resultURL);
        //
        //
        if (error) {
            [self.kirinModule requestsDialogFail:cbId];
        } else {
            if (result == FBWebDialogResultDialogCompleted) {
                // If we shared OK the result URL looks like:
                // fbconnect://success?to=100007574855460_1392017554394060
                // If not, it just looks like:
                // fbconnect://success
                // Test if the query begins with post_id
                
                NSString *query = [resultURL query];
                NSRange range = [query rangeOfString:@"error_code"];
                if (range.length > 0) {
                    // pressed Cancel
                    [self.kirinModule requestsDialogCancel:cbId];
                } else {
                    NSArray * tos = [KirinFacebook parseTosFromQuery:[resultURL query]];
                    [self.kirinModule requestsDialogSuccess:cbId :tos];
                }
            } else {
                // pressed X
                [self.kirinModule requestsDialogCancel:cbId];
            }
        }
    }];
}

+ (NSArray*) parseTosFromQuery: (NSString *) query {
    NSMutableArray * tos = [[NSMutableArray alloc] init];
    for (NSString *qs in [query componentsSeparatedByString:@"&"]) {
        NSString *value = [[[[qs componentsSeparatedByString:@"="] objectAtIndex:1]
                            stringByReplacingOccurrencesOfString:@"+" withString:@" "]
                           stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        NSString *key = [[qs componentsSeparatedByString:@"="] objectAtIndex:0];
        
        if ([key isEqualToString:@"to"]) {
            [tos addObject:value];
        }
    }
    return tos;
}

@end
