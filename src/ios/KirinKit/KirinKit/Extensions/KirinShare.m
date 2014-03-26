//
//  KirinShare.m
//  RaceForLife
//
//  Created by Douglas Hoskins on 29/01/2014.
//  Copyright (c) 2014 Future Platforms. All rights reserved.
//

#import "KirinShare.h"
#import "fromNative/GwtShareService.h"


#pragma mark Private

@interface KirinShare()
{
	int storedCBid;
}

@property (strong) id <GwtShareService> kirinModule;

@end


#pragma mark - Implementation

@implementation KirinShare

@synthesize kirinModule = kirinModule_;


#pragma mark - Creation

- (id)init
{
	self.serviceName = @"GwtShareService";
	self.kirinModuleProtocol = @protocol(GwtShareService);
		
	return [super initWithServiceName: self.serviceName];
}


#pragma mark - Action sheet

- (void)showShareSheet:(NSArray *)platforms
{
	UIActionSheet * action = [[UIActionSheet alloc] initWithTitle:@"Choose share platform" delegate:self cancelButtonTitle:nil destructiveButtonTitle:nil otherButtonTitles:nil];

	for (NSString * platform in platforms)
	{
		[action addButtonWithTitle:platform];
	}
	
	[action addButtonWithTitle:@"Cancel"];
	action.cancelButtonIndex = platforms.count;

	[action showInView:[[[UIApplication sharedApplication] delegate] window]];
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
	if (buttonIndex == actionSheet.cancelButtonIndex)
	{
		[self.kirinModule shareSheetCancel];
	}
	else
	{
		[self.kirinModule shareSheetResponse:buttonIndex];
	}
}

- (void)actionSheetCancel:(UIActionSheet *)actionSheet
{
	[self.kirinModule shareSheetCancel];
}


#pragma mark - SMS

- (void)shareSms:(NSString *)text :(int)cbId
{
    MFMessageComposeViewController *sms = [MFMessageComposeViewController new];
    sms.body = text;
    sms.messageComposeDelegate = self;
	
	storedCBid = cbId;
	
	//[[ModalStack sharedInstance] presentViewController:sms];
}

- (void)messageComposeViewController:(MFMessageComposeViewController *)controller didFinishWithResult:(MessageComposeResult)result
{
	switch (result)
	{
		case MessageComposeResultSent:
			[self.kirinModule shareResponseWin:3:storedCBid];
			break;
			
		default:
			[self.kirinModule shareResponseFail:storedCBid];
			break;
	}
	
	//[[ModalStack sharedInstance] dismissViewController:controller];
}


#pragma mark - Twitter

- (void) shareTwitter: (NSString*) text : (NSString*) imgToken : (NSString*) link : (int) cbId
{
    /*SLComposeViewController *controller = [SLComposeViewController composeViewControllerForServiceType:SLServiceTypeTwitter];
    [controller setInitialText:text];
    
    if ([SharingModel sharedInstance].imageToUpload != nil)
	{
//		NSLog(@"shareTwitter %@ %@", imgToken, [[KIRIN dropbox] consumeObjectWithToken:imgToken]);
        // TODO load the image from wherever
		[controller addImage:[SharingModel sharedInstance].imageToUpload];
    }
    
    if (link != nil) {
        [controller addURL:[NSURL URLWithString:link]];
    }
	
	__weak typeof(SLComposeViewController) *weakController = controller;
    
    [controller setCompletionHandler:^(SLComposeViewControllerResult result) {
        
        switch (result)
		{
            case SLComposeViewControllerResultCancelled:
                [self.kirinModule shareResponseFail:cbId];
                break;
				
            case SLComposeViewControllerResultDone:
                [self.kirinModule shareResponseWin:1:cbId];
                break;
                
            default:
                break;
        }
		
		if (![UIDevice rfl_isIOS7])
		{
			[[ModalStack sharedInstance] dismissViewController:weakController];
		}
		else
		{
			[[ModalStack sharedInstance] removeViewController:weakController];
		}
		
    }];
    
	[[ModalStack sharedInstance] presentViewController:controller];*/
}


#pragma mark - Email

- (void)shareEmail:(NSString *)subject :(NSString *)text :(int)cbId
{
	/*MFMailComposeViewController *mail = [MFMailComposeViewController new];
	[mail setSubject:subject];
	[mail setMessageBody:text isHTML:NO];
	[mail setMailComposeDelegate:self];
	
	if ([SharingModel sharedInstance].imageToUpload != nil)
	{
		NSData *png = UIImagePNGRepresentation([SharingModel sharedInstance].imageToUpload);
		
		[mail addAttachmentData:png mimeType:@"image/png" fileName:@"race-for-life.png"];
	}
	
	storedCBid = cbId;

	[[ModalStack sharedInstance] presentViewController:mail];*/
}

- (void)mailComposeController:(MFMailComposeViewController *)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError *)error
{
	switch (result)
	{
		case MFMailComposeResultSent:
		case MFMailComposeResultSaved:
			[self.kirinModule shareResponseWin:2:storedCBid];
			break;
			
		default:
			[self.kirinModule shareResponseFail:storedCBid];
			break;
	}
	
	//[[ModalStack sharedInstance] dismissViewController:controller];
}


#pragma mark - Platforms

// 0 = facebook
// 1 = twitter
// 2 = email
// 3 = sms
- (void) getSupportedPlatforms
{
	NSMutableArray * arr = [[NSMutableArray alloc] init];

	BOOL tw = [SLComposeViewController isAvailableForServiceType:SLServiceTypeTwitter];
	if (tw)
	{
		[arr addObject:[NSNumber numberWithInt:1]];
	}

	BOOL email = [MFMailComposeViewController canSendMail];
	if (email)
	{
		[arr addObject:[NSNumber numberWithInt:2]];
	}

	BOOL sms = [MFMessageComposeViewController canSendText];
	if (sms)
	{
		[arr addObject:[NSNumber numberWithInt:3]];
	}

	[self.kirinModule supportedPlatformsResponse:arr];
}


@end
