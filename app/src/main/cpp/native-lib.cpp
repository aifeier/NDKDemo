#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_ai_ndkdemo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_example_ai_ndkdemo_MainActivity_getSum(JNIEnv *env, jobject instance, jint a, jint b) {

    return a + b;
    // TODO

}extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_ai_ndkdemo_MainActivity_stringFromJava(JNIEnv *env, jobject instance, jstring a_) {
    const char *a = env->GetStringUTFChars(a_, 0);
    if (NULL == a)
        return env->NewStringUTF("发生错误");
    char *b = "I from C++哈哈";
    int len = strlen(a) + strlen(b);
    char str[len];
    strcpy(str, a);
    strcat(str, b);
    env->ReleaseStringUTFChars(a_, a);
    return env->NewStringUTF(str);
}


//extern "C"{
//jintArray JNICALL
//Java_com_example_ai_ndkdemo_MainActivity_getImgToGray(JNIEnv *env, jobject instance,
//                                                       jintArray data_, jint w, jint h);
//}

extern "C"
JNIEXPORT jintArray JNICALL
Java_com_example_ai_ndkdemo_MainActivity_getImgToGray(JNIEnv *env, jobject instance, jintArray data_,
                                                      jint w, jint h) {

    jint *data;
    data = env->GetIntArrayElements(data_, NULL);
    if (data == NULL) {
        return 0; /* exception occurred */
    }
    int alpha = 0xFF << 24;
    for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
            // 获得像素的颜色
            int color = data[w * i + j];
            int red = ((color & 0x00FF0000) >> 16);
            int green = ((color & 0x0000FF00) >> 8);
            int blue = color & 0x000000FF;
            color = (red + green + blue) / 3;
            color = alpha | (color << 16) | (color << 8) | color;
            data[w * i + j] = color;
        }
    }
    int size=w * h;
    jintArray result = env->NewIntArray(size);
    env->SetIntArrayRegion(result, 0, size, data);
    env->ReleaseIntArrayElements(data_, data, 0);
    return result;

}