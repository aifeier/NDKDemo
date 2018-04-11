#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_ai_ndkdemo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++ 哈哈";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_ai_ndkdemo_MainActivity_stringFromJNI2(
        JNIEnv *env,
        jobject) {
    std::string hh = "方法二";
    return env->NewStringUTF(hh.c_str());

}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_ai_ndkdemo_MainActivity_getSum(JNIEnv *env, jobject instance, jint a, jint b) {

    return a + b;
    // TODO

}