/*
 *  Copyright (c) 2017 The WebRTC project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */

#ifndef WEBRTC_MODULES_DESKTOP_CAPTURE_WINDOW_UNDER_POINT_H_
#define WEBRTC_MODULES_DESKTOP_CAPTURE_WINDOW_UNDER_POINT_H_

#include "webrtc/modules/desktop_capture/desktop_capture_types.h"
#include "webrtc/modules/desktop_capture/desktop_geometry.h"

namespace webrtc {

// Returns the id of the visible window under |point|. This function returns
// kNullWindowId if no window is under |point| and the platform does not have
// "root window" concept, i.e. the visible area under |point| is the desktop.
WindowId GetWindowUnderPoint(DesktopVector point);

}  // namespace webrtc

#endif  // WEBRTC_MODULES_DESKTOP_CAPTURE_WINDOW_UNDER_POINT_H_
