/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.adservices.ondevicepersonalization;

import android.annotation.NonNull;
import android.os.Parcelable;

import com.android.ondevicepersonalization.internal.util.AnnotationValidations;
import com.android.ondevicepersonalization.internal.util.DataClass;

import java.util.Collections;
import java.util.Map;

/**
 * Parcelable version of {@link InferenceOutput}.
 *
 * @hide
 */
@DataClass(genAidl = false, genBuilder = false)
public final class InferenceOutputParcel implements Parcelable {
    /**
     * A map mapping output indices to multidimensional arrays of output. For TFLite, this field is
     * mapped to outputs of runForMultipleInputsOutputs:
     * https://www.tensorflow.org/lite/api_docs/java/org/tensorflow/lite/InterpreterApi#parameters_9
     */
    @NonNull private Map<Integer, Object> mData = Collections.emptyMap();

    /** @hide */
    public InferenceOutputParcel(@NonNull InferenceOutput value) {
        this(value.getDataOutputs());
    }

    // Code below generated by codegen v1.0.23.
    //
    // DO NOT MODIFY!
    // CHECKSTYLE:OFF Generated code
    //
    // To regenerate run:
    // $ codegen
    // $ANDROID_BUILD_TOP/packages/modules/OnDevicePersonalization/framework/java/android/adservices/ondevicepersonalization/InferenceOutputParcel.java
    //
    // To exclude the generated code from IntelliJ auto-formatting enable (one-time):
    //   Settings > Editor > Code Style > Formatter Control
    // @formatter:off

    /**
     * Creates a new InferenceOutputParcel.
     *
     * @param data A map mapping output indices to multidimensional arrays of output. For TFLite,
     *     this field is mapped to outputs of runForMultipleInputsOutputs:
     *     https://www.tensorflow.org/lite/api_docs/java/org/tensorflow/lite/InterpreterApi#parameters_9
     */
    @DataClass.Generated.Member
    public InferenceOutputParcel(@NonNull Map<Integer, Object> data) {
        this.mData = data;
        AnnotationValidations.validate(NonNull.class, null, mData);

        // onConstructed(); // You can define this method to get a callback
    }

    /**
     * A map mapping output indices to multidimensional arrays of output. For TFLite, this field is
     * mapped to outputs of runForMultipleInputsOutputs:
     * https://www.tensorflow.org/lite/api_docs/java/org/tensorflow/lite/InterpreterApi#parameters_9
     */
    @DataClass.Generated.Member
    public @NonNull Map<Integer, Object> getData() {
        return mData;
    }

    @Override
    @DataClass.Generated.Member
    public void writeToParcel(@NonNull android.os.Parcel dest, int flags) {
        // You can override field parcelling by defining methods like:
        // void parcelFieldName(Parcel dest, int flags) { ... }

        dest.writeMap(mData);
    }

    @Override
    @DataClass.Generated.Member
    public int describeContents() {
        return 0;
    }

    /** @hide */
    @SuppressWarnings({"unchecked", "RedundantCast"})
    @DataClass.Generated.Member
    protected InferenceOutputParcel(@NonNull android.os.Parcel in) {
        // You can override field unparcelling by defining methods like:
        // static FieldType unparcelFieldName(Parcel in) { ... }

        Map<Integer, Object> data = new java.util.LinkedHashMap<>();
        in.readMap(data, Object.class.getClassLoader());

        this.mData = data;
        AnnotationValidations.validate(NonNull.class, null, mData);

        // onConstructed(); // You can define this method to get a callback
    }

    @DataClass.Generated.Member
    public static final @NonNull Parcelable.Creator<InferenceOutputParcel> CREATOR =
            new Parcelable.Creator<InferenceOutputParcel>() {
                @Override
                public InferenceOutputParcel[] newArray(int size) {
                    return new InferenceOutputParcel[size];
                }

                @Override
                public InferenceOutputParcel createFromParcel(@NonNull android.os.Parcel in) {
                    return new InferenceOutputParcel(in);
                }
            };

    @DataClass.Generated(
            time = 1706291599206L,
            codegenVersion = "1.0.23",
            sourceFile =
                    "packages/modules/OnDevicePersonalization/framework/java/android/adservices/ondevicepersonalization/InferenceOutputParcel.java",
            inputSignatures =
                    "private @android.annotation.NonNull java.util.Map<java.lang.Integer,java.lang.Object> mData\nclass InferenceOutputParcel extends java.lang.Object implements [android.os.Parcelable]\n@com.android.ondevicepersonalization.internal.util.DataClass(genAidl=false, genBuilder=false)")
    @Deprecated
    private void __metadata() {}

    // @formatter:on
    // End of generated code

}
