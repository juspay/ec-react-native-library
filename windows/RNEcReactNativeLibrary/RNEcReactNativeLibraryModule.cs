using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Ec.React.Native.Library.RNEcReactNativeLibrary
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNEcReactNativeLibraryModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNEcReactNativeLibraryModule"/>.
        /// </summary>
        internal RNEcReactNativeLibraryModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNEcReactNativeLibrary";
            }
        }
    }
}
