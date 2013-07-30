//
//  ViewController.m
//  KirinHelloiPhone
//
//  Created by Douglas Hoskins on 24/07/2013.
//  Copyright (c) 2013 Future Platforms. All rights reserved.
//

#import "ViewController.h"
#import <KirinKit/NSObject+Kirin.h>
#import "fromNative/TestModule.h"

@interface ViewController ()
@property (strong) id <TestModule> kirinModule;
@end


@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    [self kirinStartModule:@"TestModule" withProtocol:@protocol(TestModule)];
    [self.kirinModule testyMethod:@"Hey hey hey!" :1337];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) testyNativeMethod: (NSString*) str {
    NSLog(@"testyNativeMethod: %@", str);
}

@end
