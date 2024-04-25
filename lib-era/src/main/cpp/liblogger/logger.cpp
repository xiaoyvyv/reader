//
// Created by why on 4/23/24.
//
#include <stdio.h>
#include <android/log.h>
#include "logger.h"

#define LOG_TAG "NativeLog"

int printf(const char *__fmt, ...) {
    va_list args;
    va_start(args, __fmt);
    __android_log_vprint(ANDROID_LOG_INFO, LOG_TAG, __fmt, args);
    va_end(args);
    return 0;
}