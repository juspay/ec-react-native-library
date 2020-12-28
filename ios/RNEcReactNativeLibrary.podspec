Pod::Spec.new do |s|
  s.name         = "RNEcReactNativeLibrary"
  s.version      = "1.0.13"
  s.summary      = "RNEcReactNativeLibrary"
  s.description  = <<-DESC
                  RNEcReactNativeLibrary
                   DESC
  s.homepage     = "https://github.com/juspay/ec-react-native-library.git"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "9.0"
  s.source       = { :git => "https://github.com/juspay/ec-react-native-library.git", :tag => "1.0.13" }
  s.source_files = "**/*.{h,m}"
  s.header_dir   = "RNEcReactNativeLibrary"
  s.requires_arc = true
  s.static_framework = true
  
  s.dependency "React"
  s.dependency "HyperSDK","0.2.102"

end