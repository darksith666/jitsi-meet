/*
 * Copyright @ 2019-present 8x8, Inc.
 * Copyright @ 2017-2018 Atlassian Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jitsi.meet.sdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URL;

/**
 * Base Activity for applications integrating Jitsi Meet at a higher level. It
 * contains all the required wiring between the {@code JitsiMeetView} and
 * the Activity lifecycle methods already implemented.
 *
 * In this activity we use a single {@code JitsiMeetView} instance. This
 * instance gives us access to a view which displays the welcome page and the
 * conference itself. All lifetime methods associated with this Activity are
 * hooked to the React Native subsystem via proxy calls through the
 * {@code JitsiMeetView} static methods.
 */
public class JitsiMeetFragment extends Fragment {

    /**
     * A color scheme object to override the default color is the SDK.
     */
    private Bundle colorScheme;

    /**
     * Instance of the {@link JitsiMeetView} which this activity will display.
     */
    private JitsiMeetView view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return this.view = new JitsiMeetView(getActivity());
    }

    public JitsiMeetView getJitsiView() {
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        JitsiMeetActivityDelegate.onActivityResult(
                getActivity(), requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        if (view != null) {
            view.dispose();
            view = null;
        }

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        JitsiMeetActivityDelegate.onHostDestroy(getActivity());
    }

    // https://developer.android.com/reference/android/support/v4/app/ActivityCompat.OnRequestPermissionsResultCallback
    @Override
    public void onRequestPermissionsResult(
            final int requestCode,
            final String[] permissions,
            final int[] grantResults) {
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();

        JitsiMeetActivityDelegate.onHostResume(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();

        JitsiMeetActivityDelegate.onHostPause(getActivity());
    }

    public void enterPictureInPicture() {
        if (view != null) {
            view.enterPictureInPicture();
        }
    }

    /**
     *
     * @see JitsiMeetView#setColorScheme(Bundle)
     */
    public void setColorScheme(Bundle colorScheme) {
        if (view == null) {
            this.colorScheme = colorScheme;
        } else {
            view.setColorScheme(colorScheme);
        }
    }
}
