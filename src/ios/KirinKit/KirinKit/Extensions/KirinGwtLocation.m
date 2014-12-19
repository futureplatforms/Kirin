//
//  KirinGwtLocation.m
//  KirinKit
//
//  Created by Douglas Hoskins on 01/05/2014.
//  Copyright (c) 2014 Future Platforms. All rights reserved.
//

#import "KirinGwtLocation.h"
#import "fromNative/GwtLocationService.h"

@interface KirinGwtLocation() {
    
}

@property(strong) id<GwtLocationService> kirinModule;
@property(nonatomic, strong) CLLocationManager* locationManager;

@end

@implementation KirinGwtLocation

- (id) init {
    self.serviceName = @"GwtLocationService";
    self.kirinModuleProtocol = @protocol(GwtLocationService);
    self.locationManager = [[CLLocationManager alloc] init];
    self.locationManager.delegate = self;
    return [super initWithServiceName: self.serviceName];
}

- (void) hasPermission: (int) cbId {
    BOOL allowed = [CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorized;
    [self.kirinModule hasPermissionCallback:cbId :allowed];
}

+ (CLLocationAccuracy) clLocationAccuracyForKirinAccuracy: (int) acc {
    if (acc == 0) {
        // fine
        // PRIORITY_HIGH_ACCURACY on Android
        return kCLLocationAccuracyBest;
    } else if (acc == 1) {
        // medium
        // PRIORITY_BALANCED_POWER_ACCURACY on Android
        return kCLLocationAccuracyHundredMeters;
    } else if (acc == 2) {
        // coarse
        // PRIORITY_LOW_POWER on Android
        return kCLLocationAccuracyThreeKilometers;
    } else {
        // no power, does not exist on iOS
        return kCLLocationAccuracyThreeKilometers;
    }
}

- (void) startUpdating: (int) accuracy {
    self.locationManager.desiredAccuracy = [KirinGwtLocation clLocationAccuracyForKirinAccuracy:accuracy];
    [self.locationManager startUpdatingLocation];
}

- (void) stopUpdating {
    [self.locationManager stopUpdatingLocation];
}

- (void)locationManager:(CLLocationManager *)manager
	 didUpdateLocations:(NSArray *)locations {
    CLLocation * location = self.locationManager.location;
    NSString *lat = [NSString stringWithFormat:@"%f", location.coordinate.latitude];
    NSString *lng = [NSString stringWithFormat:@"%f", location.coordinate.longitude];
    NSString *acc = [NSString stringWithFormat:@"%f", location.horizontalAccuracy];
    NSString *time = [NSString stringWithFormat:@"%f", [location.timestamp timeIntervalSince1970]];
    
    [self.kirinModule updatingLocationCallback:lat :lng :acc :time];
}

- (void)locationManager:(CLLocationManager *)manager
       didFailWithError:(NSError *) error {
    [self.kirinModule locationError:[error debugDescription]];
}

@end
