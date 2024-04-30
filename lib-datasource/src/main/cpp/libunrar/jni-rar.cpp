#include <jni.h>
#include <cstdlib>
#include <cstring>
#include <string>
#include <utility>
#include <vector>
#include <sys/stat.h>
#include <sys/mman.h>
#include <errno.h>
#include <locale>
#include <map>
#include "dmc_unrar.h"
#include "logger.h"

// context
dmc_unrar_archive _arc;

//
// Created by why on 4/30/24.
//
enum imgFormat {
    FORMAT_UNKNOWN = 0,
    FORMAT_JPG,
    FORMAT_PNG,
    FORMAT_BMP,
    FORMAT_WEBP
};

imgFormat validateName(std::string name) {
    int dot = name.find_last_of('.');
    if (dot == std::string::npos) {
        return FORMAT_UNKNOWN;
    }
    std::string format = name.substr(dot + 1);
    if (format == "jpg" || format == "jpeg" || format == "JPG" || format == "JPEG") {
        return FORMAT_JPG;
    } else if (format == "png" || format == "PNG") {
        return FORMAT_PNG;
    } else if (format == "bmp" || format == "BMP") {
        return FORMAT_BMP;
    } else if (format == "webp" || format == "WEBP") {
        return FORMAT_WEBP;
    }
    return FORMAT_UNKNOWN;
}

std::string get_filename(dmc_unrar_archive *archive, dmc_unrar_size_t i) {
    char *filename;
    dmc_unrar_size_t size = dmc_unrar_get_filename(archive, i, NULL, 0);
    if (!size)
        return std::string();

    filename = (char *) malloc(size);
    if (!filename)
        return std::string();

    size = dmc_unrar_get_filename(archive, i, filename, size);
    if (!size) {
        free(filename);
        return std::string();
    }

    dmc_unrar_unicode_make_valid_utf8(filename);
    if (filename[0] == '\0') {
        free(filename);
        return std::string();
    }

    return std::string(filename, strlen(filename));
}


extern "C"
JNIEXPORT jlong JNICALL
Java_com_xiaoyv_comic_datasource_impl_archive_ArchiveBookUnRar_loadFile(JNIEnv *env, jobject thiz,
                                                                        jstring file_path) {
    jboolean iscopy;
    const char *path = env->GetStringUTFChars(file_path, &iscopy);

    dmc_unrar_return err = dmc_unrar_archive_init(&_arc);
    if (err != DMC_UNRAR_OK) {
        LE("ArchiveBookUnRar::loadFile Archive init failed: %s", dmc_unrar_strerror(err));
        env->ReleaseStringUTFChars(file_path, path);
        return 0;
    }

    err = dmc_unrar_archive_open_path(&_arc, path);
    if (err != DMC_UNRAR_OK) {
        LE("FAILED TO OPEN RAR [%s] : %s", path, dmc_unrar_strerror(err));
        env->ReleaseStringUTFChars(file_path, path);
        return 0;
    }

    env->ReleaseStringUTFChars(file_path, path);
    return (jlong) &_arc;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xiaoyv_comic_datasource_impl_archive_ArchiveBookUnRar_free(JNIEnv *env, jobject thiz,
                                                                    jlong file_handle) {

    dmc_unrar_archive *arc = (dmc_unrar_archive *) file_handle;
    dmc_unrar_archive_close(arc);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_xiaoyv_comic_datasource_impl_archive_ArchiveBookUnRar_getPages(JNIEnv *env,
                                                                        jobject thiz,
                                                                        jlong file_handle) {
    dmc_unrar_archive *arc = (dmc_unrar_archive *) file_handle;
    int dmc_filecount = dmc_unrar_get_file_count(arc);

    std::map<int, std::string> entries;

    // 填充索引和文件名
    for (int index = 0; index < dmc_filecount; index++) {
        std::string name = get_filename(arc, index);
        if (validateName(name) != FORMAT_UNKNOWN) {
            entries[index] = name;
        }
    }

    // 创建 Java 的 HashMap 对象
    jclass mapClass = env->FindClass("java/util/HashMap");
    jmethodID initMethod = env->GetMethodID(mapClass, "<init>", "()V");
    jmethodID putMethod = env->GetMethodID(mapClass, "put",
                                           "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    jobject mapObj = env->NewObject(mapClass, initMethod);

    // 将 C++ 映射中的条目添加到 Java 的 HashMap 中
    for (const auto &entry: entries) {
        // Java <Integer,String>
        jobject key = env->NewObject(env->FindClass("java/lang/Integer"),
                                     env->GetMethodID(env->FindClass("java/lang/Integer"), "<init>",
                                                      "(I)V"), entry.first);
        jstring value = env->NewStringUTF(entry.second.c_str());

        // 赋值
        env->CallObjectMethod(mapObj, putMethod, key, value);

        // 释放局部引用
        env->DeleteLocalRef(key);
        env->DeleteLocalRef(value);
    }

    // 返回 Java 的 HashMap 对象
    return mapObj;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_xiaoyv_comic_datasource_impl_archive_ArchiveBookUnRar_extractPage(JNIEnv *env, jobject thiz,
                                                                           jlong file_handle,
                                                                           jint index,
                                                                           jstring outpath) {
    dmc_unrar_archive *arc = (dmc_unrar_archive *) file_handle;
    jboolean iscopy;
    const char *path = env->GetStringUTFChars(outpath, &iscopy);

    dmc_unrar_return extracted = dmc_unrar_extract_file_to_path(arc, index, path, nullptr, false);
    if (extracted != DMC_UNRAR_OK) {
        LE("extractRarEntry: failed extracting entry %s", dmc_unrar_strerror(extracted));
        return false;
    }
    return true;
}