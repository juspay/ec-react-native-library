
#import "RNEcReactNativeLibrary.h"
#import <HyperSDK/HyperSDK.h>

@interface RNEcReactNativeLibrary()

@property (nonatomic, strong) Hyper *hyper;

@end

@implementation RNEcReactNativeLibrary

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(startPayment:(NSString *)payload :(RCTResponseSenderBlock)successCallback :(RCTResponseSenderBlock)errorCallback) {
  
  NSError *error;
  NSData *data = [payload dataUsingEncoding:NSUTF8StringEncoding];
  NSDictionary *payloadDictionary = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
  
  if (error) {
    
      errorCallback(@[@"Error while parsing the payment parameters"]);
  } else {
    
    self.hyper = [[Hyper alloc] init];
    
    NSMutableDictionary *paymentParams = [[NSMutableDictionary alloc] init];
    
    [paymentParams setDictionary:[payloadDictionary valueForKey:@"baseParams"]];
    
    NSDictionary *serviceParams = [payloadDictionary valueForKey:@"serviceParams"];
    for (NSString *key in serviceParams) {
      [paymentParams setValue:[serviceParams valueForKey:key] ? [serviceParams valueForKey:key] : @"" forKey:key];
    }
    
    NSDictionary *customParams = [payloadDictionary valueForKey:@"customParams"];
    for (NSString *key in customParams) {
      [paymentParams setValue:[customParams valueForKey:key] ? [customParams valueForKey:key] : @"" forKey:key];
    }
    
    UIViewController *rootViewController = [UIApplication sharedApplication].delegate.window.rootViewController;
    
    [self.hyper startViewController:rootViewController data:paymentParams callback:^(int status, id _Nullable responseData, NSError * _Nullable error) {
      
        NSString *responseString = [self dictionaryToString:responseData];
        successCallback(@[responseString]);
    }];
  }
  
}

- (NSString*)dictionaryToString:(id)dict{
  
  if (!dict || ![NSJSONSerialization isValidJSONObject:dict]) {
    return @"";
  }
  
  NSString *data = [[NSString alloc] initWithData:[NSJSONSerialization dataWithJSONObject:dict options:0 error:nil] encoding:NSUTF8StringEncoding];
  return data;
}

@end
  