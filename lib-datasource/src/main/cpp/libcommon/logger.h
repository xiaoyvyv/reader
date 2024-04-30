//
// Created by why on 4/23/24.
//
#include <android/log.h>

#ifndef COMICREADER_LOGGER_H
#define COMICREADER_LOGGER_H

#define ORE_LOG_TAG "NativeLib"

int printf(const char *__fmt, ...);

#ifdef DEBUGLOG
#define LV(...)  __android_log_print(ANDROID_LOG_VERBOSE, ORE_LOG_TAG, __VA_ARGS__)
#define LD(...)  __android_log_print(ANDROID_LOG_DEBUG, ORE_LOG_TAG, __VA_ARGS__)
#define LDD(ENABLED, args...) { if (ENABLED) {__android_log_print(ANDROID_LOG_DEBUG, ORE_LOG_TAG, args); } }
#define LI(...)  __android_log_print(ANDROID_LOG_INFO, ORE_LOG_TAG, __VA_ARGS__)
#define LW(...)  __android_log_print(ANDROID_LOG_WARN, ORE_LOG_TAG, __VA_ARGS__)
#define LE(...)  __android_log_print(ANDROID_LOG_ERROR, ORE_LOG_TAG, __VA_ARGS__)
#else
#define LV(...)
#define LD(...)
#define LDD(ENABLED, args...)
#define LI(...)
#define LW(...)
#define LE(...)
#endif

#endif //COMICREADER_LOGGER_H
