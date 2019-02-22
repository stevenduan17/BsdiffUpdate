#include <jni.h>
#include <string>


extern "C" {
extern int patch_main(int argc, char *argv[]);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_steven_bsdiff_MainActivity_patchNewApk(JNIEnv *env, jobject instance, jstring oldApk_, jstring patchFile_,
                                                jstring newApk_) {
    const char *oldApk = env->GetStringUTFChars(oldApk_, 0);
    const char *patchFile = env->GetStringUTFChars(patchFile_, 0);
    const char *newApk = env->GetStringUTFChars(newApk_, 0);

    const char *argv[] = {"", oldApk, newApk, patchFile};
    patch_main(4, const_cast<char **>(argv));

    env->ReleaseStringUTFChars(oldApk_, oldApk);
    env->ReleaseStringUTFChars(patchFile_, patchFile);
    env->ReleaseStringUTFChars(newApk_, newApk);
}