#include <jni.h>
#include <mobi.h>
#include "common.h"
#include "mobitool.h"


/**
 * 提取加载 mobi 的公共方法
 *
 * @param env
 * @param mobi_path
 * @return
 */
MOBIData *load_mobi(JNIEnv *env, jstring mobi_path) {
    jboolean iscopy;
    char *input = (char *) (*env)->GetStringUTFChars(env, mobi_path, &iscopy);
    MOBI_RET mobi_ret;

    MOBIData *m = mobi_init();
    if (m == NULL) {
        printf("Memory allocation failed\n");
        return NULL;
    }

    FILE *file = fopen(input, "rb");
    if (file == NULL) {
        printf("Error opening file: %s\n", input);
        mobi_free(m);
        return NULL;
    }

    mobi_ret = mobi_load_file(m, file);
    fclose(file);

    if (mobi_ret != MOBI_SUCCESS) {
        printf("Error while loading document (%s)\n", libmobi_msg(mobi_ret));
        mobi_free(m);
        return NULL;
    }

    // 释放字符串资源
    (*env)->ReleaseStringUTFChars(env, mobi_path, input);
    return m;
}

MOBIRawml *load_rawml(MOBIData *m) {
    MOBIRawml *rawml = mobi_init_rawml(m);
    if (rawml == NULL) {
        printf("Memory allocation failed\n");
        mobi_free(m);
        return NULL;
    }

    MOBI_RET mobi_ret;
    mobi_ret = mobi_parse_rawml(rawml, m);
    if (mobi_ret != MOBI_SUCCESS) {
        printf("Parsing rawml failed (%s)\n", libmobi_msg(mobi_ret));
        mobi_free(m);
        mobi_free_rawml(rawml);
        return NULL;
    }
    return rawml;
}

JNIEXPORT jint JNICALL
Java_com_xiaoyv_comic_era_NativeMobi_convertToEpub(JNIEnv *env,
                                                   __attribute__((unused)) jobject thiz,
                                                   jstring mobi_path,
                                                   jstring out_path) {
    int ret = SUCCESS;

    MOBIData *m = load_mobi(env, mobi_path);
    if (m == NULL) {
        return ERROR;
    }

    print_meta(m);

    MOBIRawml *rawml = load_rawml(m);
    if (rawml == NULL) {
        return ERROR;
    }

    MOBI_RET mobi_ret;
    mobi_ret = mobi_parse_rawml(rawml, m);
    if (mobi_ret != MOBI_SUCCESS) {
        printf("Parsing rawml failed (%s)\n", libmobi_msg(mobi_ret));
        mobi_free(m);
        mobi_free_rawml(rawml);
        return ERROR;
    }

    jboolean iscopy;
    char *output = (char *) (*env)->GetStringUTFChars(env, out_path, &iscopy);
    ret = create_epub(rawml, output);

    if (ret != SUCCESS) {
        printf("Creating EPUB failed\n");
    }

    (*env)->ReleaseStringUTFChars(env, out_path, output);

    mobi_free_rawml(rawml);
    mobi_free(m);
    return ret;
}

JNIEXPORT jint JNICALL
Java_com_xiaoyv_comic_era_NativeMobi_extractCover(JNIEnv *env,
                                                  __attribute__((unused)) jobject thiz,
                                                  jstring mobi_path,
                                                  jstring out_path) {
    int ret = SUCCESS;

    MOBIData *m = load_mobi(env, mobi_path);
    if (m == NULL) {
        return ERROR;
    }

    jboolean iscopy;
    char *output = (char *) (*env)->GetStringUTFChars(env, out_path, &iscopy);

    ret = dump_cover(m, output);

    (*env)->ReleaseStringUTFChars(env, out_path, output);
    return ret;
}